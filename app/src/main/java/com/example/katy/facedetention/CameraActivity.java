package com.example.katy.facedetention;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import static com.example.katy.facedetention.MainActivity.REQUEST_IMAGE_CAPTURE;

public class CameraActivity extends AppCompatActivity {
Button camera;
ImageButton thumbnail;
ImageView myImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        camera=findViewById(R.id.camera);
        myImageView=findViewById(R.id.imgview);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        thumbnail=findViewById(R.id.imgthumbnail);

    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inMutable=true;
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
                    if(!faceDetector.isOperational()){
                        new AlertDialog.Builder(v.getContext()).setMessage("Could not set up the face detector!").show();
                        return;
                    }
                    Frame frame = new Frame.Builder().setBitmap(imageBitmap).build();
                    SparseArray<Face> faces = faceDetector.detect(frame);
                    for(int i=0; i<faces.size(); i++) {
                        Face thisFace = faces.valueAt(i);
                        float x1 = thisFace.getPosition().x;
                        float y1 = thisFace.getPosition().y;
                        float x2 = x1 + thisFace.getWidth();
                        float y2 = y1 + thisFace.getHeight();
                        tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myRectPaint);
                    }
                    myImageView.setImageDrawable(new BitmapDrawable(getResources(),tempBitmap));
                    camera.setAlpha(0);
                }
            });
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            thumbnail.setImageBitmap(imageBitmap);
        }
    }
}
