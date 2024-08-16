package com.example.safespace;

import android.Manifest;
import androidx.core.content.FileProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.provider.MediaStore;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RescueActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSIONS = 123;
    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.SEND_SMS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String audioFilePath;
    private String videoFilePath;
    private Button rescueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main_menu);

        Button rescueButton = findViewById(R.id.rescue_button);
        rescueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RescueActivity.this, "Rescue Intitiated", Toast.LENGTH_SHORT).show();

                if (checkPermissions()) {
                    startRescueProcess();
                } else {
                    requestPermissions();
                }
            }
        });
    }

    private void startRescueProcess() {
        startAudioRecording();
        startVideoRecording();
        playRingtone();
        shareLocation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopAudioRecording();
                stopVideoRecording();
                stopRingtone();
            }
        }, 20000); // 20 seconds
    }

    private void startAudioRecording() {
        audioFilePath = getAudioFilePath();
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(audioFilePath);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopAudioRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
            } catch (IllegalStateException e) {
                // Handle the case where the recording was stopped prematurely
            } finally {
                mediaRecorder.release();
                mediaRecorder = null;
            }
        }
    }

    private void startVideoRecording() {
        videoFilePath = getVideoFilePath();
        File videoFile = new File(videoFilePath);
        Uri videoUri = FileProvider.getUriForFile(this, "com.example.safespace.fileprovider", videoFile);

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        startActivityForResult(intent, 1);
    }

    private void stopVideoRecording() {
        // Stop the video recording
        stopMediaRecorder();

        // Process the recorded video file
        processVideoFile();
    }

    private void stopMediaRecorder() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
            } catch (IllegalStateException e) {
                // Handle the case where the recording was stopped prematurely
            } finally {
                mediaRecorder.release();
                mediaRecorder = null;
            }
        }
    }

    private void processVideoFile() {
        // Add your logic to process the recorded video file
        // This could include compressing the file, uploading it to a server, or any other desired operations
    }

    private void playRingtone() {
        mediaPlayer = MediaPlayer.create(this, R.raw.safespace);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    private void stopRingtone() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void sendEvidenceToWhatsApp(String phoneNumber, String message, Uri evidenceUri) {
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            if (evidenceUri != null) {
                sendIntent.putExtra(Intent.EXTRA_STREAM, evidenceUri);
            }
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp"); // Specify WhatsApp package

            // Check if WhatsApp is installed on the device
            if (sendIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(sendIntent);
            } else {
                Toast.makeText(RescueActivity.this, "WhatsApp is not installed on this device", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(RescueActivity.this, "Failed to send emergency message via WhatsApp", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private boolean checkPermissions() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (checkPermissions()) {
                startRescueProcess();
            } else {
                Toast.makeText(this, "Permissions are required to use the rescue feature.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getAudioFilePath() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "AUDIO_" + timeStamp + ".mp4";
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName;
    }

    private String getVideoFilePath() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "VIDEO_" + timeStamp + ".mp4";
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName;
    }

    private void shareLocation() {
        // Get the user's location
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                // Share the location with the emergency contacts via WhatsApp
                String locationLink = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
                sendEvidenceToWhatsApp("+263780932494", locationLink, null);
                sendEvidenceToWhatsApp("+263787157437", locationLink, null);
            } else {
                Toast.makeText(this, "Unable to retrieve location information.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Location permission is required to share your location.", Toast.LENGTH_SHORT).show();
        }
    }
}