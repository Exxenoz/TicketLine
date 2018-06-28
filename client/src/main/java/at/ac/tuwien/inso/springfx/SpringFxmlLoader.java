package at.ac.tuwien.inso.springfx;

import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import com.google.common.base.Preconditions;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

/**
 * Bridge between Spring and JavaFX which allows to inject Spring beans in JavaFX controller.
 */
@Component
public class SpringFxmlLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ApplicationContext applicationContext;

    public SpringFxmlLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Generate fxml loader based on the given URL.
     *
     * @param url the url
     * @return the FXML loader
     */
    private FXMLLoader generateFXMLLoader(String url) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getLocationForUrl(url));
        fxmlLoader.setResources(BundleManager.getBundle());
        fxmlLoader.setControllerFactory(clazz -> {
            LOGGER.debug("Trying to retrieve spring bean for type {}", clazz.getName());
            Object bean = null;
            try {
                bean = applicationContext.getBean(clazz);
            } catch (NoSuchBeanDefinitionException e) {
                LOGGER.warn("No qualifying spring bean of type {} found", clazz.getName());
            }
            if (bean == null) {
                LOGGER.debug("Trying to instantiating class of type {}", clazz.getName());
                try {
                    bean = clazz.getDeclaredConstructor().newInstance();
                } catch (
                    InvocationTargetException |
                        NoSuchMethodException |
                        InstantiationException |
                        IllegalAccessException e
                    ) {
                    LOGGER.error("Failed to instantiate bean of type {}", clazz.getName(), e);
                    throw new RuntimeException("Failed to instantiate bean of type " + clazz.getName(), e);
                }
            }
            return bean;
        });
        return fxmlLoader;
    }

    private URL getLocationForUrl(String url) {
        URL location = SpringFxmlLoader.class.getResource(url);
        Preconditions.checkState(location != null, "Resource " + url + " not found.");
        return location;
    }

    /**
     * Loads object hierarchy from the FXML document given in the URL.
     *
     * @param url the url
     * @return the object
     */
    public <OBJECT> OBJECT load(String url) {
        try {
            return generateFXMLLoader(url).load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads and wraps an object hierarchy.
     *
     * @param url the url
     * @return the load wrapper
     */
    public <CONTROLLER, OBJECT> AnyWrapper<CONTROLLER, OBJECT> loadAndWrapAny(String url) {
        FXMLLoader fxmlLoader = generateFXMLLoader(url);
        try {
            OBJECT loadedObject = fxmlLoader.load();
            return new AnyWrapper<>(fxmlLoader.getController(), loadedObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads and wraps an object hierarchy.
     *
     * @param url the url
     * @return the load wrapper
     */
    public <CONTROLLER> Wrapper<CONTROLLER> loadAndWrap(String url) {
        FXMLLoader fxmlLoader = generateFXMLLoader(url);
        try {
            Node loadedObject = fxmlLoader.load();
            return new Wrapper<>(fxmlLoader.getController(), loadedObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The Wrapper class which wraps a node and any controller.
     */
    public class Wrapper<CONTROLLER> extends AnyWrapper<CONTROLLER, Node> {
        /**
         * Instantiates a new load wrapper.
         *
         * @param controller   the controller
         * @param loadedObject the loaded object
         */
        Wrapper(CONTROLLER controller, Node loadedObject) {
            super(controller, loadedObject);
        }
    }

    /**
     * The AnyWrapper class which wraps any object and any controller.
     */
    public class AnyWrapper<CONTROLLER, OBJECT> {

        private final CONTROLLER controller;
        private final OBJECT loadedObject;

        /**
         * Instantiates a new load wrapper.
         *
         * @param controller   the controller
         * @param loadedObject the loaded object
         */
        AnyWrapper(CONTROLLER controller, OBJECT loadedObject) {
            this.controller = controller;
            this.loadedObject = loadedObject;
        }

        /**
         * Gets the controller.
         *
         * @return the controller
         */
        public CONTROLLER getController() {
            return controller;
        }

        /**
         * Gets the loaded object.
         *
         * @return the loaded object
         */
        public OBJECT getLoadedObject() {
            return loadedObject;
        }
    }
}