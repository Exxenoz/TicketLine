package at.ac.tuwien.inso.sepm.ticketline.client.gui.reservation.seating.canvas;

public interface CanvasComponent {

    void onClicked();

    void draw();

    void isInInterval(double topLeft, double lowerRight);
}
