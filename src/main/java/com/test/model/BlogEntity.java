package com.test.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "blog", schema = "mydata", catalog = "")
public class BlogEntity {
    private int id;
    private String title;
    private String content;
    private Date pubDate;
    private UserEntity userByUerId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title", nullable = false, length = 45)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "content", nullable = true, length = 255)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "pub_date", nullable = false)
    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlogEntity that = (BlogEntity) o;
        return id == that.id &&
                Objects.equals(title, that.title) &&
                Objects.equals(content, that.content) &&
                Objects.equals(pubDate, that.pubDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, title, content, pubDate);
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public UserEntity getUserByUerId() {
        return userByUerId;
    }

    public void setUserByUerId(UserEntity userByUerId) {
        this.userByUerId = userByUerId;
    }
}
