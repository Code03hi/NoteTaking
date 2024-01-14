package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.noteapp.databinding.ActivityCreateAccountBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class CreateAccount extends AppCompatActivity {
    FirebaseAuth auth;
    ActivityCreateAccountBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // If user already have an account
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccount.this,LoginInterface.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        auth = FirebaseAuth.getInstance();
        binding.createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
    }
    public void createUser(){
        String email = binding.email.getText().toString();
        String passWord = binding.password.getText().toString();
        String confirmPassword = binding.confirmPassword.getText().toString();
        boolean valid = validateData(email,passWord,confirmPassword);
        if (!valid) {
            Toast.makeText(this, "User Creation is Failed", Toast.LENGTH_SHORT).show();
            return;
        }
        registerUser(email,passWord);
    }
    public boolean validateData(String email,String password,String confirmPassword){
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.email.setError("Please Enter Valid Email");
            binding.email.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(email)) {
            binding.email.setError("Please Enter Email");
            binding.email.requestFocus();
            return false;
        }
        if (password.length() < 9){
            binding.password.setError("Password should be eight digit");
            binding.password.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(password)) {
            binding.password.setError("Please Enter Email");
            binding.password.requestFocus();
            return false;
        }
        if (Pattern.matches(password,confirmPassword)){
            binding.password.setError("Password is not match");
            binding.confirmPassword.setError("Password is not match");
            binding.password.requestFocus();
            binding.confirmPassword.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(confirmPassword)){
            binding.password.setError("Please Enter Password");
            binding.password.requestFocus();
            return false;
        }
        return true;
    }
    void changeInProgress(boolean isProgress){
        if (isProgress){
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.createAccount.setVisibility(View.GONE);
        }else{
            binding.progressBar.setVisibility(View.GONE);
            binding.createAccount.setVisibility(View.VISIBLE);
        }
    }
    public void registerUser(String email,String password){
        changeInProgress(true);
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                FirebaseUser user = auth.getCurrentUser();
                if (task.isSuccessful()){
                    Toast.makeText(CreateAccount.this, "Successful, Please Verify Email", Toast.LENGTH_SHORT).show();
                    auth.getCurrentUser().sendEmailVerification();
                }else{
                    Toast.makeText(CreateAccount.this, "Un-successful, Please check the credential", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}