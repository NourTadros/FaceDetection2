package com.example.katy.facedetention;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
Button presshere;

    private FirebaseAuth mAuth;

static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        if (UtilitiesHelper.getUsername(getApplicationContext()).isEmpty()){
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }else {
            mAuth.signInWithEmailAndPassword(UtilitiesHelper.getUsername(getApplicationContext()), UtilitiesHelper.getPassword(getApplicationContext()))
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "",
                                        "Loading. Please wait...", true);

                                Log.d("Login", "signInWithEmail:success");

                                FirebaseUser user = mAuth.getCurrentUser();

                                Intent i = new Intent(MainActivity.this, MenuActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Login", "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


    }

//        presshere = findViewById(R.id.MainButton);
//        presshere.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(MainActivity.this,LoginActivity.class);
//                startActivity(i);
//
//            }
//        });
    }



    //}

