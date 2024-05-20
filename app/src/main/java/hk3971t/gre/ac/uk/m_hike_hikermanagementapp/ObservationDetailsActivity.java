package hk3971t.gre.ac.uk.m_hike_hikermanagementapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import hk3971t.gre.ac.uk.m_hike_hikermanagementapp.Adaptor.MHikeAdapter;
import hk3971t.gre.ac.uk.m_hike_hikermanagementapp.Adaptor.ObservationsAdapter;
import hk3971t.gre.ac.uk.m_hike_hikermanagementapp.Utils.DatabaseHelper;

public class ObservationDetailsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private DatabaseHelper myDb;
    public static Activity endThis;
    private TextView hiddenID;
    ArrayList<String> observation_id, observation_name, observation_time, observation_number;
    ObservationsAdapter observationsAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_details);

        mRecyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);

        hiddenID = (TextView) findViewById(R.id.txtHiddenID);
        hiddenID.setText(getIntent().getStringExtra("observation_id_value"));
        toolbar = (Toolbar) findViewById(R.id.tbrtoolbar);
        setSupportActionBar(toolbar);

//       Add New Hiking Observation
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ObservationDetailsActivity.this, HikingObservationDetailsActivity.class);
                intent.putExtra("observation_id_value", hiddenID.getText().toString());
                intent.putExtra("observation_id", "add");
                startActivity(intent);
            }
        });


        myDb = new DatabaseHelper(ObservationDetailsActivity.this);

        observation_id = new ArrayList<>();
        observation_name = new ArrayList<>();
        observation_time = new ArrayList<>();
        observation_number = new ArrayList<>();

        storeObservationData();
        setObservationNumbers();

        observationsAdapter = new ObservationsAdapter(ObservationDetailsActivity.this, observation_id, observation_name, observation_time, observation_number);
        mRecyclerView.setAdapter(observationsAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ObservationDetailsActivity.this));
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
    void storeObservationData() {
        Cursor cursor = myDb.readSpecificHikingObservation(Integer.parseInt(hiddenID.getText().toString()));
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Hiking Observations Found.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                observation_id.add(cursor.getString(0));
                observation_name.add(cursor.getString(1));
                observation_time.add(cursor.getString(2));
            }
        }
        cursor.close();
        myDb.close();
    }

    public void setObservationNumbers()
    {
        for (int i = 0; i < observation_id.size(); i++)
        {
            int number = i + 1;
            observation_number.add(String.valueOf(number));
        }
    }
}