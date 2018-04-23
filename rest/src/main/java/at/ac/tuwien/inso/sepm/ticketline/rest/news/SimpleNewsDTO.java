package at.ac.tuwien.inso.sepm.ticketline.rest.news;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(value = "DetailedNewsDTO", description = "A simple DTO for news entries via rest")
public class SimpleNewsDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, readOnly = true, name = "The date and time when the news was published")
    private LocalDateTime publishedAt;

    @ApiModelProperty(required = true, readOnly = true, name = "The title of the news")
    private String title;

    @ApiModelProperty(required = true, readOnly = true, name = "The summary of the news")
    private String summary;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "SimpleNewsDTO{" +
            "id=" + id +
            ", publishedAt=" + publishedAt +
            ", title='" + title + '\'' +
            ", summary='" + summary + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleNewsDTO that = (SimpleNewsDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (publishedAt != null ? !publishedAt.equals(that.publishedAt) : that.publishedAt != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return summary != null ? summary.equals(that.summary) : that.summary == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (publishedAt != null ? publishedAt.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        return result;
    }

    public static NewsDTOBuilder builder() {
        return new NewsDTOBuilder();
    }

    public static final class NewsDTOBuilder {

        private Long id;
        private LocalDateTime publishedAt;
        private String title;
        private String summary;

        public NewsDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public NewsDTOBuilder publishedAt(LocalDateTime publishedAt) {
            this.publishedAt = publishedAt;
            return this;
        }

        public NewsDTOBuilder title(String title) {
            this.title = title;
            return this;
        }

        public NewsDTOBuilder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public SimpleNewsDTO build() {
            SimpleNewsDTO newsDTO = new SimpleNewsDTO();
            newsDTO.setId(id);
            newsDTO.setPublishedAt(publishedAt);
            newsDTO.setTitle(title);
            newsDTO.setSummary(summary);
            return newsDTO;
        }
    }
}