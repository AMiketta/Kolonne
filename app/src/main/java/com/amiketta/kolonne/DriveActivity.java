package com.amiketta.kolonne;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class DriveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);

        Intent intent = getIntent();

        String frequencyText =intent.getStringExtra("frequency");

        setContentView(R.layout.activity_drive_view);

        EditText currentFrequency = (EditText) findViewById(R.id.currentFrequency);
        currentFrequency.setText(frequencyText);

        ToggleButton toggleStopAndGo = (ToggleButton) findViewById(R.id.toggleStopAndGo);
        toggleStopAndGo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                EditText currentFrequency = (EditText) findViewById(R.id.currentFrequency);
                Firebase myFirebaseRef = new Firebase("https://luminous-heat-2096.firebaseio.com/");

                Firebase frequencyref = myFirebaseRef.child("frequencies").child(currentFrequency.getText().toString());
                frequencyref.child("go").setValue(isChecked);

                LinearLayout driveView = (LinearLayout) findViewById(R.id.driveView);


                if (isChecked) {
                    Log.d("Kolonne", "losfahren");
                    driveView.setBackgroundColor(Color.parseColor("#00ff00"));
                } else {
                    Log.d("Kolonne", "stehen beliben");
                    driveView.setBackgroundColor(Color.parseColor("#ff0000"));
                }
            }
        });

        Firebase myFirebaseRef = new Firebase("https://luminous-heat-2096.firebaseio.com/frequencies");
        myFirebaseRef.child(frequencyText).

                addValueEventListener(new ValueEventListener() {

                                          @Override
                                          public void onDataChange(DataSnapshot snapshot) {
                                              LinearLayout driveView = (LinearLayout) findViewById(R.id.driveView);
                                              KolonnenChannel frequency = snapshot.getValue(KolonnenChannel.class);

                                              ToggleButton toggleStopAndGo = (ToggleButton) findViewById(R.id.toggleStopAndGo);
                                              toggleStopAndGo.setChecked(frequency.getGo());

                                              if (frequency.getGo()) {
                                                  driveView.setBackgroundColor(Color.parseColor("#00ff00"));
                                              } else {
                                                  driveView.setBackgroundColor(Color.parseColor("#ff0000"));
                                              }

                                          }

                                          @Override
                                          public void onCancelled(FirebaseError error) {
                                              Log.i("FirebaseError", error.getMessage());
                                          }

                                      }

                );
    }

}
