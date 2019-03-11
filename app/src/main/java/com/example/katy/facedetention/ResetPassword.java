package com.example.katy.facedetention;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {
    TextView emailLbl;
    Button resetPasswordBtn;
    EditText emailFld;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        emailFld = findViewById(R.id.EmailFieldET);

        emailLbl =  findViewById(R.id.EmailID);

        resetPasswordBtn =  findViewById(R.id.button3);

        initializeActivity();

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (UtilitiesHelper.getUsername(getBaseContext()).isEmpty()){
                    emailLbl.setVisibility(View.GONE);
                    if (emailFld.getText().toString().isEmpty()) {
                        //signed in
                        emailFld.setError(("Empty! Please Enter Your emailLbl"));
                        if (!emailFld.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                            emailFld.setError((" Please Enter Your email correctly formatted"));
                        }
                    } else {

                        resetPassword(emailFld.getText().toString());
                        Intent i = new Intent(ResetPassword.this,LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                }else{
                    //signed in
                    emailFld.setVisibility(View.GONE);
                    emailLbl.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    resetPassword(emailLbl.getText().toString());
                    UtilitiesHelper.logout();
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
                            Log.d("Reset Password", "emailLbl sent.");
                            UtilitiesHelper.showToast(getBaseContext(), "Please check your email");
                        }else{
                            Log.d("Reset Password", task.getException().getMessage());
                            UtilitiesHelper.showToast(getBaseContext(), task.getException().getMessage());
                        }
                    }
                });
    }

    public void initializeActivity(){
        if (UtilitiesHelper.getUsername(getBaseContext()).isEmpty()){
            emailLbl.setVisibility(View.GONE);
        }else{
            //signed in
            emailFld.setVisibility(View.GONE);
            emailLbl.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }
    }

}
