package com.example.safespace;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class secureMessaging extends AppCompatActivity {
    private EditText messageEditText;
    private Button sendButton;
    private SecretKey secretKey;
    private IvParameterSpec iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.securemessage);

        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_button);

        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            secretKey = keyGenerator.generateKey();

            SecureRandom random = new SecureRandom();
            byte[] ivBytes = new byte[16]; // Specify the size of the byte array
            random.nextBytes(ivBytes);
            iv = new IvParameterSpec(ivBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString();
                if (message.isEmpty()) {
                    Toast.makeText(secureMessaging.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                    return;
                }

                String encryptedMessage = encryptMessage(message);
                if (encryptedMessage == null) {
                    Toast.makeText(secureMessaging.this, "Failed to encrypt the message", Toast.LENGTH_SHORT).show();
                    return;
                }

                String phoneNumber1 = "+" + "263780932494";
                String phoneNumber2 = "+" + "263787157437";

                sendMessage(phoneNumber1, encryptedMessage);
                sendMessage(phoneNumber2, encryptedMessage);
            }
        });
    }

    private void sendMessage(String phoneNumber, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + phoneNumber));
        intent.putExtra("sms_body", message);
        startActivity(intent);
    }

    private String encryptMessage(String message) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] encryptedBytes = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));

            // Create a new message with the encrypted message and the encryption key
            String encryptedMessage = Base64.getEncoder().encodeToString(encryptedBytes);
            String encryptionKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append(encryptedMessage);
            messageBuilder.append(":");
            messageBuilder.append(encryptionKey);

            return messageBuilder.toString();
        } catch (Exception e) {
            Log.e("Error", "Failed to encrypt the message", e);
            return null;
        }
    }
}