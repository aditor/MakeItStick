package com.flashcardai.makeitstick;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.Toast;

import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

public class CameraKitActivity extends AppCompatActivity {
    CameraView cameraView;
    boolean canTakePicture;
    private CameraKitEventListener cameraListener = new CameraKitEventListener() {
        @Override
        public void onEvent(CameraKitEvent cameraKitEvent) {
            switch (cameraKitEvent.getType()) {
                case CameraKitEvent.TYPE_CAMERA_OPEN:
                    canTakePicture = true;
                    break;

                case CameraKitEvent.TYPE_CAMERA_CLOSE:
                    canTakePicture = false;
                    break;
            }
        }

        @Override
        public void onError(CameraKitError cameraKitError) {
        }

        @Override
        public void onImage(CameraKitImage cameraKitImage) {
            byte[] picture = cameraKitImage.getJpeg(); // will return byte[]
            Bitmap result = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        }

        @Override
        public void onVideo(CameraKitVideo cameraKitVideo) {
            return;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_kit);
        cameraView = findViewById(R.id.camera);

        cameraView.addCameraKitListener(cameraListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }

}
