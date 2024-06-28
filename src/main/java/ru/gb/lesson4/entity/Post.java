package ru.gb.lesson4.entity;

import jakarta.persistence.*;


import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "post")
public class Post {

    //private final Date currentDate = new Date();

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String title;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    public Post() {
        timestamp = new Timestamp(new Date().getTime());
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
