package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    Seat findBySectorAndPositionXAndPositionY(Sector sector, int positionX, int positionY);
}
