package com.example.katy.facedetention;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class WelcomeScreen extends AppCompatActivity {
    public final int SPLASH_DISPLAY_LENGTH = 2000;

    private FirebaseAuth mAuth;

    public static boolean isAppRunning;

    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome_screen);
        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(UtilitiesHelper.isInternetConnected(getBaseContext())) {


                    if (UtilitiesHelper.getUsername(getApplicationContext()).isEmpty()) {
                        Log.i("user", "no user data");
                        Intent i = new Intent(WelcomeScreen.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        mAuth.signInWithEmailAndPassword(UtilitiesHelper.getUsername(getApplicationContext()), UtilitiesHelper.getPassword(getApplicationContext()))
                                .addOnCompleteListener(WelcomeScreen.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information

                                            dialog = ProgressDialog.show(WelcomeScreen.this, "",
                                                    "Loading. Please wait...", true);

                                            Log.d("Login", "signInWithEmail:success");

                                            FirebaseUser user = mAuth.getCurrentUser();

                                            Intent i = new Intent(WelcomeScreen.this, MenuActivity.class);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w("Login", "signInWithEmail:failure", task.getException());
                                            Toast.makeText(WelcomeScreen.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }


                                });
                    }
                }
                else{

                    showMessage("Internet Connectivity","Please check your internet connection and try again");
                    //UtilitiesHelper.showToast(getBaseContext(), "Please check your internet connection and try again");
                }


            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        isAppRunning = false;
        //dialog.dismiss();
    }
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}

