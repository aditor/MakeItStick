package com.flashcardai.makeitstick;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QuickPhotoActivity extends AppCompatActivity {
    Button cameraButton;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        cameraButton = findViewById(R.id.cameraButton);
        imageView = findViewById(R.id.imageView);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String timeStamp = new SimpleDateFormat( "yyyyMMdd_HHmmss").format( new Date( ));
        String output_file_name = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + timeStamp + ".jpeg";

        File pictureFile = new File(output_file_name);
        if (pictureFile.exists()) {
            pictureFile.delete();
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);

            //Bitmap realImage = BitmapFactory.decodeByteArray(data, 0, data.length);

            ExifInterface exif=new ExifInterface(pictureFile.toString());

            Log.d("EXIF value", exif.getAttribute(ExifInterface.TAG_ORIENTATION));
            if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("6")){
                //realImage= rotate(realImage, 90);
            } else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")){
                //realImage= rotate(realImage, 270);
            } else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")){
                //realImage= rotate(realImage, 180);
            } else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("0")){
                //realImage= rotate(realImage, 90);
            }

            //realImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.close();

            //imageView.setImageBitmap(realImage);

        } catch (FileNotFoundException e) {
            Log.d("Info", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("TAG", "Error accessing file: " + e.getMessage());
        }
    }

}
