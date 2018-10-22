package edu.osu.cse5236.group10.packmap.data.store;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;


public abstract class AbstractStore {

    protected FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addDocument(String collection, Map<String, Object> data,
                            OnSuccessListener<DocumentReference> onSuccessListener,
                            OnFailureListener onFailureListener) {
        db.collection(collection)
            .add(data)
            .addOnSuccessListener(onSuccessListener)
            .addOnFailureListener(onFailureListener);
    }

    public void addDocument(String collection, Map<String, Object> data) {
        addDocument(collection, data, getDocumentReferenceOnSuccessListener(), getOnFailureListener());
    }

    public void getAllDocuments(String collection,
                                OnCompleteListener<QuerySnapshot> onCompleteListener) {
        getAllDocuments(collection, onCompleteListener, getOnFailureListener());
    }

    public void getAllDocuments(String collection,
                                OnCompleteListener<QuerySnapshot> onCompleteListener,
                                OnFailureListener onFailureListener) {
        db.collection(collection)
                .get()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    private OnSuccessListener<DocumentReference> getDocumentReferenceOnSuccessListener() {
        return new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(getTag(), "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        };
    }

    private OnFailureListener getOnFailureListener() {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(getTag(), "Error adding document", e);
            }
        };
    }

    public abstract String getTag();
}
