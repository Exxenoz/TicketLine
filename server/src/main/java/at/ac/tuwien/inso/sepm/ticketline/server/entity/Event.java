package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "seq_event_id")
    @SequenceGenerator(name = "seq_event_id", sequenceName = "seq_event_id")
    private Long id;

    @ManyToMany
    private Set<Artist> artists = new HashSet<>();

    @Column(nullable = false)
    @Size(max = 100)
    private String name;

    @Column(nullable = false)
    private EventType eventType;

    @Column(nullable = false)
    @Size(max = 1000)
    private String description;

    public Event() {
    }

    public Event(Set<Artist> artists, @Size(max = 100) String name, EventType eventType, @Size(max = 1000) String description) {
        this.artists = artists;
        this.name = name;
        this.eventType = eventType;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Artist> getArtists() {
        return artists;
    }

    public void setArtists(Set<Artist> artists) {
        this.artists = artists;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
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
        return "Event{" +
            ", name='" + name + '\'' +
            ", eventType=" + eventType +
            ", description='" + description + '\'' +
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
        Event event = (Event) o;
        return Objects.equals(id, event.id) &&
            Objects.equals(artists, event.artists) &&
            Objects.equals(name, event.name) &&
            Objects.equals(eventType, event.eventType) &&
            Objects.equals(description, event.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, eventType, description);
    }
}
