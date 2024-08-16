package com.example.safespace;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class protection extends Activity {
    private ImageView imageView;
    private Button nextButton, youtubeButton;
    private int imageIndex = 0;
    private final int[] images = {R.drawable.rs0, R.drawable.rs1, R.drawable.rs2, R.drawable.rs3, R.drawable.rs4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selfprotectionrescue);

        // Display text content before images
        TextView textView1 = findViewById(R.id.text_view_1);
        TextView textView2 = findViewById(R.id.text_view_2);

        textView1.setText("Self-Defense Steps and Moves:");
        textView2.setText("Step 1: Awareness\n- Be mindful of your surroundings and potential threats.\n- Trust your instincts and avoid situations that feel unsafe.\n\nStep 2: Verbal De-escalation\n- Use a firm but calm tone to deter an attacker.\n- Say 'Back off' or 'Leave me alone' clearly and assertively.\n\nStep 3: Boundary Setting\n- Use your body to create distance and set boundaries.\n- Stand with your feet shoulder-width apart and your hands up in a defensive position.\n\nBasic Self-Defense Moves:\n1. Palm Strike: Strike an attacker's face or chest with the heel of your palm.\n2. Elbow Strike: Use your elbow to strike an attacker's face, chest, or stomach.\n3. Knee Strike: Strike an attacker's groin or stomach with your knee.\n4. Heel Kick: Kick an attacker's shin, knee, or groin with the heel of your foot.\n5. Escape: Use your hands and feet to create distance and escape the situation.\n\nAdditional Tips:\n- Use your voice to attract attention and deter an attacker.\n- Aim for vulnerable areas like the eyes, throat, and groin.\n- Use your surroundings to your advantage (e.g., use a nearby object as a weapon).\n- Practice self-defense techniques regularly to build confidence and muscle memory.\n\nRemember, self-defense is not just about physical moves – it's also about being aware, assertive, and prepared.");

        imageView = findViewById(R.id.imageView61);
        nextButton = findViewById(R.id.button41);
        youtubeButton = findViewById(R.id.button42);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageIndex = (imageIndex + 1) % images.length;
                Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                imageView.startAnimation(fadeIn);
                imageView.setImageResource(images[imageIndex]);
            }
        });

        youtubeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=self+protection"));
                startActivity(intent);
            }
        });
    }
}