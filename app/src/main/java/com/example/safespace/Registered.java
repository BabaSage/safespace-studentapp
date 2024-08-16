package com.example.safespace;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.safespace.DataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Registered extends AppCompatActivity {
    private TextView phoneNumberTextView;
    private TextView emailTextView;
    private TextView placeOfResidenceTextView;
    private TextView departmentTextView;
    private List<DataSet> dataSetList;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);

        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        emailTextView = findViewById(R.id.emailTextView);
        placeOfResidenceTextView = findViewById(R.id.placeOfResidenceTextView);
        departmentTextView = findViewById(R.id.departmentTextView);

        // Retrieve the serialized list from SharedPreferences
        String serializedList = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                .getString("dataSetList", null);

        if (serializedList != null) {
            // Deserialize the list from JSON
            Type listType = new TypeToken<ArrayList<DataSet>>(){}.getType();
            dataSetList = new Gson().fromJson(serializedList, listType);

            if (dataSetList != null && !dataSetList.isEmpty()) {
                currentIndex = 0;
                displayDataSet(currentIndex);
            } else {
                showNoDataMessage();
            }
        } else {
            showNoDataMessage();
        }
    }

    public void onNextButtonClick(View view) {
        if (dataSetList != null && !dataSetList.isEmpty()) {
            currentIndex = (currentIndex + 1) % dataSetList.size();
            displayDataSet(currentIndex);
        }
    }

    private void displayDataSet(int index) {
        DataSet dataSet = dataSetList.get(index);
        phoneNumberTextView.setText(dataSet.getPhoneNumber());
        emailTextView.setText(dataSet.getEmail());
        placeOfResidenceTextView.setText(dataSet.getPlaceOfResidence());
        departmentTextView.setText(dataSet.getDepartment());
    }

    private void showNoDataMessage() {
        phoneNumberTextView.setText("No data available.");
        emailTextView.setText("");
        placeOfResidenceTextView.setText("");
        departmentTextView.setText("");
    }
}