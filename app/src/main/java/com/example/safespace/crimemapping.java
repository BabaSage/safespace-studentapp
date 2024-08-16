package com.example.safespace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class crimemapping extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private EditText mCrimeTypeInput;
    private EditText mCrimeDateInput;
    private EditText mCrimeTimeInput;
    private EditText mCrimeDescriptionInput;
    private List<Crime> crimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crimemapping);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mCrimeTypeInput = findViewById(R.id.crime_type_input);
        mCrimeDateInput = findViewById(R.id.crime_date_input);
        mCrimeTimeInput = findViewById(R.id.crime_time_input);
        mCrimeDescriptionInput = findViewById(R.id.crime_description_input);

        Button zoomInButton = findViewById(R.id.zoom_in_button);
        Button zoomOutButton = findViewById(R.id.zoom_out_button);

        zoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });

        // Load crimes from internal storage
        crimes = loadCrimes(this);
        if (crimes == null) {
            crimes = new ArrayList<>();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);

        // Add existing crimes to the map
        for (Crime crime : crimes) {
            LatLng crimeLocation = new LatLng(crime.getLatitude(), crime.getLongitude());
            mMap.addMarker(new MarkerOptions().position(crimeLocation).title("Crime").snippet(crime.getCrimeType() + "\n" + crime.getCrimeDate() + "\n" + crime.getCrimeTime() + "\n" + crime.getCrimeDescription()));
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // Handle map click event
        Crime newCrime = new Crime(latLng.latitude, latLng.longitude, mCrimeTypeInput.getText().toString(), mCrimeDateInput.getText().toString(), mCrimeTimeInput.getText().toString(), mCrimeDescriptionInput.getText().toString());
        crimes.add(newCrime);
        saveCrimes(crimes, this);

        mMap.addMarker(new MarkerOptions().position(latLng).title("Crime").snippet(newCrime.getCrimeType() + "\n" + newCrime.getCrimeDate() + "\n" + newCrime.getCrimeTime() + "\n" + newCrime.getCrimeDescription()));

        mCrimeTypeInput.setText("");
        mCrimeDateInput.setText("");
        mCrimeTimeInput.setText("");
        mCrimeDescriptionInput.setText("");
    }

    public static void saveCrimes(List<Crime> crimes, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("crimes", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("crimes", serializeCrimes(crimes));
        editor.apply();
    }

    public static List<Crime> loadCrimes(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("crimes", Context.MODE_PRIVATE);
        String crimesJson = sharedPreferences.getString("crimes", null);
        if (crimesJson == null) {
            return null;
        }
        return deserializeCrimes(crimesJson);
    }

    public static String serializeCrimes(List<Crime> crimes) {
        StringBuilder crimesJson = new StringBuilder();
        for (int i = 0; i < crimes.size(); i++) {
            Crime crime = crimes.get(i);
            if (i != 0) {
                crimesJson.append(",");
            }
            crimesJson.append(crime.getLatitude());
            crimesJson.append(",");
            crimesJson.append(crime.getLongitude());
            crimesJson.append(",");
            crimesJson.append(crime.getCrimeType());
            crimesJson.append(",");
            crimesJson.append(crime.getCrimeDate());
            crimesJson.append(",");
            crimesJson.append(crime.getCrimeTime());
            crimesJson.append(",");
            crimesJson.append(crime.getCrimeDescription());
        }
        return crimesJson.toString();
    }

    public static List<Crime> deserializeCrimes(String crimesJson) {
        List<Crime> crimes = new ArrayList<>();
        String[] crimeData = crimesJson.split(",");
        for (int i = 0; i < crimeData.length - 1; i += 8) {
            try {
                double latitude = Double.parseDouble(crimeData[i]);
                double longitude = Double.parseDouble(crimeData[i + 1]);
                String crimeType = crimeData[i + 2];
                String crimeDate = crimeData[i + 3];
                String crimeTime = crimeData[i + 4];
                String crimeDescription = crimeData[i + 5];
                Crime crime = new Crime(latitude, longitude, crimeType, crimeDate, crimeTime, crimeDescription);
                crimes.add(crime);
            } catch (NumberFormatException e) {
                // Skip this crime if there is a parsing error
                continue;
            }
        }
        return crimes;
    }

    public static class Crime implements Serializable {
        private double latitude;
        private double longitude;
        private String crimeType;
        private String crimeDate;
        private String crimeTime;
        private String crimeDescription;

        public Crime(double latitude, double longitude, String crimeType, String crimeDate, String crimeTime, String crimeDescription) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.crimeType = crimeType;
            this.crimeDate = crimeDate;
            this.crimeTime = crimeTime;
            this.crimeDescription = crimeDescription;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public String getCrimeType() {
            return crimeType;
        }

        public String getCrimeDate() {
            return crimeDate;
        }

        public String getCrimeTime() {
            return crimeTime;
        }

        public String getCrimeDescription() {
            return crimeDescription;
        }
    }
}