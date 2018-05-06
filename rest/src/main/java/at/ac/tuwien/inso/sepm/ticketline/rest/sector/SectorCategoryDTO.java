package at.ac.tuwien.inso.sepm.ticketline.rest.sector;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Objects;

@ApiModel(value = "SectorCategoryDTO", description = "The sector category DTO for sector category entries via rest")
public class SectorCategoryDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, readOnly = true, name = "The name of the sector category")
    private String name;

    @ApiModelProperty(required = true, readOnly = true, name = "The base price modifier of the sector category")
    private BigDecimal basePriceMod;

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

    public BigDecimal getBasePriceMod() {
        return basePriceMod;
    }

    public void setBasePriceMod(BigDecimal basePriceMod) {
        this.basePriceMod = basePriceMod;
    }

    @Override
    public String toString() {
        return "SectorCategoryDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", basePriceMod=" + basePriceMod +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SectorCategoryDTO that = (SectorCategoryDTO) o;

        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(basePriceMod, that.basePriceMod);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, basePriceMod);
    }
}
