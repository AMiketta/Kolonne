package com.amiketta.kolonne;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ConnectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        EditText frequency = (EditText) findViewById(R.id.ConnectFrequencyText);
        frequency.setText("00000");
        Button connectToHostButton = (Button) findViewById(R.id.ConnectToHostButton);
        connectToHostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText frequency = (EditText) findViewById(R.id.ConnectFrequencyText);
                String frequencyText = frequency.getText().toString();

                Intent driveintent = new Intent(ConnectActivity.this, DriveActivity.class);
                driveintent.putExtra("frequency",frequencyText);
                ConnectActivity.this.startActivity(driveintent);
            }

        });
    }

}
