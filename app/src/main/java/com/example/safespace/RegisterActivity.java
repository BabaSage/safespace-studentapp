package com.example.safespace;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;


public class RegisterActivity extends AppCompatActivity {
    EditText phoneNumberEditText, emailEditText, placeOfResidenceEditText;
    Button saveButton, retrieveButton, deleteButton, viewRegisteredButton;
    SharedPreferences sharedPreferences;
    Spinner departmentSpinner;
    String[] departments = {"Fire", "Medical", "Police"};
    ArrayList<DataSet> dataSetList;
    Gson gson;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views and variables
        phoneNumberEditText = findViewById(R.id.phone_number_edittext);
        emailEditText = findViewById(R.id.email_edittext);
        placeOfResidenceEditText = findViewById(R.id.place_of_residence_edittext);
        saveButton = findViewById(R.id.save_button);
        retrieveButton = findViewById(R.id.retrieve_button);
        deleteButton = findViewById(R.id.delete_button);
      /*  viewRegisteredButton = findViewById(R.id.view_registered_button);*/
        departmentSpinner = findViewById(R.id.departmentSpinner);
        gson = new Gson();

        // Initialize SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Retrieve dataSetList from SharedPreferences
        String dataSetListJson = sharedPreferences.getString("dataSetList", "");
        Type type = new TypeToken<ArrayList<DataSet>>() {}.getType();
        dataSetList = gson.fromJson(dataSetListJson, type);

        if (dataSetList == null) {
            dataSetList = new ArrayList<>();
        }

        // Save data to SharedPreferences
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String phoneNumber = phoneNumberEditText.getText().toString();
                    String email = emailEditText.getText().toString();
                    String placeOfResidence = placeOfResidenceEditText.getText().toString();

                    if (!isValidPhoneNumber(phoneNumber)) {
                        throw new IllegalArgumentException("Invalid phone number format");
                    }

                    if (!isValidEmail(email)) {
                        throw new IllegalArgumentException("Invalid email address");
                    }

                    // Get the selected department from the Spinner
                    String selectedDepartment = (String) departmentSpinner.getSelectedItem();

                    // Create a new DataSet object
                    DataSet dataSet = new DataSet(phoneNumber, email, placeOfResidence, selectedDepartment);

                    // Add the new DataSet object to the list
                    dataSetList.add(dataSet);

                    // Convert the dataSetList to JSON string
                    String dataSetListJson = gson.toJson(dataSetList);

                    // Save the JSON string to SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("dataSetList", dataSetListJson);
                    editor.apply();

                    Toast.makeText(RegisterActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Retrieve and display data from SharedPreferences
        retrieveButton.setOnClickListener(new View.OnClickListener() {
            int currentIndex = 0;
            @Override
            public void onClick(View v) {
                if (dataSetList.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (currentIndex < dataSetList.size()) {
                    DataSet currentDataSet = dataSetList.get(currentIndex);

                    // Display the current data including department
                    phoneNumberEditText.setText(currentDataSet.getPhoneNumber());
                    emailEditText.setText(currentDataSet.getEmail());
                    placeOfResidenceEditText.setText(currentDataSet.getPlaceOfResidence());

                    // Set the selected department in the Spinner
                    int departmentIndex = Arrays.asList(departments).indexOf(currentDataSet.getDepartment());
                    departmentSpinner.setSelection(departmentIndex);

                    Toast.makeText(RegisterActivity.this, "Data retrieved successfully", Toast.LENGTH_SHORT).show();

                    currentIndex++;

                    // Implement logic to retrieve and display data as needed
                } else {
                    Toast.makeText(RegisterActivity.this, "End of saved data", Toast.LENGTH_SHORT).show();
                    currentIndex = 0; // Reset index for next cycle
                }
            }
        });

        // Delete data from SharedPreferences
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSetList.clear();

                // Convert the dataSetList to JSON string
                String dataSetListJson = gson.toJson(dataSetList);

                // Save the JSON string to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("dataSetList", dataSetListJson);
                editor.apply();

                Toast.makeText(RegisterActivity.this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up Spinner with departments
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departments);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(adapter);
/*
        // View registered data
        viewRegisteredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, Registered.class);
                startActivity(intent);
                Toast.makeText(RegisterActivity.this, "Viewing Saved Data", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    // Implement email validation logic
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    // Implement phone number validation logic
    private boolean isValidPhoneNumber(String phoneNumber) {
        String phonePattern = "\\+263[1-9]\\d{8}";
        return phoneNumber.matches(phonePattern);
    }
}