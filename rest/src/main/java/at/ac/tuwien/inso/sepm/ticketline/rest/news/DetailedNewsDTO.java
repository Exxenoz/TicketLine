package at.ac.tuwien.inso.sepm.ticketline.rest.news;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(value = "DetailedNewsDTO", description = "A detailed DTO for news entries via rest")
public class DetailedNewsDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(readOnly = true, name = "The date and time when the news was published")
    private LocalDateTime publishedAt;

    @ApiModelProperty(required = true, name = "The title of the news")
    private String title;

    @ApiModelProperty(required = true, name = "The text content of the news")
    private String text;

    @ApiModelProperty(name = "The optional image of the news")
    private byte[] imageData;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    @Override
    public String toString() {
        return "DetailedNewsDTO{" +
            "id=" + id +
            ", publishedAt=" + publishedAt +
            ", title='" + title + '\'' +
            ", text='" + text + '\'' +
            ", imageData='" + imageData + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DetailedNewsDTO that = (DetailedNewsDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (publishedAt != null ? !publishedAt.equals(that.publishedAt) : that.publishedAt != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (imageData != null ? !imageData.equals(that.imageData) : that.imageData != null) return false;
        return text != null ? text.equals(that.text) : that.text == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (publishedAt != null ? publishedAt.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (imageData != null ? imageData.hashCode() : 0);
        return result;
    }

    public static NewsDTOBuilder builder() {
        return new NewsDTOBuilder();
    }

    public static final class NewsDTOBuilder {

        private Long id;
        private LocalDateTime publishedAt;
        private String title;
        private String text;
        private byte[] imageData;

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

        public NewsDTOBuilder text(String text) {
            this.text = text;
            return this;
        }

        public NewsDTOBuilder imageData(byte[] imageData) {
            this.imageData = imageData;
            return this;
        }

        public DetailedNewsDTO build() {
            DetailedNewsDTO newsDTO = new DetailedNewsDTO();
            newsDTO.setId(id);
            newsDTO.setPublishedAt(publishedAt);
            newsDTO.setTitle(title);
            newsDTO.setText(text);
            newsDTO.setImageData(imageData);
            return newsDTO;
        }
    }
}