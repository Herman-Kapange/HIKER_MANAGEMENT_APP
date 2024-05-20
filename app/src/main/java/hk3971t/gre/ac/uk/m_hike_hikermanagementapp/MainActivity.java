package hk3971t.gre.ac.uk.m_hike_hikermanagementapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuInflater;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import hk3971t.gre.ac.uk.m_hike_hikermanagementapp.Adaptor.MHikeAdapter;
import hk3971t.gre.ac.uk.m_hike_hikermanagementapp.Utils.DatabaseHelper;
import hk3971t.gre.ac.uk.m_hike_hikermanagementapp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private DatabaseHelper myDb;
    public static Activity endThis;
    ArrayList<String> observation_id, observation_name, observation_date, observation_location, observation_number;

    MHikeAdapter mHikeAdapter;
    private Menu menu;

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private Toolbar mainToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        endThis = this;

        mRecyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        mainToolbar = findViewById((R.id.tbrMain));

//       Add New Hiking Trip
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HikingDetailsActivity.class);
                intent.putExtra("observation_id", "add");
                startActivity(intent);
            }
        });
        myDb = new DatabaseHelper(MainActivity.this);

        setSupportActionBar(mainToolbar);
        observation_id = new ArrayList<>();
        observation_name = new ArrayList<>();
        observation_date = new ArrayList<>();
        observation_location = new ArrayList<>();
        observation_number = new ArrayList<>();
        storeObservationData();
        setObservationNumbers();
//        Loading Data
        mHikeAdapter = new MHikeAdapter(MainActivity.this, observation_id, observation_name, observation_date, observation_location, observation_number);
        mRecyclerView.setAdapter(mHikeAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homemenu, menu);

        MenuItem search_item = menu.findItem(R.id.itmHomeSearch);
        SearchView app_init_search_view = (SearchView) search_item.getActionView();
        app_init_search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            //Initiate Search
            //Code Modified and Obtained from: https://www.youtube.com/watch?v=CTvzoVtKoJ8
            @Override
            public boolean onQueryTextChange(String newText) {
                mHikeAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem formatBtn = menu.findItem(R.id.itmFormat);
        MenuItem searchBtn = menu.findItem(R.id.itmHomeSearch);

        formatBtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                AlertDialog.Builder deleteConfirmation = new AlertDialog.Builder(MainActivity.this);
                deleteConfirmation.setMessage("Are you Sure that You want to Delete All Data?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myDb = new DatabaseHelper(MainActivity.this);
                                String result = myDb.deleteAll();
                                if (result == "fail") {
                                    Toast.makeText(MainActivity.this, "Failed to Delete Data", Toast.LENGTH_SHORT).show();
                                } else if (result == "success") {
                                    Toast.makeText(MainActivity.this, "Data Successfully Deleted", Toast.LENGTH_SHORT)
                                            .show();
                                    finish();
                                    startActivity(getIntent());
                                }
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = deleteConfirmation.create();
                alert.setTitle("Permanently Delete All Data");
                alert.show();

                return false;
            }
        });
        return true;
    }
    //Retrieve Observations from Database
    void storeObservationData() {
        Cursor cursor = myDb.retrieveAllObservations();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                observation_id.add(cursor.getString(0));
                observation_name.add(cursor.getString(1));
                observation_location.add(cursor.getString(2));
                observation_date.add(cursor.getString(4));
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