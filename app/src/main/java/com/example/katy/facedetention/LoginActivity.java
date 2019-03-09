package com.example.katy.facedetention;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    TextView textstatus;

Button signuphere,login;
    LoginButton login_button;
    CallbackManager callbackManager;
private FirebaseAuth mAuth;
EditText username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
        username=findViewById(R.id.usernameLoginET);
        password=findViewById(R.id.passwordLoginET);
        login=findViewById(R.id.LoginUserBtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;

                if (username.getText().toString().isEmpty()) {
                    username.setError("Please Enter Your username");
                    valid = false;
                } else if (password.getText().toString().isEmpty()) {
                    password.setError(("Empty! Please Enter Your Password"));
                    valid = false;
                }else if ((password.length() < 8)){
                    password.setError(("Password must be more than 8 characters"));
                }
                else {
                    if (UtilitiesHelper.isInternetConnected(getBaseContext())){
                        login();
                    }else{
                        UtilitiesHelper.showToast(getBaseContext(), "Please check your internet connection and try again");
                    }

                }
            }
        });
       signuphere=findViewById(R.id.signupBtn);
       signuphere.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i=new Intent(LoginActivity.this,RegisterationActivity.class);
               startActivity(i);

           }
       });
    }
    public void login() {
        mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "",
                                    "Loading. Please wait...", true);

                            Log.d("Login", "signInWithEmail:success");


                            FirebaseUser user = mAuth.getCurrentUser();
                            UtilitiesHelper.setUsernameAndPassword(getApplicationContext(),
                                    username.getText().toString(),
                                    password.getText().toString(),
                                    mAuth.getCurrentUser().getUid()
                            );

                            Log.i("userid", mAuth.getCurrentUser().getUid());

                            Intent i = new Intent(LoginActivity.this,MenuActivity.class);
                            startActivity(i);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage("Authentication failed \n" + task.getException().getMessage().toString())
                                    .setCancelable(false)
                                    .setTitle("Oops")
                                    .setPositiveButton("ok",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // finish the current activity
                                                    // AlertBoxAdvance.this.finish();
                                                    dialog.dismiss();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
//    private void setupFacebookLogin() {
//        login_button.setReadPermissions("public_profile", "email");
//        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                if (loginResult.getAccessToken() != null) {
//                    AuthCredential authCredential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
//                    FirebaseAuth.getInstance().signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
//                                finish();
//                            } else {
//                                if (task.getException() != null)
//                                    textstatus.setText(task.getException().getMessage());
//                                else
//                                    textstatus.setText("Something went wrong");
//                            }
//                        }
//
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            textstatus.setText(e.getMessage());
//                        }
//                    });
//
//                } else {
//                    textstatus.setText("Failed to retrieve access token!");
//                }
//            }
//
//            @Override
//            public void onCancel() {
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                textstatus.setText("Login Error: " + error.getMessage());
//            }
//        });
//    }
}
