package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Objects;

import static javax.persistence.GenerationType.AUTO;

@Entity
public class SectorCategory {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "seq_sector_category_id")
    @SequenceGenerator(name = "seq_sector_category_id", sequenceName = "seq_sector_category_id")
    private Long id;

    @Column(nullable = false)
    @Size(max = 64)
    private String name;

    @Column(nullable = false)
    private Long basePriceMod;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBasePriceMod() {
        return basePriceMod;
    }

    public void setBasePriceMod(Long basePriceMod) {
        this.basePriceMod = basePriceMod;
    }

    @Override
    public String toString() {
        return "SectorCategory{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", basePriceMod=" + basePriceMod +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SectorCategory that = (SectorCategory) o;

        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(basePriceMod, that.basePriceMod);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, basePriceMod);
    }
}
