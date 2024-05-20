package hk3971t.gre.ac.uk.m_hike_hikermanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import hk3971t.gre.ac.uk.m_hike_hikermanagementapp.Modal.MHikeModal;
import hk3971t.gre.ac.uk.m_hike_hikermanagementapp.Utils.DatabaseHelper;

public class ConfirmObservationActivity extends AppCompatActivity {

    private TextView h_date, h_location, h_name, h_length, p_available, l_difficulty, h_description, h_s_requirement, h_r_gear, hiddenID, confirmHeading;
    private Button confirmObservationDetails, goback;
    private Toolbar confirmDetailsToolbar;
    private DatabaseHelper myDb;

    DatabaseHelper mHikeDB;
    public static Activity endThis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_observation);
        // Declaration of Controls
        h_date = (TextView) findViewById(R.id.txtConfrimDate);
        h_location = (TextView) findViewById(R.id.txtConfirmLocation);
        h_name = (TextView) findViewById(R.id.txtConfrimObservationName);
        h_length = (TextView) findViewById(R.id.txtConfirmHikingLength);
        p_available = (TextView) findViewById(R.id.txtConfirmParkingAvailable);
        l_difficulty = (TextView) findViewById(R.id.txtConfirmLevelOfDifficulty);
        h_description = (TextView) findViewById(R.id.txtConfirmDescription);
        h_s_requirement = (TextView) findViewById(R.id.txtConfirmSpecialRequirement);
        h_r_gear = (TextView) findViewById(R.id.txtConfirmRecommendedGear);
        hiddenID = (TextView) findViewById(R.id.txtTripid);
        confirmObservationDetails = (Button) findViewById(R.id.btnConfirmHiking);
        goback = (Button) findViewById(R.id.btnConfirmGoBack);
        confirmDetailsToolbar = findViewById(R.id.tbrHikingDetails);
        confirmHeading = findViewById(R.id.txtConfirmHikingDetails);

        endThis = this;

