package at.ac.tuwien.inso.sepm.ticketline.rest.event;

import java.util.Objects;

public class EventFilterTopTenDTO {
    private Integer month;
    private Integer category;

    public EventFilterTopTenDTO(Integer month, Integer category) {
        this.month = month;
        this.category = category;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "EventFilterTopTenDTO{" +
            "month=" + month +
            ", category=" + category +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventFilterTopTenDTO that = (EventFilterTopTenDTO) o;
        return Objects.equals(month, that.month) &&
            Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(month, category);
    }
}
