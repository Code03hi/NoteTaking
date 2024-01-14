package com.example.noteapp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.noteapp.databinding.ActivityMainBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    NoteAdapter adapter;
    RecyclerView recyclerView;
    FloatingActionButton addMore;
    MaterialToolbar menu;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    RecyclerView notes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addMore = findViewById(R.id.addMoreNote);
        menu = findViewById(R.id.menu);
        recyclerView = findViewById(R.id.recycler);
        drawerLayout = findViewById(R.id.drawable);
        navigationView = findViewById(R.id.navigationView);

        drawerLayout = findViewById(R.id.drawable);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,menu,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        drawerLayout.setScrimColor(getColor(R.color.colorPrimary));

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            menuItem.isChecked();
            if (id == R.id.home){
                Toast.makeText(MainActivity.this,"Home Clicked",Toast.LENGTH_LONG).show();
                return true;
            }else if (id == R.id.explore){
                Toast.makeText(MainActivity.this,"Home Clicked",Toast.LENGTH_LONG).show();
                return true;
            }else if (id == R.id.library){
                Toast.makeText(MainActivity.this,"Home Clicked",Toast.LENGTH_LONG).show();
                return true;
            }else if (id == R.id.subscription){
                Toast.makeText(MainActivity.this,"Home Clicked",Toast.LENGTH_LONG).show();
                return true;
            }else if (id == R.id.setting){
                Toast.makeText(MainActivity.this,"Home Clicked",Toast.LENGTH_LONG).show();
                return true;
            }else if (id == R.id.account){
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,LoginInterface.class));
                finish();
                Toast.makeText(MainActivity.this,"Home Clicked",Toast.LENGTH_LONG).show();
                return true;
            }
            return true;
        });


        setupRecyclerView();
    }
    public void showMenu(){

    }
    public void setupRecyclerView(){
        Query query = Utility.getCollectionReference().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new NoteAdapter(options,MainActivity.this);
        recyclerView.setAdapter(adapter);

        addMore.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,NoteTaking.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}