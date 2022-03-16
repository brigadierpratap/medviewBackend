package com.app.medview.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GenericGenerator(name="doc-uuid",strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "doc-uuid")
    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;
    private String type;

    @Column(nullable = false)
    private String fileKey;

    @Column(name = "user_id")
    private String userId;



    private String description;

    public Document() {
    }

    public Document(String type, String fileKey, String userId,String description) {
        this.type = type;
        this.fileKey = fileKey;
        this.userId = userId;
        this.description=description;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String url) {
        this.fileKey = url;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
