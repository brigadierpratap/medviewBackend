package com.app.medview.repository;

import com.app.medview.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
    List<Document> getDocumentsByUserId(String userId);
}
