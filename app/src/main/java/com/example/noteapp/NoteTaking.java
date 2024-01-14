package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteapp.databinding.ActivityNoteTakingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

public class NoteTaking extends AppCompatActivity {
    ActivityNoteTakingBinding binding;
    EditText title,message;
    TextView parentHead,delete;
    String headTitle,content,time,docId;
    ImageView save;
    boolean isEditMode = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteTakingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Defined the ID's of the all
        title = binding.subHead;
        message = binding.message;
        save = binding.saveNote;
        parentHead = binding.parentHead;
        delete = binding.delete;

        // receive data from Intent
        headTitle = getIntent().getStringExtra("Title");
        content = getIntent().getStringExtra("content");
        time = getIntent().getStringExtra("timestamp");
        docId = getIntent().getStringExtra("docId");

        if (docId != null && !docId.isEmpty()){
            isEditMode = true;
        }

        if (isEditMode){
            parentHead.setText("Update Your Note");
            delete.setVisibility(View.VISIBLE);
        }

        delete.setOnClickListener(v -> {
            deleteNoteFromFirebase();
            finish();
        });

        title.setText(headTitle);
        message.setText(content);

        save.setOnClickListener(view -> {
            saveNote();
            finish();
        });
    }
    public void saveNote(){
        String topic = title.getText().toString();
        String content = message.getText().toString();
        if (topic == null || topic.isEmpty()){
            title.setError("Title is required");
            title.requestFocus();
            return;
        }
        Note note = new Note();
        note.setTitle(topic);
        note.setContent(content);
        note.setTimestamp(Timestamp.now());
        saveNoteInDatabase(note);
    }
    public void saveNoteInDatabase(Note note){
        DocumentReference reference;
        if (isEditMode){
            reference = Utility.getCollectionReference().document(docId);
        }
        else {
            reference = Utility.getCollectionReference().document();
        }
        reference.set(note).addOnCompleteListener(NoteTaking.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(NoteTaking.this,"Note adding successfully",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(NoteTaking.this,"Try Again",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void deleteNoteFromFirebase(){
        DocumentReference reference;
        reference = Utility.getCollectionReference().document(docId);
        reference.delete().addOnCompleteListener(NoteTaking.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(NoteTaking.this,"Note deleted successfully",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(NoteTaking.this,"Try Again",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}