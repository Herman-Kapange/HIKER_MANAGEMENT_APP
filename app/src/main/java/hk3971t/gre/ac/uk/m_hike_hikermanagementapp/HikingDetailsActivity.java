package hk3971t.gre.ac.uk.m_hike_hikermanagementapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import hk3971t.gre.ac.uk.m_hike_hikermanagementapp.Utils.DatabaseHelper;

public class HikingDetailsActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private Button hikeDate, nextButton;
    private RadioGroup parkingAvailable, rgpLevelOfDifficulty;
    private TextInputEditText length_of_hike, hiking_name, location, description, observation, observation_time, observation_comment, special_requirement, recommended_gear;
    private RadioButton selectd_p_available, selectd_l_difficulty;
    private TextView hiddenID;
    public static Activity endThis;
    DatabaseHelper myDb;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiking_details);
        initiateDatePicker();
//        Controls

        hikeDate = (Button) findViewById(R.id.btnHikingDate);
        hikeDate.setText(getCurrentDate());
        hiking_name = (TextInputEditText) findViewById(R.id.hikingnameTextEdit);
        location = (TextInputEditText) findViewById(R.id.hikingLocationTextEdit);
        length_of_hike = (TextInputEditText) findViewById(R.id.hikingLengthOfHikeTextEdit);
        parkingAvailable = (RadioGroup) findViewById(R.id.parking_available);
        rgpLevelOfDifficulty = (RadioGroup) findViewById(R.id.levelofDifficulty);
        description = (TextInputEditText) findViewById(R.id.txtDescriptionEditText);
        special_requirement = (TextInputEditText) findViewById(R.id.txtSpecialRequirementsEdit);
        recommended_gear = (TextInputEditText) findViewById(R.id.txtRecommendedGearEditText);
        nextButton = (Button) findViewById(R.id.btnNextbtn);
        hiddenID = (TextView) findViewById(R.id.txtAddActivityHiddenID);
        toolbar = (Toolbar) findViewById(R.id.tbrtoolbar);

        if (getIntent().getStringExtra("observation_id").equals("edit_hiking_details")) {
            // Calling Setup Activity
            configureActivity(getIntent().getStringExtra("observation_id_value"), getIntent().getStringExtra("h_date"),
                    getIntent().getStringExtra("h_location"),
                    getIntent().getStringExtra("h_name"),
                    getIntent().getStringExtra("h_length"),
                    getIntent().getStringExtra("p_available"),
                    getIntent().getStringExtra("l_difficulty"),
                    getIntent().getStringExtra("h_description"),
                    getIntent().getStringExtra("h_s_requirement"),
                    getIntent().getStringExtra("h_r_gear"));
            toolbar.setTitle("Edit Hiking Trip");
        }
        else
        {
            toolbar.setTitle("Add Hiking Trip");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hiking_details_layout, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem delete_btn = menu.findItem(R.id.itmObservationsDetailsDelete);
        delete_btn.setVisible(false);
        return true;
    }
    private void configureActivity(String observation_id_value, String h_date, String h_location, String h_name, String h_length,
                               String p_available, String l_difficulty, String h_description, String h_s_requirement, String h_r_gear) {
        hiddenID.setText(observation_id_value);
        setTitle("Edit Hiking Details");

        hikeDate.setText(h_date);
        location.setText(h_location);
        hiking_name.setText(h_name);
        length_of_hike.setText(h_length);
        description.setText(h_description);
        special_requirement.setText(h_s_requirement)
        ;
        recommended_gear.setText(h_r_gear);


        if (p_available.toLowerCase().equals("yes")) {
            parkingAvailable.check(R.id.rboYes);
        } else if (p_available.toLowerCase().equals("no")) {
            parkingAvailable.check(R.id.rboNo);
        }

        if (l_difficulty.toLowerCase().equals("easy")) {
            rgpLevelOfDifficulty.check(R.id.rboeasy);
        } else if (l_difficulty.toLowerCase().equals("medium")) {
            rgpLevelOfDifficulty.check(R.id.rbomedium);
        } else if (l_difficulty.toLowerCase().equals("hard")) {
            rgpLevelOfDifficulty.check(R.id.rbohard);
        }

        nextButton.setText("View Changes");
    }
    public void initiateDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);

                hikeDate.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }

    private String makeDateString(int day, int month, int year) {
        // Returning Month from function and Returning date as String
        return getMonthParse(month) + " " + day + " " + year;
    }

    private String getMonthParse(int month) {
        if (month == 1) {
            return "JAN";
        } else if (month == 2) {
            return "FEB";
        } else if (month == 3) {
            return "MAR";
        } else if (month == 4) {
            return "APR";
        } else if (month == 5) {
            return "MAY";
        } else if (month == 6) {
            return "JUN";
        } else if (month == 7) {
            return "JUL";
        } else if (month == 8) {
            return "AUG";
        } else if (month == 9) {
            return "SEP";
        } else if (month == 10) {
            return "OCT";
        } else if (month == 11) {
            return "NOV";
        } else if (month == 12) {
            return "DEC";
        }
        // Default should never happen
        return "JAN";
    }


    // Getting Current Date
    private String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        // Returing a function that returns the date as a string
        return makeDateString(day, month, year);
    }

    public void pickDate(View view) {
        initiateDatePicker();
        datePickerDialog.show();
    }

//    Checking Required Validation Fields
    public Boolean validation() {
        Boolean result = false;
        if (hiking_name.getText().toString().isEmpty() || location.getText().toString().isEmpty() || hikeDate.getText().toString().isEmpty() || length_of_hike.getText().toString().isEmpty()) {
            result = false;
        } else if (!hiking_name.getText().toString().isEmpty() && !location.getText().toString().isEmpty() && !hikeDate.getText().toString().isEmpty() && !length_of_hike.getText().toString().isEmpty()) {
            result = true;
        }
        return result;
    }

