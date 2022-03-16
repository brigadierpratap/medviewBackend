package com.app.medview.service;

import com.app.medview.model.Document;

import java.util.List;

public interface DocumentService   {
    Document saveDocument(Document document);
    List<Document> getDocument(String userId);
}
