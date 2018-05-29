package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.canvas;

import javafx.scene.canvas.GraphicsContext;

public interface CanvasComponent {

    void onClicked();

    void draw(GraphicsContext gc);

    void isInInterval(double topLeft, double lowerRight);
}
