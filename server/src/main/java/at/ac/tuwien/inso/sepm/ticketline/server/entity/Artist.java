package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Objects;

import static javax.persistence.GenerationType.AUTO;

@Entity
public class Artist {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "seq_artist_id")
    @SequenceGenerator(name = "seq_artist_id", sequenceName = "seq_artist_id")
    private Long id;

    @Column(nullable = false)
    @Size(max = 100)
    private String firstName;

    @Column(nullable = false)
    @Size(max = 100)
    private String lastName;

    public Artist() {
    }

    public Artist(@Size(max = 100) String firstName, @Size(max = 100) String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Artist{" +
            "firstName= " + firstName +
            ", lastName= " + lastName +
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
        Artist artist = (Artist) o;
        return Objects.equals(id, artist.id) &&
            Objects.equals(firstName, artist.firstName) &&
            Objects.equals(lastName, artist.lastName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, firstName, lastName);
    }

    public static final class Builder {
        private Long id;
        private String firstName;
        private String lastName;

        private Builder() {
        }

        public static Builder anArtist() {
            return new Builder();
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Artist build() {
            Artist artist = new Artist();
            artist.setId(id);
            artist.setFirstName(firstName);
            artist.setLastName(lastName);
            return artist;
        }
    }
}
