package hk3971t.gre.ac.uk.m_hike_hikermanagementapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hk3971t.gre.ac.uk.m_hike_hikermanagementapp.Utils.DatabaseHelper;

public class HikingObservationDetailsActivity extends AppCompatActivity {

    private Button btnAddObservation;
    private int hour, minute;
    private TextView txtvTime, hiddenID;

    private TextInputEditText observationName, additionalComments;

    private String hikingID;

    DatabaseHelper myDb;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiking_observation_details);

        hiddenID = (TextView) findViewById(R.id.txtHiddenID);
        hiddenID.setText(getIntent().getStringExtra("observation_id_value"));

        txtvTime = (TextView) findViewById(R.id.txthikingTime);
        observationName = (TextInputEditText) findViewById(R.id.observationnameTextEdit);
        additionalComments = (TextInputEditText) findViewById(R.id.txtObservationCommentEditText);
        btnAddObservation = (Button) findViewById(R.id.btnAddObservationbtn);
        toolbar = (Toolbar) findViewById(R.id.tbrtoolbar);
        setSupportActionBar(toolbar);

        txtvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initiate TimePicker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        HikingObservationDetailsActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //Initialize Hour and Minutes
                                hour = hourOfDay;
                                minute = minute;
                                //Store hour and minute in a string
                                String time = hour + ":" + minute;
                                //Initialize 24hr time format
                                SimpleDateFormat f24hs = new SimpleDateFormat(
                                        "HH:mm"
                                );
                                try {
                                    Date date = f24hs.parse(time);
                                    //Initialize 12hrs Format
                                    SimpleDateFormat f12hrs = new SimpleDateFormat(
                                            "hh:mm aa"
                                    );
                                    //Set selected Time on text view
                                    txtvTime.setText(f24hs.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12, 0, false
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(hour, minute);
                timePickerDialog.show();
            }
        });
//        Retriving Values
        String values_action_type = getIntent().getStringExtra("observation_id");

        if (values_action_type.equals("edit")) {
            toolbar.setTitle("Edit Observation");
            btnAddObservation.setText("Edit Observation");
            Log.e("My Tag", getIntent().getStringExtra("observation_value"));
//            Fetch Edit Values
            loadSpecificObservation(Integer.parseInt(getIntent().getStringExtra("observation_value")));

            btnAddObservation.setOnClickListener(new View.OnClickListener() {
                DatabaseHelper myDb = new DatabaseHelper(HikingObservationDetailsActivity.this);

                @Override
                public void onClick(View v) {
                    String result = myDb.updateHikeObservation(Integer.parseInt(hiddenID.getText().toString()), observationName.getText().toString(), txtvTime.getText().toString(), additionalComments.getText().toString().toLowerCase());
                    if (result == "failed") {
                        Toast.makeText(HikingObservationDetailsActivity.this, "Update Error", Toast.LENGTH_SHORT).show();
                    } else if (result == "successful") {
                        Toast.makeText(HikingObservationDetailsActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(HikingObservationDetailsActivity.this, ObservationDetailsActivity.class);
                        intent.putExtra("observation_value", hikingID);
                        finish();
                        startActivity(intent);
                    }
                }
            });
        }
        else
        {
            toolbar.setTitle("Add Observation");
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
//    Validate Fields
public Boolean hikingObservationValidation(String observation_name, String observationTime, String additional_comments)
{
    Boolean result = false;
    if (observation_name.isEmpty() || observationTime.isEmpty())
    {
        result = false;
    }
    else if (!observation_name.isEmpty() && !observationTime.isEmpty())
    {
        result = true;
    }
    return result;
}

//Add Observation
public void processHikingObservationForm(View view)
{
    Boolean validation_result = hikingObservationValidation(observationName.getText().toString(), txtvTime.getText().toString(), additionalComments.getText().toString());
    if (validation_result == false)
    {
        String alert_message = "Please Enter all required fields";
        Toast.makeText(this, alert_message, Toast.LENGTH_SHORT).show();
    }
    else if (validation_result == true)
    {
        myDb = new DatabaseHelper(HikingObservationDetailsActivity.this);

        boolean result = myDb.insertHikeObservation(observationName.getText().toString(), txtvTime.getText().toString(), additionalComments.getText().toString(), Integer.parseInt(hiddenID.getText().toString()));

        if (!result)
        {
            Toast.makeText(HikingObservationDetailsActivity.this, "Could Not Add Hiking Observation", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(HikingObservationDetailsActivity.this, "Hiking Observation Added Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ObservationDetailsActivity.class);
            intent.putExtra("observation_id_value",hiddenID.getText().toString());
            finish();
            startActivity(intent);
        }
    }
}

    void loadSpecificObservation(int observation_id) {
        myDb = new DatabaseHelper(HikingObservationDetailsActivity.this);
        Cursor cursor = myDb.getSpecificHikingObservations(observation_id);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                hiddenID.setText(cursor.getString(0));
                observationName.setText(cursor.getString(1));
                txtvTime.setText(cursor.getString(2));
                additionalComments.setText(cursor.getString(3));
                hikingID = cursor.getString(0);
            }
        }
        cursor.close();
        myDb.close();
    }
}