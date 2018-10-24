package edu.osu.cse5236.group10.packmap.data.store;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import edu.osu.cse5236.group10.packmap.data.model.BaseDocument;


public abstract class AbstractStore {

    protected FirebaseFirestore db = FirebaseFirestore.getInstance();

    protected void addDocument(BaseDocument data,
                            OnSuccessListener<DocumentReference> onSuccessListener,
                            OnFailureListener onFailureListener) {
        db.collection(getCollection())
            .add(data)
            .addOnSuccessListener(onSuccessListener)
            .addOnFailureListener(onFailureListener);
    }

    protected void addDocument(BaseDocument data) {
        addDocument(data, getDocumentReferenceOnSuccessListener(), getOnFailureListener());
    }

    protected void setDocument(BaseDocument data, OnSuccessListener<Void> onSuccessListener,
                            OnFailureListener onFailureListener) {
        db.collection(getCollection()).document(data.getDocumentId())
                .set(data)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    protected void setDocument(BaseDocument data) {
        setDocument(data, getVoidOnSuccessListener(), getOnFailureListener());
    }

    protected void getDocument(BaseDocument data,
                               OnCompleteListener<DocumentSnapshot> onCompleteListener,
                               OnFailureListener onFailureListener) {
        db.collection(getCollection()).document(data.getDocumentId())
                .get()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    protected void getAllDocuments(OnCompleteListener<QuerySnapshot> onCompleteListener) {
        getAllDocuments(onCompleteListener, getOnFailureListener());
    }

    protected void getAllDocuments(OnCompleteListener<QuerySnapshot> onCompleteListener,
                                   OnFailureListener onFailureListener) {
        db.collection(getCollection())
                .get()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    protected void deleteDocument(BaseDocument data) {
        deleteDocument(data, getVoidOnSuccessListener(), getOnFailureListener());
    }

    protected void deleteDocument(BaseDocument data, OnSuccessListener<Void> onSuccessListener,
                                  OnFailureListener onFailureListener) {
        db.collection(getCollection()).document(data.getDocumentId())
                .delete()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    protected OnSuccessListener<DocumentReference> getDocumentReferenceOnSuccessListener() {
        return new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(getTag(), "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        };
    }

    protected OnSuccessListener<Void> getVoidOnSuccessListener() {
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(getTag(), "DocumentSnapshot successfully handled");
            }
        };
    }

    protected OnFailureListener getOnFailureListener() {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(getTag(), "Error when interacting with Firestore", e);
            }
        };
    }

    protected abstract String getCollection();

    protected abstract String getTag();
}
