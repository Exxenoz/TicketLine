package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    /**
     * Find one seat by its sector and x,y position
     *
     * @param sector the sector of the seat
     * @param positionX the x position of the seat
     * @param positionY the y position of the seat
     * @return the found seat
     */
    Seat findBySectorAndPositionXAndPositionY(Sector sector, int positionX, int positionY);
}
