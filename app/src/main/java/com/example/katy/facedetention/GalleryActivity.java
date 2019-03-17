package com.example.katy.facedetention;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.example.katy.facedetention.Model.PictureModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {
    DatabaseReference dbRef;
    FirebaseDatabase firebaseDb;
    String userID;
    public PictureModel pictureModel;
    public ArrayList<PictureModel> picturesModelArrayList;
    RecyclerView recyclerView;


    private final String image_titles[] = {
            "Img1",
            "Img2",
            "Img3",
    };
    private final Integer image_ids[] = {
            R.mipmap.img1,
            R.mipmap.img2,
            R.mipmap.img3,

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        recyclerView = findViewById(R.id.imageGallery);
        recyclerView.setHasFixedSize(true);


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        firebaseDb = FirebaseDatabase.getInstance();

        fetchPictures();
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

    public void initializePictures(){
        ArrayList<String> UserID = new ArrayList<>();

        for (int i = 0; i< picturesModelArrayList.size(); i++){
            UserID.add(picturesModelArrayList.get(i).getUserID());
        }

        Log.i("user ID", UserID.get(0) );



    }

    private void fetchPictures(){
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
                        initializePictures();

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
    }
}
