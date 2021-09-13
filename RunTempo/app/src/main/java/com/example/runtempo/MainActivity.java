package com.example.runtempo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mp = new MediaPlayer() ;
    private float speed = 1f ;
    private Double latitude, longitude, altitude, n_latitude, n_longitude, n_altitude, distance, desired_pace, current_pace;
    private static final Integer min_time = 1000 ;
    private EditText desired_pace_edittext ;
    private TextView song_textview ;
    private Boolean flag = false ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        TextView pace_textview = findViewById(R.id.pace_textview) ;
        desired_pace_edittext = findViewById(R.id.desired_pace_edittext) ;
        song_textview = findViewById(R.id.song_textview) ;

        /*InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Double d = Double.parseDouble(source.toString()) ;
                Log.d("mega", source.toString()) ;
                if ( d < 0.00 || d > 99.99 ) { return "" ; }
                return null;
            }
        } ;
        desired_pace.setFilters(new InputFilter[] {filter}) ;*/


        DecimalFormat df = new DecimalFormat("#.##") ;
        df.setRoundingMode(RoundingMode.FLOOR) ;

        LocationListener listener = new LocationListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onLocationChanged(@NonNull Location location) {
                n_latitude = location.getLatitude() ;
                n_longitude = location.getLongitude() ;
                n_altitude = location.getAltitude() ;
                if (latitude != null && longitude != null && altitude != null) {
                   distance = havDistance(latitude, n_latitude, longitude, n_longitude, altitude, n_altitude) ;
                   current_pace = calcPace(distance) ;
                   pace_textview.setText(String.valueOf(df.format(current_pace))) ;
                   //Log.d("mega",  "LATITUDE DIFFERENCE = " + String.valueOf(nlatitude - latitude) + "\n" + "LONGITUDE DIFFERENCE = " + String.valueOf(nlongitude - longitude) + "\n" + "ALTITUDE DIFFERENCE = " + String.valueOf(naltitude - altitude)) ;
                   //Toast.makeText(getApplicationContext(), "PACE (KM/H)  = " + String.valueOf(calcPace(distance)), Toast.LENGTH_LONG).show();
                    if (flag) {
                        desired_pace = Double.parseDouble(desired_pace_edittext.getText().toString()) ;
                        speed = (float) (current_pace/desired_pace) ;
                        Log.d("mega", String.valueOf(speed)) ;
                        if (speed > 2f) { speed = 2f ; }
                        else if (speed < 0.5f) { speed = 0.5f ; }
                        mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(speed)) ;
                    }
                }
                latitude = n_latitude ;
                longitude = n_longitude ;
                altitude = n_altitude ;
            }

            @Override                       // This override is here because I was having issues with the app crashing when I went to select a song
            public void onStatusChanged(String provider, int status, Bundle extras) {}
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, min_time, 0, listener);

    }

    public static Double havDistance(double lat1, double lat2, double long1, double long2, double alt1, double alt2) {

        final int radius = 6371 ;                               // radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1) ;
        double longDistance = Math.toRadians(long2 - long1) ;
        double a = Math.sin(latDistance/2) * Math.sin(latDistance/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(longDistance/2) * Math.sin(longDistance/2) ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)) ;
        double distance = radius * c * 1000 ;                   // convert to meters

        double height = alt2 - alt1 ;

        distance = Math.pow(distance, 2) + Math.pow(height, 2) ;

        return Math.sqrt(distance) ;
    }

    public static Double calcPace(Double distance) { return 360.0/1000.0 * distance/(min_time/1000.0) ; }

    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) ;
        startActivityForResult(intent, 10) ;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data) ;

        if(resultCode == RESULT_OK && requestCode == 10) {
            Uri uriSound = data.getData() ;
            setSong(this, uriSound) ;
        }
    }

    private void setSong(Context context, Uri uri) {

        try {
            Log.d("mega", "beeet") ;
            TextView songName = findViewById(R.id.song_textview) ;
            songName.setText(uri.getLastPathSegment());
            mp.setDataSource(context, uri);
            mp.prepare();

        }
        catch (IllegalArgumentException e) {
            Log.d("mega", e.toString()) ;
        }
        catch (SecurityException e) {
            Log.d("mega", e.toString()) ;
        }
        catch (IllegalStateException e) {
            Log.d("mega", e.toString()) ;
        }
        catch (IOException e) {
            Log.d("mega", e.toString()) ;
        }
    }

    public void pause(View view) { flag = false ; mp.pause(); }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void play(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this) ;
        builder.setMessage("Enter a desired pace and choose a song") ;

        String s1 = desired_pace_edittext.getText().toString() ;
        String s2 = song_textview.getText().toString() ;

        if (!(s1.isEmpty() || s2.isEmpty()) ) {
            //mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(speed));
            flag = true ;
            mp.start();
        }
        else {
            AlertDialog alert = builder.create() ;
            alert.show();
        }
    }

}
