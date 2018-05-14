package at.ac.tuwien.inso.sepm.ticketline.rest.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;

@ApiModel(value = "PageResponseDTO", description = "DTO for page responses via rest")
public class PageResponseDTO<T> {
    @ApiModelProperty(required = true, readOnly = true, name = "The content of the page response")
    private List<T> content;
    @ApiModelProperty(required = true, readOnly = true, name = "The total pages of the page response")
    private int totalPages;

    public PageResponseDTO() {
    }

    public PageResponseDTO(List<T> content, int totalPages) {
        this.content = content;
        this.totalPages = totalPages;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public String toString() {
        return "PageResponseDTO{" +
            "content=" + content +
            ", totalPages=" + totalPages +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageResponseDTO<?> that = (PageResponseDTO<?>) o;
        return totalPages == that.totalPages &&
            Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, totalPages);
    }
}