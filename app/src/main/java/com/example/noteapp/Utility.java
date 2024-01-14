package com.example.noteapp;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Utility {
    static CollectionReference getCollectionReference(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("notes").document(user.getUid()).collection("my_notes");
    }
    static String timeStampToString(Timestamp timestamp){
        return new SimpleDateFormat("DD/MM/YYYY").format(timestamp.toDate());
    }
}
