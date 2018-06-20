package com.flashcardai.makeitstick;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.ConceptsOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;


public class WatsonProcess extends AppCompatActivity {

    Button btnContinue;
    TextView watsonTxt;
    NaturalLanguageUnderstanding service;
    StringBuilder sBuilder = Storage.getInstance().getVisionProcessedString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watson);
        btnContinue = findViewById(R.id.btnContinue);
        watsonTxt = findViewById(R.id.watsonTxt);

        service = new NaturalLanguageUnderstanding(
                "2018-03-16",
                "57acc354-6c80-4ecb-bbe9-6f7f12808fb5",
                "aS4wgosp7xW8"
        );

        String visionText = sBuilder.toString();

        ConceptsOptions concepts = new ConceptsOptions.Builder()
                .limit(20)
                .build();

        Features features = new Features.Builder()
                .concepts(concepts)
                .build();

        AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                .text(visionText)
                .features(features)
                .build();

        ServiceCall call = service.analyze(parameters);
        call.enqueue(new ServiceCallback<AnalysisResults>() {
            @Override
            public void onResponse(AnalysisResults response) {
                System.out.println("done");
                //watsonTxt.setText(result);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

    }

}
