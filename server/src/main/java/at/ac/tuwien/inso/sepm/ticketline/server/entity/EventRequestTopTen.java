package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import java.util.Objects;

public class EventRequestTopTen {

    private Integer month;
    private Integer year;
    private Long categoryId;

    public EventRequestTopTen() {
    }

    public EventRequestTopTen(Integer month, Integer year, Long categoryId) {
        this.month = month;
        this.year = year;
        this.categoryId = categoryId;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "EventRequestTopTen{" +
            "month=" + month +
            "year=" + year +
            ", categoryId=" + categoryId +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventRequestTopTen that = (EventRequestTopTen) o;
        return Objects.equals(month, that.month) &&
            Objects.equals(year, that.year) &&
            Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(month, year, categoryId);
    }
}
