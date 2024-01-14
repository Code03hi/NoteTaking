package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.noteapp.databinding.ActivityLoginInterfaceBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginInterface extends AppCompatActivity {
    ActivityLoginInterfaceBinding binding;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginInterfaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginInterface.this,CreateAccount.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                if (TextUtils.isEmpty(email)){
                    binding.email.setError("Please enter your email address");
                    binding.email.requestFocus();
                }else if (TextUtils.isEmpty(password)){
                    binding.password.setError("Please enter your password");
                    binding.password.requestFocus();
                }else {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.login.setVisibility(View.GONE);
                    loginUser(email,password);
                }
            }
        });
    }
    public void loginUser(String email,String password){
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginInterface.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    binding.progressBar.setVisibility(View.GONE);
                    binding.login.setVisibility(View.GONE);
                    FirebaseUser user = auth.getCurrentUser();
                    if (user.isEmailVerified()){
                        Intent intent = new Intent(LoginInterface.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else{
                        showDialog();
                    }
                }else{
                    Toast.makeText(LoginInterface.this,"User login un-successfully",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void showDialog(){
        AlertDialog dialog = new AlertDialog.Builder(LoginInterface.this)
                .setMessage("E-mail is not verified")
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).show();
    }
}