package com.realtime.ehabhamdy.locationtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;

import static com.basgeekball.awesomevalidation.ValidationStyle.UNDERLABEL;

public class MainActivity extends AppCompatActivity {

    public static final String TRACKING_NUMBER = "tNumber";

    AwesomeValidation mTrackingValidation;
    AwesomeValidation mTrackedValidation;

    private EditText mTrackingNumberEditText;
    private Button mStartTrackingButton;
    private EditText mTrackedPersonNumberEditText;
    private String trackingNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTrackingValidation = new AwesomeValidation(UNDERLABEL);
        mTrackingValidation.setContext(this);


        mTrackedValidation = new AwesomeValidation(UNDERLABEL);
        mTrackedValidation.setContext(this);


        mTrackingValidation.addValidation(this, R.id.trackin_number_ed, "^(?=\\s*\\S).*$", R.string.err_trackingNumber);

        mTrackedValidation.addValidation(this, R.id.tracked_num_ed, "^(?=\\s*\\S).*$", R.string.err_trackedNumber);



        mTrackingNumberEditText = (EditText) findViewById(R.id.trackin_number_ed);

        mStartTrackingButton = (Button) findViewById(R.id.track_btn);

        mStartTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTrackingValidation.validate();
                if(!mTrackingNumberEditText.getText().toString().trim().isEmpty()) {
                    trackingNumber = mTrackingNumberEditText.getText().toString();
                    Intent intent = new Intent(MainActivity.this, TrackingActivity.class);
                    intent.putExtra("tNumber", trackingNumber);
                    startActivity(intent);
                }
            }
        });

        mTrackedPersonNumberEditText = (EditText) findViewById(R.id.tracked_num_ed);

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTrackedValidation.validate();
                if(!mTrackedPersonNumberEditText.getText().toString().trim().isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra(TRACKING_NUMBER, mTrackedPersonNumberEditText.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

}
