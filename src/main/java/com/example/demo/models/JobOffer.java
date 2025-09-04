package com.example.demo.models;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;

@Entity
public class JobOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @ManyToOne
    private UserApp creator;  // lien vers l’utilisateur qui a créé l’offre

    public JobOffer(String title, String description, UserApp creator) {
        this.title = title;
        this.description = description;
        this.creator = creator;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserApp getCreator() {
        return creator;
    }

    public void setCreator(UserApp creator) {
        this.creator = creator;
    }
}
