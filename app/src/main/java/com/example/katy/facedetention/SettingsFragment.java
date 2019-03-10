package com.example.katy.facedetention;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.katy.facedetention.Model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsFragment extends Fragment {

    Button changePasswordBtn;
    FirebaseDatabase database;
    private DatabaseReference myRef;
    public UserModel user;
    public String userID;
    public TextView userFName, userMobile, userEmail;
    private FirebaseAuth mAuth;


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //Log.i(driverID, "ID");

        userFName = view.findViewById(R.id.firstName);
        userEmail = view.findViewById(R.id.emailTV);
        userMobile = view.findViewById(R.id.mobileNumber);


        changePasswordBtn = view.findViewById(R.id.changePasswordBtn);

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ResetPassword.class);
                startActivity(i);
            }
        });
        getUserDetails();


        return view;
    }

    public void getUserDetails(){
        myRef = FirebaseDatabase.getInstance().getReference().child(UtilitiesHelper.User_TABLE_NAME);

        Log.i("User ID", userID);

        myRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i("child count", String.valueOf(dataSnapshot.getChildrenCount()));

                if (dataSnapshot.exists()) {

                    user = dataSnapshot.getValue(UserModel.class);

                    userFName.setText(user.getFirstName() + " " + user.getLastName());

                    userEmail.setText(user.getEmailAddress());

                    userMobile.setText(user.getMobileNumber());


                } else {
                    Log.i("User details", "no user found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("isUserFromDb", "Failed to read value." + error.toException());
            }
        });
    }




}
