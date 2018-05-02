package at.ac.tuwien.inso.sepm.ticketline.rest.event;

import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class EventDTO {

    private Long id;
    private String name;
    private Set<ArtistDTO> artists = new HashSet<>();
    private EventTypeDTO eventType;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Set<ArtistDTO> getArtists() {
        return artists;
    }

    public void setArtists(Set<ArtistDTO> artists) {
        this.artists = artists;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventTypeDTO getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeDTO eventType) {
        this.eventType = eventType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "EventDTO{" +
            " name='" + name + '\'' +
            ", eventType=" + eventType +
            ", description='" + description + '\'' +
            ", artist=" + artists.toString() + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventDTO eventDTO = (EventDTO) o;
        return Objects.equals(id, eventDTO.id) &&
            Objects.equals(name, eventDTO.name) &&
            Objects.equals(eventType, eventDTO.eventType) &&
            Objects.equals(description, eventDTO.description) &&
            Objects.equals(artists, eventDTO.artists)
            ;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, eventType, description, artists);
    }
}
