package com.example.katy.facedetention;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.example.katy.facedetention.Model.PictureModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {
    DatabaseReference dbRef;
    FirebaseDatabase firebaseDb;
    String userID;
    public PictureModel pictureModel;
    public ArrayList<PictureModel> picturesModelArrayList;
    RecyclerView recyclerView;
    public AlertDialog.Builder builder;
    AlertDialog alert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        recyclerView = findViewById(R.id.imageGallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        firebaseDb = FirebaseDatabase.getInstance();

        //fetchPictures();
        fetchPictureCorrespondingToUser();
    }
    private ArrayList<PictureModel> prepareData(){

        ArrayList<PictureModel> theimage = new ArrayList<>();
        for(int i = 0; i< picturesModelArrayList.size(); i++){
            PictureModel createList = new PictureModel();
            createList.setPictureID(String.valueOf(i));
            createList.setPhotoURL(picturesModelArrayList.get(i).getPhotoURL());
            theimage.add(createList);
        }
        return theimage;
    }

    private void fetchPictureCorrespondingToUser(){
        dbRef = firebaseDb.getReference().child(UtilitiesHelper.Picture_TABLE_NAME);
        Query userQuery = dbRef.orderByChild("userID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

        picturesModelArrayList = new ArrayList<>();

        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("child count", String.valueOf(dataSnapshot.getChildrenCount()));

                if (dataSnapshot.getChildrenCount() < 1){
                    builder = new AlertDialog.Builder(getBaseContext());
                    builder.setMessage("Sorry you have no Pictures in your Gallery")
                            .setCancelable(false)
                            .setTitle("Sorry")
                            .setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            Intent i = new Intent(getBaseContext(), MenuActivity.class);
                                            startActivity(i);
                                        }
                                    });
                    alert = builder.create();
                    alert.show();

                } else {
                    if (dataSnapshot.exists()) {

                        Log.i("picture count", String.valueOf(dataSnapshot.getChildrenCount()));

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            PictureModel pictureModel = snapshot.getValue(PictureModel.class);

                            picturesModelArrayList.add(pictureModel);

                            Log.i("user ids", pictureModel.getUserID());

                            ArrayList<PictureModel> createLists = prepareData();
                            MyAdapter adapter = new MyAdapter(getApplicationContext(), createLists);
                            recyclerView.setAdapter(adapter);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*private void fetchPictures(){
        dbRef = firebaseDb.getReference().child(UtilitiesHelper.Picture_TABLE_NAME);

        picturesModelArrayList = new ArrayList<>();
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()){
                        pictureModel = dataSnapshot.getValue(PictureModel.class);
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            //etc
                            picturesModelArrayList.add(
                                    new PictureModel(
                                            ds.child("photoURL").getValue().toString(),
                                            ds.child("userID").getValue().toString(),
                                            ds.child("pictureID").getValue().toString()
                                    )
                            );
                        }
                        //initializePictures();

                        ArrayList<PictureModel> createLists = prepareData();
                        MyAdapter adapter = new MyAdapter(getApplicationContext(), createLists);
                        recyclerView.setAdapter(adapter);

                    }else {
                        Log.i("Picture details", "no picture found");
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.i("isPicFromDb", "Failed to read value." + error.toException());
                }
            });
    }*/
}
