package com.example.safespace;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class contactus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactus);

        Button whatsappButton1 = findViewById(R.id.whatsapp_button1);
        Button whatsappButton2 = findViewById(R.id.whatsapp_button2);
        Button facebookButton = findViewById(R.id.facebook_button);
        Button telegramButton = findViewById(R.id.telegram_button);
        Button twitterButton = findViewById(R.id.twitter_button);
        Button instagramButton = findViewById(R.id.instagram_button);

        whatsappButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = "+263787157437";
                String url = "https://api.whatsapp.com/send?phone=" + phoneNumber;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        whatsappButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = "+263780932494";
                String url = "https://api.whatsapp.com/send?phone=" + phoneNumber;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String facebookUrl = "https://www.facebook.com/profile.php?id=100071142189436";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
                startActivity(intent);
            }
        });

        telegramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String telegramUrl = "https://t.me/yourchannel"; // Replace with your Telegram channel URL
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(telegramUrl));
                startActivity(intent);
            }
        });

        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String twitterUrl = "https://twitter.com/So_Journer";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl));
                startActivity(intent);
            }
        });

        instagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String instagramUrl = "https://www.instagram.com/yourhandle"; // Replace with your Instagram handle
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl));
                startActivity(intent);
            }
        });
    }
}