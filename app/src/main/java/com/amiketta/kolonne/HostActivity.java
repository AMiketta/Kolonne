package com.amiketta.kolonne;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;

public class HostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        Intent intent = getIntent();
        //String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);

        EditText frequency = (EditText) findViewById(R.id.frequencyText);
        frequency.setText("00000");

        Button hostButton = (Button) findViewById(R.id.HostCannelbutton);
        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText frequency = (EditText) findViewById(R.id.frequencyText);

                KolonnenChannel newchannel = new KolonnenChannel("unknown", "Frequenz");

                Firebase myFirebaseRef = new Firebase("https://luminous-heat-2096.firebaseio.com/");
                Firebase frequencyref = myFirebaseRef.child("frequencies").child(frequency.getText().toString());
                frequencyref.setValue(newchannel);

                String frequencyText = frequency.getText().toString();

                Intent driveintent = new Intent(HostActivity.this, DriveActivity.class);
                driveintent.putExtra("frequency",frequencyText);
                HostActivity.this.startActivity(driveintent);
                // SetDriveView(frequencyText);
            }

        });
    }

}
