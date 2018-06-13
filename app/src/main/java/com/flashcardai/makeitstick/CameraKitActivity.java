package com.flashcardai.makeitstick;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

public class CameraKitActivity extends AppCompatActivity implements View.OnClickListener{
    private CameraView cameraView;
    private Button captureButton;
    private CameraKitEventListener cameraListener = new CameraKitEventListener() {
        private boolean canTakePicture;
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
        public void onError(CameraKitError cameraKitError) {}

        @Override
        public void onImage(CameraKitImage cameraKitImage) {
            byte[] picture = cameraKitImage.getJpeg();
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
        captureButton = findViewById(R.id.btnCapture);

        captureButton.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if(v == captureButton){
            cameraView.captureImage();
        }
    }
}
