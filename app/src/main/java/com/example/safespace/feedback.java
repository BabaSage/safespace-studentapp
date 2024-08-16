package com.example.safespace;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class feedback extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 100;
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        EditText nameEditText = findViewById(R.id.name);
        EditText emailEditText = findViewById(R.id.email);
        EditText feedbackEditText = findViewById(R.id.feedback);
        Button sendFeedbackButton = findViewById(R.id.send_feedback);

        sendFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String feedback = feedbackEditText.getText().toString();

                if (ActivityCompat.checkSelfPermission(feedback.this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(feedback.this,
                            new String[]{Manifest.permission.SEND_SMS},
                            SMS_PERMISSION_REQUEST_CODE);
                    return;
                }

                if (!email.matches(EMAIL_REGEX)) {
                    Toast.makeText(feedback.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("+263780932494", null, "Name: " + name + "\nEmail: " + email + "\nFeedback: " + feedback, null, null);
                Toast.makeText(feedback.this, "Feedback sent successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().matches(EMAIL_REGEX)) {
                    emailEditText.setError(null);
                } else {
                    emailEditText.setError("Please enter a valid email address");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == SMS_PERMISSION_REQUEST_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "SMS permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}