//        Retriving Values
        String values_action_type = getIntent().getStringExtra("observation_id");

        if (values_action_type.equals("add")) {
            confirmHeading.setText("Confirm Hiking Details");
            confirmDetailsToolbar.setTitle("Confirm Hiking Details");
            // Set the with Values
            h_date.setText(getIntent().getStringExtra("h_date"));
            h_location.setText(getIntent().getStringExtra("h_location"));
            h_name.setText(getIntent().getStringExtra("h_name"));
            h_length.setText(getIntent().getStringExtra("h_length"));
            p_available.setText(getIntent().getStringExtra("p_available"));
            l_difficulty.setText(getIntent().getStringExtra("l_difficulty"));
            h_description.setText(getIntent().getStringExtra("h_description"));
            h_s_requirement.setText(getIntent().getStringExtra("h_s_requirement"));
            h_r_gear.setText(getIntent().getStringExtra("h_r_gear"));
            hiddenID.setText(getIntent().getStringExtra("observation_id_value"));

//            Add Hiking Trip
            confirmObservationDetails.setOnClickListener(new View.OnClickListener() {
                DatabaseHelper myDB = new DatabaseHelper(ConfirmObservationActivity.this);

                @Override
                public void onClick(View v) {
                    myDB.insertHike(h_name.getText().toString(), h_location.getText().toString(), l_difficulty.getText().toString(), h_date.getText().toString(), Integer.parseInt(h_length.getText().toString()), p_available.getText().toString(), h_s_requirement.getText().toString(), h_r_gear.getText().toString(), h_description.getText().toString() );
                    Intent intent = new Intent(ConfirmObservationActivity.this, MainActivity.class);
                    startActivity(intent);
                    ConfirmObservationActivity.endThis.finish();
                    HikingDetailsActivity.endThis.finish();
                    finish();
                }
            });
        }
        else if (values_action_type.equals("view")) {
            // Set Action Bar Title
            setTitle("Hiking Details");

            confirmHeading.setText("Hiking Details");
             setSupportActionBar(confirmDetailsToolbar);
            confirmDetailsToolbar.setTitle("Hiking Details");
            confirmObservationDetails.setText("Hiking Observations");
            goback.setText("Edit Hiking Details");
            int sent_hiking_id = Integer.parseInt(getIntent().getStringExtra("observation_value"));
            hiddenID.setText(getIntent().getStringExtra("observation_id_value"));
            loadSpecificObservation(sent_hiking_id);
            confirmObservationDetails.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ConfirmObservationActivity.this, ObservationDetailsActivity.class);
                    intent.putExtra("observation_id_value", hiddenID.getText().toString());
                    startActivity(intent);
                }
            });
        }
        else if (values_action_type.equals("edit_hiking")) {
            confirmHeading.setText("Confirm Changes");
            confirmDetailsToolbar.setTitle("Confirm Hiking Details");
            // Set the with Values
            h_date.setText(getIntent().getStringExtra("h_date"));
            h_location.setText(getIntent().getStringExtra("h_location"));
            h_name.setText(getIntent().getStringExtra("h_name"));
            h_length.setText(getIntent().getStringExtra("h_length"));
            p_available.setText(getIntent().getStringExtra("p_available"));
            l_difficulty.setText(getIntent().getStringExtra("l_difficulty"));
            h_description.setText(getIntent().getStringExtra("h_description"));
            h_s_requirement.setText(getIntent().getStringExtra("h_s_requirement"));
            h_r_gear.setText(getIntent().getStringExtra("h_r_gear"));
            hiddenID.setText(getIntent().getStringExtra("observation_id_value"));

            confirmObservationDetails.setOnClickListener(new View.OnClickListener() {
                DatabaseHelper myDb = new DatabaseHelper(ConfirmObservationActivity.this);

                @Override
                public void onClick(View v) {
                    String result = myDb.updateHike(Integer.parseInt(hiddenID.getText().toString()), h_name.getText().toString(), h_location.getText().toString(), l_difficulty.getText().toString().toLowerCase(), h_date.getText().toString(), Integer.parseInt(h_length.getText().toString()), p_available.getText().toString().toLowerCase(), h_s_requirement.getText().toString(), h_r_gear.getText().toString(), h_description.getText().toString());
                    if (result == "failed") {
                        Toast.makeText(ConfirmObservationActivity.this, "Update Error", Toast.LENGTH_SHORT).show();
                    } else if (result == "successful") {
                        Toast.makeText(ConfirmObservationActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                        MainActivity.endThis.finish();
                        finish();
                        Intent intent = new Intent(ConfirmObservationActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    public void goBack(View view) {
        String btnText = (String) goback.getText();
        if (btnText.toLowerCase().equals("go back")) {
            finish();
        }
        else if (btnText.toLowerCase().equals("edit hiking details")) {
            Intent intent = new Intent(this, HikingDetailsActivity.class);
            intent.putExtra("observation_id", "edit_hiking_details");
            intent.putExtra("observation_id_value", hiddenID.getText().toString());
            intent.putExtra("h_date", h_date.getText().toString());
            intent.putExtra("h_location", h_location.getText().toString());
            intent.putExtra("h_name", h_name.getText().toString());
            intent.putExtra("h_length", h_length.getText().toString());
            intent.putExtra("p_available", p_available.getText().toString());
            intent.putExtra("l_difficulty", l_difficulty.getText().toString());
            intent.putExtra("h_description", h_description.getText().toString());
            intent.putExtra("h_s_requirement", h_s_requirement.getText().toString());
            intent.putExtra("h_r_gear", h_r_gear.getText().toString());
            startActivity(intent);

        }
    }

    void loadSpecificObservation(int hiking_id) {
        myDb = new DatabaseHelper(ConfirmObservationActivity.this);
        Cursor cursor = myDb.getSpecificHikesObservations(hiking_id);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                hiddenID.setText(cursor.getString(0));
                h_name.setText(cursor.getString(1));
                h_location.setText(cursor.getString(2));
                l_difficulty.setText(cursor.getString(3));
                h_date.setText(cursor.getString(4));
                h_length.setText(cursor.getString(5));
                p_available.setText(cursor.getString(6));
                h_s_requirement.setText(cursor.getString(7));
                h_r_gear.setText(cursor.getString(8));
                h_description.setText(cursor.getString(9));
            }
        }
        cursor.close();
        myDb.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hiking_details_layout, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem delete_btn = menu.findItem(R.id.itmObservationsDetailsDelete);
        if (confirmDetailsToolbar.getTitle().equals("Hiking Details")) {
            delete_btn.setVisible(true);
        } else {
            delete_btn.setVisible(false);
        }
        return true;
    }


//    Delete Hiking Trip
@Override
public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.itmObservationsDetailsDelete) {
        AlertDialog.Builder deleteConfirmation = new AlertDialog.Builder(ConfirmObservationActivity.this);
        deleteConfirmation.setMessage("Delete Hike: " + h_name.getText().toString() + " ?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDb = new DatabaseHelper(ConfirmObservationActivity.this);
                        String result = myDb.deleteHike( Integer.parseInt(hiddenID.getText().toString()));
                        if (result == "fail") {
                            Toast.makeText(ConfirmObservationActivity.this, "Failed to Delete", Toast.LENGTH_SHORT).show();
                        } else if (result == "success") {
                            Toast.makeText(ConfirmObservationActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT)
                                    .show();
                            Intent intent = new Intent(ConfirmObservationActivity.this, MainActivity.class);
                            MainActivity.endThis.finish();
                            ConfirmObservationActivity.endThis.finish();
                            finish();
                            startActivity(intent);
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = deleteConfirmation.create();
        alert.setTitle("Delete Hiking Trip");
        alert.show();
    }
    return true;
}
}