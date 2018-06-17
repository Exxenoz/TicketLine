package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.AUTO;

@Entity
public class News {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "seq_news_id")
    @SequenceGenerator(name = "seq_news_id", sequenceName = "seq_news_id")
    private Long id;

    @Column(nullable = false, name = "published_at")
    private LocalDateTime publishedAt;

    @Column(nullable = false)
    @Size(max = 100)
    private String title;

    @Column(nullable = false)
    @Size(max = 50)
    private String summary;

    @Column(nullable = false, length = 10_000)
    private String text;

    @Column(nullable = true, length = 8000000)
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public static NewsBuilder builder() {
        return new NewsBuilder();
    }

    @Override
    public String toString() {
        return "News{" +
            "id=" + id +
            ", publishedAt=" + publishedAt +
            ", title='" + title + '\'' +
            ", summary='" + summary + '\'' +
            ", text='" + text + '\'' +
            ", imageData='" + imageData + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        News news = (News) o;

        if (id != null ? !id.equals(news.id) : news.id != null) {
            return false;
        }
        if (publishedAt != null ? !publishedAt.equals(news.publishedAt) : news.publishedAt != null) {
            return false;
        }
        if (title != null ? !title.equals(news.title) : news.title != null) {
            return false;
        }
        if (summary != null ? !summary.equals(news.summary) : news.summary != null) {
            return false;
        }
        if (imageData != null ? !imageData.equals(news.imageData) : news.imageData != null) {
            return false;
        }
        return text != null ? text.equals(news.text) : news.text == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (publishedAt != null ? publishedAt.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (imageData != null ? imageData.hashCode() : 0);
        return result;
    }

    public static final class NewsBuilder {
        private Long id;
        private LocalDateTime publishedAt;
        private String title;
        private String summary;
        private String text;
        private byte[] imageData;

        private NewsBuilder() {
        }

        public NewsBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public NewsBuilder publishedAt(LocalDateTime publishedAt) {
            this.publishedAt = publishedAt;
            return this;
        }

        public NewsBuilder title(String title) {
            this.title = title;
            return this;
        }

        public NewsBuilder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public NewsBuilder text(String text) {
            this.text = text;
            return this;
        }

        public NewsBuilder imageData(byte[] imageData) {
            this.imageData = imageData;
            return this;
        }

        public News build() {
            News news = new News();
            news.setId(id);
            news.setPublishedAt(publishedAt);
            news.setTitle(title);
            news.setSummary(summary);
            news.setText(text);
            news.setImageData(imageData);
            return news;
        }
    }
}