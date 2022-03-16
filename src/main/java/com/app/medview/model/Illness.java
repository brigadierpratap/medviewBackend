package com.app.medview.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "illnesses")
public class Illness {

    @Id
    @GenericGenerator(name="ill-uuid",strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "ill-uuid")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "user_id",nullable = false)
    private String userId;

    public Illness() {
    }

    public Illness(String name, String userId) {
        this.name = name;
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
