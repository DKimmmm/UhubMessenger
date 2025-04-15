package com.example.UhabMessenger.userdata.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Entity
@Table(name = "posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "post_id")
    private UUID postId;
    @Column(name = "tittle")
    private String tittle;
    @Column(name = "description")
    private String description;
    @OneToMany
    @JoinTable(
            name = "post_images", // Имя промежуточной таблицы
            joinColumns = @JoinColumn(name = "post_id"), // Колонка для post
            inverseJoinColumns = @JoinColumn(name = "image_id") // Колонка для Image
    )
    private Set<ImageModel> images = new HashSet<>();

    public void setTittle(String tittle) {
        log.info("tittle is {}", tittle);
        this.tittle = tittle;
    }

    public void setDescription(String description) {
        log.info("des = {}", description);
        this.description = description;
    }
}
