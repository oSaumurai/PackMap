package edu.osu.cse5236.group10.packmap.data;

import com.annimon.stream.Stream;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import edu.osu.cse5236.group10.packmap.data.model.BaseDocument;

public class DataUtils {

    public static <U extends BaseDocument> U getObject(DocumentSnapshot documentSnapshot,
                                                       Class<U> clazz) {
        U object = documentSnapshot.toObject(clazz);
        object.setDocumentId(documentSnapshot.getId());
        return object;
    }

    public static <U extends BaseDocument> List<U> getObjectList(QuerySnapshot querySnapshot,
                                                                 Class<U> clazz) {
        return Stream.of(querySnapshot.getDocuments())
                .map(documentSnapshot -> getObject(documentSnapshot, clazz))
                .toList();
    }
}
