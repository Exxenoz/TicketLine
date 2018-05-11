package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;

import java.util.Objects;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

@Entity
public class Sector {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "seq_sector_id")
    @SequenceGenerator(name = "seq_sector_id", sequenceName = "seq_sector_id")
    private Long id;

    @ManyToOne
    private SectorCategory category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SectorCategory getCategory() {
        return category;
    }

    public void setCategory(SectorCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Sector{" +
            "id=" + id +
            ", category=" + category +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sector sector = (Sector) o;

        return Objects.equals(id, sector.id) &&
            Objects.equals(category, sector.category);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, category);
    }
}