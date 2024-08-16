package com.example.safespace;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class about extends Activity {
    private static final String TAG = "About";
    private int clickCount = 0;
    private TextView textViewDescription;
    private Handler handler = new Handler();
    private static final long DELAY_MILLISECONDS = 10000; // 10 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        TextView textViewTitle = findViewById(R.id.textViewTitle);
        textViewDescription = findViewById(R.id.textViewDescription);

        // Set the text for the title and description
        textViewTitle.setText("About Us");
        textViewDescription.setText("The Safe Space application was developed and designed by Farai Peter Junior Mushipe, following the concept proposed by Sandisiwe Nkomazana. The app aims to enhance personal safety and community security by providing immediate assistance in dangerous situations and facilitating swift action by law enforcement authorities.\n" +
                "                Key features of Safe Space include a panic button for immediate distress alerts, location tracking for accurate response coordination, secure messaging with security personnel, incident reporting tools, crime mapping functionalities, and resources for self-protection and crime prevention. The app also includes a color-coded emergency ranking system and all Zimbabwean emergency response team numbers.\n" +
                "        Safe Space is expected to revolutionize crime reporting and emergency response mechanisms, fostering a safer and more vigilant community in Zimbabwe. The app is slated for launch within months following approval, with a focus on user testing, security protocols, and collaboration with law enforcement agencies.\n" +
                "                The project budget encompasses development costs, security infrastructure investments, and outreach efforts to promote app adoption among community members. Partnerships with local authorities, security firms, and safety organizations will be pivotal in ensuring the effectiveness and reach of Safe Space.\n" +
                "        The expected impact of Safe Space includes improved response times to distress signals, increased crime resolution rates, positive user testimonials on safety improvements, and partnerships with law enforcement agencies. A sustainability plan involves revenue generation through premium security features, community engagement initiatives, and potential collaborations with public safety agencies.\n" +
                "        Safe Space embodies the spirit of innovation and social impact by harnessing technology to enhance personal safety and community security, contributing to a safer future for all..");

        // Add click listener to the text view
        textViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount++;
                if (clickCount == 3) {
                    textViewDescription.setText("This mobile application was designed and developed by Farai Peter Junior Mushipe.");
                    // Post a delayed action to reset the text after 10 seconds
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textViewDescription.setText("The Safe Space application was developed and designed by Farai Peter Junior Mushipe, following the concept proposed by Sandisiwe Nkomazana. The app aims to enhance personal safety and community security by providing immediate assistance in dangerous situations and facilitating swift action by law enforcement authorities.\n" +
                                    "                Key features of Safe Space include a panic button for immediate distress alerts, location tracking for accurate response coordination, secure messaging with security personnel, incident reporting tools, crime mapping functionalities, and resources for self-protection and crime prevention. The app also includes a color-coded emergency ranking system and all Zimbabwean emergency response team numbers.\n" +
                                    "        Safe Space is expected to revolutionize crime reporting and emergency response mechanisms, fostering a safer and more vigilant community in Zimbabwe. The app is slated for launch within months following approval, with a focus on user testing, security protocols, and collaboration with law enforcement agencies.\n" +
                                    "                The project budget encompasses development costs, security infrastructure investments, and outreach efforts to promote app adoption among community members. Partnerships with local authorities, security firms, and safety organizations will be pivotal in ensuring the effectiveness and reach of Safe Space.\n" +
                                    "        The expected impact of Safe Space includes improved response times to distress signals, increased crime resolution rates, positive user testimonials on safety improvements, and partnerships with law enforcement agencies. A sustainability plan involves revenue generation through premium security features, community engagement initiatives, and potential collaborations with public safety agencies.\n" +
                                    "        Safe Space embodies the spirit of innovation and social impact by harnessing technology to enhance personal safety and community security, contributing to a safer future for all..");
                        }
                    }, DELAY_MILLISECONDS);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove any pending delayed actions when the activity is destroyed
        handler.removeCallbacksAndMessages(null);
    }
}