//    Process Hiking Form
    public void processHikingForm(View view) {
        // Validating Form
            Boolean validation_result = validation();

        if (validation_result == false) {
            String alert_message = "Please Input all required fields";
            Toast.makeText(this, alert_message, Toast.LENGTH_SHORT).show();
        } else if (validation_result == true) {
            if (nextButton.getText().toString().toLowerCase().equals("view changes")) {
                if (amendValidation(hiddenID.getText().toString()) == "fail") {
                    Toast.makeText(this, "No changes were detected", Toast.LENGTH_SHORT).show();
                } else if (amendValidation(hiddenID.getText().toString()) == "pass") {
                    confirmDetails("pass");
                }
            } else if (nextButton.getText().toString().toLowerCase().equals("next")) {
                confirmDetails("next");
            }
        }
    }

//    Function to Open Form that Views Details


    // Open Confirm Details Activity
    public void confirmDetails(String validation) {
        String h_date = hikeDate.getText().toString();
        String h_location = location.getText().toString();
        String h_name = hiking_name.getText().toString();
        String h_length = length_of_hike.getText().toString();
        int p_available_rbtn = parkingAvailable.getCheckedRadioButtonId();
        selectd_p_available = (RadioButton) findViewById(p_available_rbtn);
        String p_available = selectd_p_available.getText().toString();
        int l_difficulty_rbtn = rgpLevelOfDifficulty.getCheckedRadioButtonId();
        selectd_l_difficulty = (RadioButton) findViewById(l_difficulty_rbtn);
        String l_difficulty = selectd_l_difficulty.getText().toString();
        String h_description = description.getText().toString();
        String h_s_requirement = special_requirement.getText().toString();
        String h_r_gear = recommended_gear.getText().toString();

        Intent intent = new Intent(this, ConfirmObservationActivity.class);
        if (validation.equals("pass")) {
            intent.putExtra("observation_id", "edit_hiking");
        } else if (validation.equals("next")) {
            intent.putExtra("observation_id", "add");
        }
        intent.putExtra("observation_id_value", hiddenID.getText().toString());
        intent.putExtra("h_date", h_date);
        intent.putExtra("h_location", h_location);
        intent.putExtra("h_name", h_name);
        intent.putExtra("h_length", h_length);
        intent.putExtra("p_available", p_available);
        intent.putExtra("l_difficulty", l_difficulty);
        intent.putExtra("h_description", h_description);
        intent.putExtra("h_s_requirement", h_s_requirement);
        intent.putExtra("h_r_gear", h_r_gear);
        startActivity(intent);
    }
    public String amendValidation(String hiking_id) {
        int int_hiking_id = Integer.parseInt(hiking_id);
        myDb = new DatabaseHelper(HikingDetailsActivity.this);
        Cursor cursor = myDb.getSpecificHikesObservations(int_hiking_id);

        String h_name = "", h_location = "", l_difficulty = "", h_date = "", h_length = "", p_available = "", h_s_requirement = "", h_r_gear = "", h_description = "";

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                h_name = cursor.getString(1).toLowerCase();
                h_location = cursor.getString(2).toLowerCase();
                l_difficulty = cursor.getString(3).toLowerCase();
                h_date = cursor.getString(4).toLowerCase();
                h_length = cursor.getString(5).toLowerCase();
                p_available = cursor.getString(6).toLowerCase();
                h_s_requirement = cursor.getString(7).toLowerCase();
                h_r_gear = cursor.getString(8).toLowerCase();
                h_description = cursor.getString(9).toLowerCase();
            }
        }
        cursor.close();
        myDb.close();

        int p_available_rbtn = parkingAvailable.getCheckedRadioButtonId();
        selectd_p_available = (RadioButton) findViewById(p_available_rbtn);
        String p_available_value = selectd_p_available.getText().toString();
        int l_difficulty_rbtn = rgpLevelOfDifficulty.getCheckedRadioButtonId();
        selectd_l_difficulty = (RadioButton) findViewById(l_difficulty_rbtn);
        String l_difficulty_value = selectd_l_difficulty.getText().toString();


        String result = "fail";
        if (h_name.equals(hiking_name.getText().toString().toLowerCase())
                && h_location.equals(location.getText().toString().toLowerCase())
                && l_difficulty.equals(l_difficulty_value.toString().toLowerCase())
                && h_date.equals(hikeDate.getText().toString().toLowerCase())
                && h_length.equals(length_of_hike.getText().toString().toLowerCase())
                && p_available.equals(p_available_value.toString()) && h_s_requirement.equals(special_requirement.getText().toString().toLowerCase()) && h_r_gear.equals(recommended_gear.getText().toString().toLowerCase()) && h_description.equals(description.getText().toString().toLowerCase())) {
            result = "fail";
        } else if (h_name != hiking_name.getText().toString().toLowerCase()
                || h_location != location.getText().toString().toLowerCase()
                || l_difficulty != l_difficulty_value.toString().toLowerCase()
                || h_date != hikeDate.getText().toString().toLowerCase()
                || h_length != length_of_hike.getText().toString().toLowerCase() || p_available != p_available_value.toString()
                || h_s_requirement != special_requirement.getText().toString().toLowerCase() || h_r_gear != recommended_gear.getText().toString().toLowerCase() || h_description != description.getText().toString().toLowerCase()) {
            result = "pass";
        }
        return result;
    }

}
