package com.CutlerDevelopment.murraycup.Models;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class DatabaseConnectionHandler {

    FirebaseFirestore db;

    public DatabaseConnectionHandler () {
        db = FirebaseFirestore.getInstance();
    }

    public FirebaseFirestore dbConnection()  {
        return FirebaseFirestore.getInstance();
    }

    public void addDocument(String collectionName, Map<String, Object> documentToAdd) {
        db.collection(collectionName).document()
                .set(documentToAdd);
    }

    public void updateDocumentStringField(String collectionName,
                                          String documentReference,
                                          String fieldName,
                                          String newValue) {
        db.collection(collectionName).document(documentReference)
                .update(fieldName, newValue);
    }

    public void updateDocumentIntField(String collectionName,
                                       String documentReference,
                                       String fieldName,
                                       int newValue) {
        db.collection(collectionName).document(documentReference)
                .update(fieldName, newValue);
    }

    public void deleteDocument(String collectionName, String documentReference) {
        db.collection(collectionName).document(documentReference)
                .delete();
    }
}
