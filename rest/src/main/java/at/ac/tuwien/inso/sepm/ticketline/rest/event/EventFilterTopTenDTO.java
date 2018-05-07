package at.ac.tuwien.inso.sepm.ticketline.rest.event;

import java.util.Objects;

public class EventFilterTopTenDTO {
	
    private Integer month;
    private Long categoryId;

    public EventFilterTopTenDTO() {

    }

    public EventFilterTopTenDTO(Integer month, Long categoryId) {
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
        return "EventFilterTopTenDTO{" +
            "month=" + month +
            ", categoryId=" + categoryId +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventFilterTopTenDTO that = (EventFilterTopTenDTO) o;
        return Objects.equals(month, that.month) &&
            Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(month, categoryId);
    }
}
