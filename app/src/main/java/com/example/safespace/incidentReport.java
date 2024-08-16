package com.example.safespace;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class incidentReport extends AppCompatActivity {

    private static final int MAX_FILE_SIZE = 15 * 1024 * 1024; // 15 MB
    private static final int REQUEST_CODE_EVIDENCE = 1;

    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private Button mVoiceButton;
    private Button mVideoButton;
    private Button mPictureButton;
    private Button mDocumentButton;
    private TextView mEvidenceTextView;
    private Uri mEvidenceUri;
    private Button mSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incidentreport);

        mTitleEditText = findViewById(R.id.title_edit_text);
        mDescriptionEditText = findViewById(R.id.description_edit_text);
        mVoiceButton = findViewById(R.id.voice_button);
        mVideoButton = findViewById(R.id.video_button);
        mPictureButton = findViewById(R.id.picture_button);
        mDocumentButton = findViewById(R.id.document_button);
        mEvidenceTextView = findViewById(R.id.evidence_text_view);
        mSubmitButton = findViewById(R.id.submit_button);

        mVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectEvidence(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            }
        });

        mVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectEvidence(MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            }
        });

        mPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectEvidence(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            }
        });

        mDocumentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDocument();
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitleEditText.getText().toString();
                String description = mDescriptionEditText.getText().toString();

                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || mEvidenceUri == null) {
                    Toast.makeText(incidentReport.this, "Please fill in all fields and attach evidence", Toast.LENGTH_SHORT).show();
                    return;
                }

                long evidenceSize = mEvidenceUri.getPath().length();

                if (evidenceSize > MAX_FILE_SIZE) {
                    Toast.makeText(incidentReport.this, "Evidence file size is too large (max 15 MB)", Toast.LENGTH_SHORT).show();
                    return;
                }

                String message = "Incident Report: " + title + "\nDescription: " + description + "\nEvidence: " + mEvidenceUri;

                // Send the incident report via WhatsApp to the primary links
                sendEvidenceToWhatsApp("https://wa.link/e7aklu", message, mEvidenceUri);
                sendEvidenceToWhatsApp("https://wa.link/nu3qek", message, mEvidenceUri);

                // Send the incident report via WhatsApp to the fallback phone numbers
                sendEvidenceToWhatsApp("+263780932494", message, mEvidenceUri);
                sendEvidenceToWhatsApp("+263787157437", message, mEvidenceUri);

                Toast.makeText(incidentReport.this, "Incident report sent via WhatsApp", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectEvidence(Uri contentUri) {
        Intent intent = new Intent(Intent.ACTION_PICK, contentUri);
        startActivityForResult(intent, REQUEST_CODE_EVIDENCE);
    }

    private void selectDocument() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*"); // Set the MIME type to allow any file type
        startActivityForResult(intent, REQUEST_CODE_EVIDENCE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_EVIDENCE && resultCode == RESULT_OK && data != null) {
            mEvidenceUri = data.getData();
            mEvidenceTextView.setText(getEvidenceName(mEvidenceUri));
        }
    }

    private String getEvidenceName(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(projection[0]);
            return cursor.getString(columnIndex);
        }
        return "";
    }

    private void sendEvidenceToWhatsApp(String phoneNumber, String message, Uri evidenceUri) {
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.putExtra(Intent.EXTRA_STREAM, evidenceUri);
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp"); // Specify WhatsApp package

            // Check if WhatsApp is installed on the device
            if (sendIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(sendIntent);
            } else {
                Toast.makeText(incidentReport.this, "WhatsApp is not installed on this device", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(incidentReport.this, "Failed to send evidence via WhatsApp", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}