package at.ac.tuwien.inso.sepm.ticketline.server.validator.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Sector;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalHallValidationException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.HallRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.validator.HallPlanValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class SimpleHallPlanValidator implements HallPlanValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private HallRepository hallRepository;

    @Override
    public Hall findHallPlanById(Long id) {
        return hallRepository.getOne(id);
    }

    @Override
    public void checkSectorsAgainstHall(List<Sector> sectors, Hall hall) throws InternalHallValidationException {
        //First check if sectors are in hall
        for (Sector s : sectors) {
            if (!hall.getSectors().contains(s)) {
                LOGGER.warn("The sector '{}' is not part of this hall", s);
                throw new InternalHallValidationException();
            }
        }
    }

    @Override
    public void checkSeatsAgainstSectors(List<Seat> seats, List<Sector> sectors) throws InternalHallValidationException {
        for (Seat s : seats) {
            boolean contained = false;
            for (Sector sec : sectors) {
                if (s.getSector().getId().equals(sec.getId())) {
                    contained = true;
                    if (s.getPositionX() < 0 || s.getPositionX() > sec.getSeatsPerRow()) {
                        LOGGER.warn("The seat '{}' is not inside the horizontal dimensions of sector '{}'", s, sec);
                        throw new InternalHallValidationException();
                    }

                    if (s.getPositionY() < 0 || s.getPositionY() > sec.getRows()) {
                        LOGGER.warn("The seat '{}' is not inside the vertical dimensions of sector '{}'", s, sec);
                        throw new InternalHallValidationException();
                    }
                }
            }

            if(!contained) {
                LOGGER.warn("The seat '{}' was not found in any sector", s);
                throw new InternalHallValidationException();
            }
        }
    }
}
