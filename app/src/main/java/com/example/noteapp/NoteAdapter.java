package com.example.noteapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note,NoteAdapter.NoteHolder> {
    Context context;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull Note note) {
        holder.title.setText(note.getTitle());
        holder.content.setText(note.getContent());
        holder.update.setText(Utility.timeStampToString(note.timestamp));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,NoteTaking.class);
                intent.putExtra("Title",note.title);
                intent.putExtra("content",note.content);
                String docId = getSnapshots().getSnapshot(position).getId();
                intent.putExtra("docId",docId);
                intent.putExtra("timestamp",Utility.timeStampToString(note.getTimestamp()));
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteHolder(LayoutInflater.from(context).inflate(R.layout.note_for_recycler,parent,false));
    }

    public static class NoteHolder extends RecyclerView.ViewHolder{
        TextView title,content,update;
        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.message);
            update = itemView.findViewById(R.id.time);
        }
    }
}
