package com.example.katy.facedetention;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import com.example.katy.facedetention.Model.PictureModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static com.example.katy.facedetention.MainActivity.REQUEST_IMAGE_CAPTURE;

public class CameraActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    Button camera;
    ImageButton thumbnail;
    ImageView myImageView,arrow;
    TextView checkSelfie;
    String currentPhotoPath;

    private String deviceIdentifier;

    private FirebaseStorage firebaseStorage;


    public StorageReference mStorageRef;
    public Uri filePath;
    StorageReference storageReference;
    public FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        arrow=findViewById(R.id.arrow);
        checkSelfie=findViewById(R.id.presshereselfie);
        arrow.setVisibility(View.GONE);
        checkSelfie.setVisibility(View.GONE);


        firebaseStorage = FirebaseStorage.getInstance();
        camera = findViewById(R.id.camera);
        myImageView = findViewById(R.id.imgview);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    dispatchTakePictureIntent();
                }
            }
        });
        thumbnail = findViewById(R.id.imgthumbnail);

    }
//    private void galleryAddPic() {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(currentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }

//
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);


        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra( MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            File pictureFile = null;
            try {
                pictureFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this,
                        "Photo file can't be created, please try again",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (pictureFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        pictureFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
            }
        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new  File(currentPhotoPath);
            if(imgFile.exists())            {
                myImageView.setImageURI(Uri.fromFile(imgFile));
            }
        }
        camera.setAlpha(0);
        arrow.setVisibility(View.VISIBLE);
        checkSelfie.setVisibility(View.VISIBLE);
        myImageView.setVisibility(View.GONE);



            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    faceDetection(data, v);



                }
            });

        }


    public void faceDetection(final Intent data, View v) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        thumbnail.setImageBitmap(imageBitmap);
        Paint myRectPaint = new Paint();
        myRectPaint.setStrokeWidth(5);
        myRectPaint.setColor(Color.RED);
        myRectPaint.setStyle(Paint.Style.STROKE);
        Bitmap tempBitmap = Bitmap.createBitmap(imageBitmap.getWidth(), imageBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(imageBitmap, 0, 0, null);
        FaceDetector faceDetector = new
                FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false)
                .build();

        if (!faceDetector.isOperational()) {
            new AlertDialog.Builder(v.getContext()).setMessage("Could not set up the face detector!").show();
            return;
        }

        Frame frame = new Frame.Builder().setBitmap(imageBitmap).build();
        SparseArray<Face> faces = faceDetector.detect(frame);
        if(faces.size()==0){
            showMessage("Oops","Your facial features are unclear\n Please press on the picture to try again");

        }

        for (int i = 0; i < faces.size(); i++) {
            Log.i("face_size", String.valueOf(faces.size()));

            Face thisFace = faces.valueAt(i);
            float x1 = thisFace.getPosition().x;
            float y1 = thisFace.getPosition().y;
            float x2 = x1 + thisFace.getWidth();
            float y2 = y1 + thisFace.getHeight();
            tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myRectPaint);
            //showMessage("Wohoo", "Your facial features are clear\n press ok to check it out");
            addToCloudStorage();
        }
        myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
        camera.setAlpha(0);
        arrow.setVisibility(View.GONE);
        checkSelfie.setVisibility(View.GONE);
    }






    public void addDataTodb( String photoURL,String UserID) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(UtilitiesHelper.Picture_TABLE_NAME);

        String pictureID = mDatabase.push().getKey();



        PictureModel pictureModel = new PictureModel(
                photoURL,
                UserID,
                pictureID

        );

        if (pictureID != null) {
            mDatabase.child(pictureID).setValue(pictureModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                        AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
                        builder.setMessage("Your facial features are clear.\nPicture Added Successfully, you will be returned to the Main activity")
                                .setCancelable(false)
                                .setTitle("Congratulations")
                                .setPositiveButton("ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // finish the current activity
                                                // AlertBoxAdvance.this.finish();
                                                Intent i = new Intent(CameraActivity.this, MenuActivity.class);
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
    public void showMessage(String title,String Message){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
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

    private void addToCloudStorage() {
        File f = new File(currentPhotoPath);
        Uri picUri = Uri.fromFile(f);
        final String cloudFilePath = picUri.getLastPathSegment();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference uploadeRef = storageRef.child(cloudFilePath);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        uploadeRef.putFile(picUri).addOnFailureListener(new OnFailureListener(){
            public void onFailure(@NonNull Exception exception){
                Log.e("CaptureImage","Failed to upload picture to cloud storage");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                Toast.makeText(CameraActivity.this,
                        "Image has been uploaded to cloud storage",
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                String UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                addDataTodb(currentPhotoPath,UserID);
            }

        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

}
