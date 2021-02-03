package com.example.authapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    private TextView lblTitle;
    private Button btnRegisterUser;
    private EditText txtFullName,txtAge,txtEmail,txtPassword;
    private FirebaseAuth mAuth;
    private ProgressBar prbProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        lblTitle = (TextView)findViewById(R.id.lbl_Title);
        lblTitle.setOnClickListener(this);

        btnRegisterUser = (Button)findViewById(R.id.btn_RegisterUser);
        btnRegisterUser.setOnClickListener(this);

        //EditText
        txtFullName = (EditText)findViewById(R.id.txt_Full_Name);
        txtAge = (EditText)findViewById(R.id.txt_Age);
        txtEmail= (EditText)findViewById(R.id.txt_Email);
        txtPassword= (EditText)findViewById(R.id.txt_Password);

        prbProgress = (ProgressBar) findViewById(R.id.pgb_ProgressBar);

        //updateUI(currentUser);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lbl_Title:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.btn_RegisterUser:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String vlcEmail = txtEmail.getText().toString().trim();
        String vlcPassword = txtPassword.getText().toString().trim();
        String vlcFullName = txtFullName.getText().toString().trim();
        String vlcAge = txtAge.getText().toString().trim();

        if(vlcEmail.isEmpty()){
            txtEmail.setError("Email is required");
            txtEmail.requestFocus();
            return;
        }
        if(vlcAge.isEmpty()){
            txtAge.setError("Age is required");
            txtAge.requestFocus();
            return;
        }
        if(vlcFullName.isEmpty()){
            txtFullName.setError("Full Name is required");
            txtFullName.requestFocus();
            return;
        }
        if(vlcPassword.isEmpty()){
            txtPassword.setError("Password is required");
            txtPassword.requestFocus();
            return;
        }
        if(vlcEmail.isEmpty()){
            txtEmail.setError("Email is required");
            txtEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(vlcEmail).matches()){
            txtEmail.setError("E-MAIL is not valid");
            txtEmail.requestFocus();
            return;
        }
        if(vlcPassword.length()<6){
            txtPassword.setError("Min Password length should be 6 characters!");
            txtPassword.requestFocus();
            return;
        }

        prbProgress.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(vlcEmail,vlcPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    User vloUser = new User(vlcFullName,vlcAge,vlcEmail);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(vloUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterUser.this,"User has been registered successfully", Toast.LENGTH_LONG).show();
                                prbProgress.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                Toast.makeText(RegisterUser.this,"Failed to register. Try again!", Toast.LENGTH_LONG).show();
                                prbProgress.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(RegisterUser.this,"Failed to register. Try again!", Toast.LENGTH_LONG).show();
                    prbProgress.setVisibility(View.GONE);
                }
            }
        });
    }
}