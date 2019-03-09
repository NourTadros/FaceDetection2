package com.example.katy.facedetention;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.katy.facedetention.Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.rimoto.intlphoneinput.IntlPhoneInput;

public class RegisterationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText fnameFld, lnameFld, passwordFld, confirmPasswrodFld, emailFld;
    IntlPhoneInput mobileNumberSpinner;
    Button signupbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        mAuth = FirebaseAuth.getInstance();
        fnameFld = findViewById(R.id.SignupFnameFld);
        lnameFld = findViewById(R.id.SignupLnameFld);
        signupbtn=findViewById(R.id.SignupBtn);

        mobileNumberSpinner = findViewById(R.id.SignUpMobile);

        passwordFld = findViewById(R.id.SignupPasswordFld);
        confirmPasswrodFld = findViewById(R.id.SignupConfirmPasswordFld);

        emailFld = findViewById(R.id.SignupEmailFld);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fnameFld.getText().toString().isEmpty()) {
                    fnameFld.setError("Empty! please Enter your firstName");
                } else if (lnameFld.getText().toString().isEmpty()) {
                    lnameFld.setError("Empty! please enter your LastName");
                } else if (emailFld.getText().toString().isEmpty()) {
                    emailFld.setError("Empty! Please Enter Your Email");
                } else if (passwordFld.getText().toString().isEmpty()) {
                    passwordFld.setError("Empty! please Enter your password");
                } else if (passwordFld.getText().toString().length() < 8) {
                    passwordFld.setError("Password must be more then 8 characters");
                } else if (confirmPasswrodFld.getText().toString().isEmpty()) {
                    confirmPasswrodFld.setError("Empty! please confirm your password");
                } else if (!confirmPasswrodFld.getText().toString().equals(passwordFld.getText().toString())) {
                    confirmPasswrodFld.setError("Empty! passwords doesn't match");
                 } else {
                    if (UtilitiesHelper.isInternetConnected(getBaseContext())) {
                        signUp();
                    } else {

                        UtilitiesHelper.showToast(getBaseContext(), "Please check your internet connection and try again");
                    }
                }
            }
        });




    }


    public void addDataTodb(String userID) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(UtilitiesHelper.User_TABLE_NAME);

        String driverID = mDatabase.push().getKey();


        UserModel user = new UserModel(fnameFld.getText().toString(),
                lnameFld.getText().toString(),
                emailFld.getText().toString(),
                mobileNumberSpinner.getNumber(),

                userID
        );

        if (driverID != null) {
            mDatabase.child(userID).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterationActivity.this);
                        builder.setMessage("User Created Successfully, you will be returned to the login activity")
                                .setCancelable(false)
                                .setTitle("Congratulations")
                                .setPositiveButton("ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // finish the current activity
                                                // AlertBoxAdvance.this.finish();
                                                Intent i = new Intent(RegisterationActivity.this, MainActivity.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }else {
                        UtilitiesHelper.showToast(getBaseContext(), "Error occured, " + task.getException());
                    }
                }
            });
        }



    }

    public void signUp() {
        mAuth.createUserWithEmailAndPassword(emailFld.getText().toString(), passwordFld.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Signup", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            addDataTodb(user.getUid());


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signup", "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(SignUpRiderForm.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            if (!task.isSuccessful()) {
                                FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                Toast.makeText(RegisterationActivity.this, "Failed Registration: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                message.hide();
                                Log.i("Signup fail", e.getMessage());
                                return;
                            }
                        }
                    }
                });
    }
}