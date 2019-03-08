package com.example.katy.facedetention;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {
    EditText emailFld;
    Button resetPasswordBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        emailFld = (EditText) findViewById(R.id.EmailID);
        resetPasswordBtn = (Button) findViewById(R.id.button3);
        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valid = true;

                if (emailFld.getText().toString().isEmpty()) {
                    emailFld.setError(("Empty! Please Enter Your emailFld"));
                    if (!emailFld.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                        emailFld.setError((" Please Enter Your emailFld"));

                    }
                } else {
                    resetPassword(emailFld.getText().toString());
                    Intent i = new Intent(ResetPassword.this,LoginActivity.class);
                    startActivity(i);
                    finish();

                }
            }
        });
    }

    public void resetPassword(String email){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Reset Password", "emailFld sent.");
                            UtilitiesHelper.showToast(getBaseContext(), "Please check your email");
                        }else{
                            Log.d("Reset Password", task.getException().getMessage());
                            UtilitiesHelper.showToast(getBaseContext(), task.getException().getMessage());

                        }
                    }
                });
    }

}
