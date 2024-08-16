package com.example.safespace;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageButton drawerToggle;
    private NavigationView navigationView;
    private Button fireButton;
    private Button medicalButton;
    private Button policeButton;
    private Button rescuebutton;

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int SMS_PERMISSION_REQUEST_CODE = 1001;
    private static final int CAMERA_AUDIO_PERMISSION_REQUEST_CODE = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private MediaRecorder mediaRecorder;
    private ArrayList<DataSet> dataSetList;
    private Gson gson;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = findViewById(R.id.DrawerToggle);
        navigationView = findViewById(R.id.navigationView);

        drawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.Register) {
                    Toast.makeText(MainMenu.this, "Register Opened", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainMenu.this, RegisterActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.LocationTracking) {
                    Toast.makeText(MainMenu.this, "Location Tracking Opened", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainMenu.this, Google_Maps.class);
                    startActivity(intent);
                } else if (itemId == R.id.SecureMessaging) {
                    Toast.makeText(MainMenu.this, "SecureMessaging Opened", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainMenu.this, secureMessaging.class);
                    startActivity(intent);
                } else if (itemId == R.id.IncidentReport) {
                    Toast.makeText(MainMenu.this, "IncidentReporting Opened", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainMenu.this, incidentReport.class);
                    startActivity(intent);
                } else if (itemId == R.id.CrimeMapping) {
                    Toast.makeText(MainMenu.this, "CrimeMapping Opened", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainMenu.this, crimemapping.class);
                    startActivity(intent);
                } else if (itemId == R.id.EmergencyContact) {
                    Toast.makeText(MainMenu.this, "EmergencyContacts Opened", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainMenu.this, emergency.class);
                    startActivity(intent);
                } else if (itemId == R.id.SelfProtectionRescue) {
                    Toast.makeText(MainMenu.this, "Self Protection Opened", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainMenu.this, protection.class);
                    startActivity(intent);
                } else if (itemId == R.id.Instructions) {
                    Toast.makeText(MainMenu.this, "Instructions Opened", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainMenu.this, instructions.class);
                    startActivity(intent);
                } else if (itemId == R.id.TermsandConditions) {
                    Toast.makeText(MainMenu.this, "Terms and Conditions Opened", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainMenu.this, termsandconditions.class);
                    startActivity(intent);
                } else if (itemId == R.id.About) {
                    Toast.makeText(MainMenu.this, "About Opened", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainMenu.this, about.class);
                    startActivity(intent);
                } else if (itemId == R.id.notifications) {
                    Toast.makeText(MainMenu.this, "Notifications Opened", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainMenu.this, notifications.class);
                    startActivity(intent);
                } else if (itemId == R.id.feedback) {
                    Toast.makeText(MainMenu.this, "FeedBack Opened", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainMenu.this, feedback.class);
                    startActivity(intent);
                } else if (itemId == R.id.ContactUs) {
                    Toast.makeText(MainMenu.this, "Contact Us Opened", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainMenu.this, contactus.class);
                    startActivity(intent);
                } else if (itemId == R.id.GuardianAi) {
                    Toast.makeText(MainMenu.this, "SafetyAI Opened", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainMenu.this, chatgptMain.class);
                    startActivity(intent);

                }


                return true;
            }
        });

        fireButton = findViewById(R.id.fire_button);
        medicalButton = findViewById(R.id.Medical);
        policeButton = findViewById(R.id.PoliceButton);
        rescuebutton = findViewById(R.id.rescue_button);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        List<String> fireNumbers = new ArrayList<>();
        fireNumbers.add("+263780932494");
        fireNumbers.add("+263787157437");

        List<String> medicalNumbers = new ArrayList<>();
        medicalNumbers.add("+263780932494");
        medicalNumbers.add("++263787157437");

        List<String> policeNumbers = new ArrayList<>();
        policeNumbers.add("+263780932494");
        policeNumbers.add("++263787157437");

        medicalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmergencySMS(medicalNumbers, "Medical emergency! Come Help Please, I'm in Trouble");
            }
        });
        rescuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, RescueActivity.class);
                startActivity(intent);

            }
        });

        fireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmergencySMS(fireNumbers, "Fire emergency! Come Help Please, I'm in Trouble");
            }
        });

        policeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmergencySMS(policeNumbers, "Police emergency! Come Help Please, I'm in Trouble");
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void sendEmergencySMS(List<String> phoneNumbers, String message) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        String locationMessage = message + "\nMy Location: Latitude: " + latitude + ", Longitude: " + longitude;
                        sendSmsToNumbers(phoneNumbers, locationMessage);
                    } else {
                        sendSmsToNumbers(phoneNumbers, message);
                        Toast.makeText(MainMenu.this, "SMS sent without location details", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(MainMenu.this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSmsToNumbers(List<String> phoneNumbers, String message) {
        for (String phoneNumber : phoneNumbers) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                Toast.makeText(MainMenu.this, "SMS sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(MainMenu.this, "Failed to send SMS to " + phoneNumber, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted
                getLastKnownLocation();
            } else {
                Toast.makeText(this, "Location permission is denied, please allow the permission to access the location", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // SMS permission granted
                // You can now safely send SMS messages
            } else {
                Toast.makeText(this, "SMS permission is denied, please allow the permission to send SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getLastKnownLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        // Use the location to update the UI or send it in the SMS message
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        // Do something with the location data
                    } else {
                        // Location is not available
                        Toast.makeText(MainMenu.this, "Location not available", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // Location permission is not granted
            Toast.makeText(this, "Location permission is denied, please allow the permission to access the location", Toast.LENGTH_SHORT).show();
        }
    }
}