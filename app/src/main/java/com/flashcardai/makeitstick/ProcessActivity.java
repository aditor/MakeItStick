package com.flashcardai.makeitstick;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.LanguageCodes;
import com.microsoft.projectoxford.vision.contract.Line;
import com.microsoft.projectoxford.vision.contract.OCR;
import com.microsoft.projectoxford.vision.contract.Region;
import com.microsoft.projectoxford.vision.contract.Word;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ProcessActivity extends AppCompatActivity {
    Button btnProcess;
    ImageView imageView;
    TextView textDescription;
    public VisionServiceClient visionServiceClient = new VisionServiceRestClient("26b4e6f2fc824cb4993944031baf3511", "https://westcentralus.api.cognitive.microsoft.com/vision/v2.0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        btnProcess = findViewById(R.id.btnProcess);
        imageView = findViewById(R.id.imageView);
        textDescription = findViewById(R.id.txtDescription);

        Bitmap mBitmap = Storage.getInstance().getBitmap();

        imageView.setImageBitmap(mBitmap);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<InputStream, String, String> recognizeTextTask = new AsyncTask<InputStream, String, String>() {
                    ProgressDialog mDialog = new ProgressDialog(ProcessActivity.this);

                    @Override
                    protected String doInBackground(InputStream... params) {
                        try {
                            publishProgress("Recognizing");
                            OCR ocr = visionServiceClient.recognizeText(params[0], LanguageCodes.English, true);
                            String result = new Gson().toJson(ocr);
                            return result;
                        } catch (Exception e) {
                            System.out.println(e);
                            return null;
                        }
                    }

                    @Override
                    protected void onPreExecute() {
                        mDialog.show();
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        mDialog.dismiss();
                        OCR ocr = new Gson().fromJson(s, OCR.class);
                        TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
                        StringBuilder stringBuilder = new StringBuilder();

                        for (Region region : ocr.regions) {
                            for (Line line : region.lines) {
                                for (Word word : line.words) {
                                    stringBuilder.append(word.text + " ");
                                    stringBuilder.append("\n");
                                }
                            }
                            stringBuilder.append("\n\n");
                        }
                        txtDescription.setText(stringBuilder);
                        Toast.makeText(ProcessActivity.this, stringBuilder, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    protected void onProgressUpdate(String... values) {
                        mDialog.setMessage(values[0]);
                    }

                };

                recognizeTextTask.execute(inputStream);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        /* !!! TODO REMOVE WHEN EVERYTHING WORKING
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            Intent loginIntent = new Intent(ProcessActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }*/
    }
}
