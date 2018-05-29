package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.canvas;

import javafx.scene.canvas.GraphicsContext;

public interface CanvasComponent {

    void draw(GraphicsContext gc);

    boolean isClicked(double x, double y);
}
