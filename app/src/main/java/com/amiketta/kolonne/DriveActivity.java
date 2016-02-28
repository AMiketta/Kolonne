package com.amiketta.kolonne;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.facebook.AccessToken;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class DriveActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    /* A reference to the Firebase */
    private Firebase mFirebaseRef;

    /* Data from the authenticated user */
    private AuthData mAuthData;

    /* Listener for Firebase session changes */
    private Firebase.AuthStateListener mAuthStateListener;


    /* *************************************
     *              FACEBOOK               *
     ***************************************/
    /* A dialog that is presented until the Firebase authentication finished. */
    private ProgressDialog mAuthProgressDialog;


    private LocationManager locationManager;
    private Firebase myFirebaseRef;
    private TextView editLocation;

    private double _longitude;
    private double _latitude;

    private String frequencyText;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);

          /* Create the Firebase ref that is used for all authentication with Firebase */
        mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));

        myFirebaseRef = new Firebase("https://luminous-heat-2096.firebaseio.com/");
        Intent intent = getIntent();

        frequencyText = intent.getStringExtra("frequency");

         /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading");
        mAuthProgressDialog.setMessage("Authenticating ...");
        mAuthProgressDialog.setCancelable(false);
        mAuthProgressDialog.show();

        mAuthStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                mAuthProgressDialog.hide();
                setAuthenticatedUser(authData);
            }
        };
        /* Check if the user is authenticated with Firebase already. If this is the case we can set the authenticated
         * user*/
        mFirebaseRef.addAuthStateListener(mAuthStateListener);


        EditText currentFrequency = (EditText) findViewById(R.id.currentFrequency);
        currentFrequency.setText(frequencyText);

        _longitude = 0;
        _latitude = 0;
        PackageManager pm = getPackageManager();
        int hasPerm = pm.checkPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                getPackageName());
        if (hasPerm == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager)
                    getSystemService(LOCATION_SERVICE);

            LocationListener locationListener = new MyLocationListener();
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 500, 1, locationListener);
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null) {
                _latitude = loc.getLatitude();
                _longitude = loc.getLongitude();
            }
        }

        ToggleButton toggleStopAndGo = (ToggleButton) findViewById(R.id.toggleStopAndGo);
        toggleStopAndGo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                EditText currentFrequency = (EditText) findViewById(R.id.currentFrequency);

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


    /**
     * Utility class for authentication results
     */
    private class AuthResultHandler implements Firebase.AuthResultHandler {

        private final String provider;

        public AuthResultHandler(String provider) {
            this.provider = provider;
        }

        @Override
        public void onAuthenticated(AuthData authData) {
            mAuthProgressDialog.hide();
            Log.i(TAG, provider + " auth successful");
            setAuthenticatedUser(authData);
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            mAuthProgressDialog.hide();

        }
    }

    /* ************************************
    *             FACEBOOK               *
    **************************************
    */
    private void onFacebookAccessTokenChange(AccessToken token) {
        if (token != null) {
            mAuthProgressDialog.show();
            mFirebaseRef.authWithOAuthToken("facebook", token.getToken(), new AuthResultHandler("facebook"));
        } else {
            // Logged out of Facebook and currently authenticated with Firebase using Facebook, so do a logout
            if (this.mAuthData != null && this.mAuthData.getProvider().equals("facebook")) {
                mFirebaseRef.unauth();
                setAuthenticatedUser(null);
            }
        }
    }

    /**
     * Once a user is logged in, take the mAuthData provided from Firebase and "use" it.
     */
    private void setAuthenticatedUser(AuthData authData) {
        if (authData != null) {

            // mGoogleLoginButton.setVisibility(View.GONE);


            /* show a provider specific status text */
            String name = null;
            if (authData.getProvider().equals("facebook")) {
                name = (String) authData.getProviderData().get("displayName");
            } else if (authData.getProvider().equals("anonymous")
                    || authData.getProvider().equals("password")) {
                name = authData.getUid();
            } else {
                Log.e(TAG, "Invalid provider: " + authData.getProvider());
            }
            if (name != null) {
                username = name;
            }
        }
        this.mAuthData = authData;
        /* invalidate options menu to hide/show the logout button */
        supportInvalidateOptionsMenu();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            ChannelMember member = new ChannelMember(username, loc.getLatitude(), loc.getLongitude());

            Firebase members = myFirebaseRef.child("members").child(frequencyText).child(username);
            members.setValue(member);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
}
