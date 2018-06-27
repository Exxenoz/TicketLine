package at.ac.tuwien.inso.sepm.ticketline.client.validator;


import at.ac.tuwien.inso.sepm.ticketline.client.exception.NewsValidationException;
import javafx.embed.swing.JFXPanel;
import javafx.scene.web.HTMLEditor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import javafx.scene.control.TextField;
import org.springframework.test.context.junit4.SpringRunner;

import static at.ac.tuwien.inso.sepm.ticketline.client.validator.NewsValidator.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("unit-test")
public class NewsValidatorTest {

    private final JFXPanel fxPanel = new JFXPanel();


    //VALIDATE TITLE

    @Test
    public void validateTitleTest() throws NewsValidationException {
        TextField textField = new TextField();
        textField.setText("Title");
        validateTitle(textField);
    }

    @Test(expected = NewsValidationException.class)
    public void validateTitleNullTest() throws NewsValidationException {
        TextField textField = new TextField();
        validateTitle(textField);
    }

    @Test(expected = NewsValidationException.class)
    public void validateTitleTooShortTest() throws NewsValidationException {
        TextField textField = new TextField();
        textField.setText("");
        validateTitle(textField);
    }

    @Test(expected = NewsValidationException.class)
    public void validateTitleTooLongTest() throws NewsValidationException {
        TextField textField = new TextField();
        textField.setText("veBE2yN1cXvyMQahtUfppjH5x3MioudGCNHA5io6ZGFwkEJKQxNveBE2yN1cXvyMQahtUfppjH5x3MioudGCNHA5io6ZGFwkEJKQxN");
        validateTitle(textField);
    }

    //VALIDATE IMAGE

    @Test(expected = NewsValidationException.class)
    public void validateInvalidImagePathTest() throws NewsValidationException {
        validateImage("notavalidpath");
    }

    //VALIDATE FILE NAME

    @Test
    public void validateFileNameTest() throws NewsValidationException {
        validateFileName("filename.jpg");
    }

    @Test(expected = NewsValidationException.class)
    public void validateFileNameNoExtensionTest() throws NewsValidationException {
        validateFileName("filename");
    }

    @Test(expected = NewsValidationException.class)
    public void validateFileNameNoValidExtensionTest() throws NewsValidationException {
        validateFileName("filename.xy");
    }

    //VALIDATE SUMMARY

    @Test
    public void validateSummaryTest() throws NewsValidationException {
        TextField textField = new TextField();
        textField.setText("This is a Summary");
        validateSummary(textField);
    }

    @Test(expected = NewsValidationException.class)
    public void validateSummaryNullTest() throws NewsValidationException {
        TextField textField = new TextField();
        validateSummary(textField);
    }

    @Test(expected = NewsValidationException.class)
    public void validateSummaryTooShortTest() throws NewsValidationException {
        TextField textField = new TextField();
        textField.setText("");
        validateSummary(textField);
    }

    @Test(expected = NewsValidationException.class)
    public void validateSummaryTooLongTest() throws NewsValidationException {
        TextField textField = new TextField();
        textField.setText("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ");
        validateSummary(textField);
    }
}
