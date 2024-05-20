package hk3971t.gre.ac.uk.m_hike_hikermanagementapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import hk3971t.gre.ac.uk.m_hike_hikermanagementapp.Utils.DatabaseHelper;

public class ViewSpecificObservationActivity extends AppCompatActivity {
    private TextView o_name, o_time, o_comment, hiddenID;
    private Button deleteBtn, editBtn;
    private DatabaseHelper myDb;
    DatabaseHelper mHikeDB;
    public static Activity endThis;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_specific_observation);


        // Declaration of Controls
        o_name = (TextView) findViewById(R.id.txtConfrimObservationName);
        o_time = (TextView) findViewById(R.id.txtConfirmTime);
        o_comment = (TextView) findViewById(R.id.txtConfrimComment);
        hiddenID = (TextView) findViewById(R.id.txtHiddenID);
        editBtn = (Button) findViewById(R.id.btnEditObservation);
        deleteBtn = (Button) findViewById(R.id.btnDelete);
        toolbar = (Toolbar) findViewById(R.id.tbrtoolbar);
        setSupportActionBar(toolbar);

        endThis = this;

        int sent_hiking_id = Integer.parseInt(getIntent().getStringExtra("observation_value"));
        hiddenID.setText(getIntent().getStringExtra("observation_value"));
        loadSpecificObservation(sent_hiking_id);
        Log.e("Log ", hiddenID.getText().toString());
        editBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewSpecificObservationActivity.this, HikingObservationDetailsActivity.class);
                intent.putExtra("observation_value", hiddenID.getText().toString());
                intent.putExtra("observation_id", "edit");
                intent.putExtra("o_name", o_name.getText().toString());
                intent.putExtra("o_time", o_time.getText().toString());
                intent.putExtra("o_comment", o_comment.getText().toString());
                startActivity(intent);
            }
        });
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
    void loadSpecificObservation(int observation_id) {
        myDb = new DatabaseHelper(ViewSpecificObservationActivity.this);
        Cursor cursor = myDb.getSpecificHikingObservations(observation_id);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                hiddenID.setText(cursor.getString(0));
                o_name.setText(cursor.getString(1));
                o_time.setText(cursor.getString(2));
                o_comment.setText(cursor.getString(3));
            }
        }
        cursor.close();
        myDb.close();
    }

    public void deleteHikingObservation(View view) {
        AlertDialog.Builder deleteConfirmation = new AlertDialog.Builder(ViewSpecificObservationActivity.this);
        deleteConfirmation.setMessage("Delete Hiking Observation: " + o_name.getText().toString() + " ?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDb = new DatabaseHelper(ViewSpecificObservationActivity.this);
                        String result = myDb.deleteHikingObservation( Integer.parseInt(hiddenID.getText().toString()));
                        if (result == "fail") {
                            Toast.makeText(ViewSpecificObservationActivity.this, "Failed to Delete", Toast.LENGTH_SHORT).show();
                        } else if (result == "success") {
                            Toast.makeText(ViewSpecificObservationActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT)
                                    .show();
                            Intent intent = new Intent(ViewSpecificObservationActivity.this, HikingDetailsActivity.class);
                            ViewSpecificObservationActivity.endThis.finish();
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
}