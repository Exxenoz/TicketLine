package at.ac.tuwien.inso.sepm.ticketline.rest.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

@ApiModel(value = "PageRequestDTO", description = "DTO for page requests via rest")
public class PageRequestDTO {

    @ApiModelProperty(required = true, readOnly = true, name = "The page of the page request")
    private int page;

    @ApiModelProperty(required = true, readOnly = true, name = "The size of the page request")
    private int size;

    @ApiModelProperty(required = true, readOnly = true, name = "The sort direction of the page request")
    private Sort.Direction sortDirection;

    @ApiModelProperty(required = true, readOnly = true, name = "The sort column name of the page request")
    private String sortColumnName;

    public PageRequestDTO() {
    }

    public PageRequestDTO(int page, int size, Sort.Direction sortDirection, String sortColumnName) {
        this.page = page;
        this.size = size;
        this.sortDirection = sortDirection;
        this.sortColumnName = sortColumnName;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Sort.Direction getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(Sort.Direction sortDirection) {
        this.sortDirection = sortDirection;
    }

    public String getSortColumnName() {
        return sortColumnName;
    }

    public void setSortColumnName(String sortColumnName) {
        this.sortColumnName = sortColumnName;
    }

    @Override
    public String toString() {
        return "PageRequestDTO{" +
            "page=" + page +
            ", size=" + size +
            ", sortDirection=" + sortDirection +
            ", sortColumnName='" + sortColumnName + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PageRequestDTO that = (PageRequestDTO) o;

        return page == that.page &&
            size == that.size &&
            sortDirection == that.sortDirection &&
            Objects.equals(sortColumnName, that.sortColumnName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(page, size, sortDirection, sortColumnName);
    }

    public Pageable getPageable() {
        return (sortDirection != null && sortColumnName != null) ?
            PageRequest.of(page, size, Sort.by(sortDirection, sortColumnName)) :
            PageRequest.of(page, size);
    }
}
