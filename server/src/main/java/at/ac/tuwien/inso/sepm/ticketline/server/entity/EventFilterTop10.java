package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import java.util.Objects;

public class EventFilterTop10 {

    private Integer month;

    private Long categoryId;

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
        return "EventFilterTop10{" +
            "month=" + month +
            ", categoryId=" + categoryId +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventFilterTop10 that = (EventFilterTop10) o;

        return Objects.equals(month, that.month) &&
            Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(month, categoryId);
    }
}
