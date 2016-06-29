package saigatram.myfirstapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


public class MyActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    public final static String EXTRA_MESSAGE = "saigatram.myfirstapp.MESSAGE";
    private static final int MY_PERMISSIONS_REQUEST_GET_LOCATION = 1;
    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    TextView mLatitudeText,mLongitudeText;


    //The following code checks if the app has permission to read the user's contacts, and requests the permission if necessary
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        /*check if permssion is gotten
        int permissionCheck = ContextCompat.checkSelfPermission(MyActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        */

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (ContextCompat.checkSelfPermission(MyActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MyActivity.this,
                    Manifest.permission.READ_CONTACTS)) {

                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MyActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_GET_LOCATION);

                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        mLatitudeText=(TextView)findViewById(R.id.textView1);
        mLongitudeText=(TextView)findViewById(R.id.textView2);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GET_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MyActivity.this, "Access Granted :D",
                            Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(MyActivity.this, "Access Denied :(",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this,DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE,message);
        startActivity(intent);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        int permissionCheck = ContextCompat.checkSelfPermission(MyActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

