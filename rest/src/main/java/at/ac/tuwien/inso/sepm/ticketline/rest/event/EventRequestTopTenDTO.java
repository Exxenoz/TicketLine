package at.ac.tuwien.inso.sepm.ticketline.rest.event;

import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.domain.Pageable;

import java.util.Objects;

@ApiModel(value = "EventRequestTopTenDTO", description = "The event request DTO for top ten event entries via rest")
public class EventRequestTopTenDTO {

    @ApiModelProperty(required = true, readOnly = true, name = "The month of the top ten 10 entries")
    private Integer month;

    @ApiModelProperty(required = true, readOnly = true, name = "The category id of the top 10 entries")
    private Long categoryId;

    public EventRequestTopTenDTO() {
    }

    public EventRequestTopTenDTO(Integer month, Long categoryId) {
        this.month = month;
        this.categoryId = categoryId;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "EventRequestTopTenDTO{" +
            "month=" + month +
            ", categoryId=" + categoryId +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventRequestTopTenDTO that = (EventRequestTopTenDTO) o;
        return Objects.equals(month, that.month) &&
            Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(month, categoryId);
    }
}
