package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventTypeDTO;

public enum EventType {
    SEAT,
    SECTOR
    ;

    public static EventType from(EventTypeDTO eventTypeDTO) {
        switch (eventTypeDTO) {
            case SEAT: return SEAT;
            case SECTOR: return SECTOR;
        }
        return null;
    }
}
