package hk3971t.gre.ac.uk.m_hike_hikermanagementapp.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import hk3971t.gre.ac.uk.m_hike_hikermanagementapp.Modal.MHikeModal;

public class DatabaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    private Context context;
    private static final String DATABASE_NAME = "MHIKING_DATABASE.db";
    private static final String MAIN_TABLE_NAME = "hiking_tbl";
    private static final String HIKING_NAME_COLUMN = "hiking_name";
    private static final String LOCATION_COLUMN = "location";
    private static final String DATE_COLUMN = "date";
    private static final String LEVEL_OF_DIFFICULTY_COLUMN = "level_of_difficulty";
    private static final String DESCRIPTION_COLUMN = "description";
    private static final String PARKING_AVAILABLE_COLUMN = "parking_available";
    private static final String LENGTH_OF_HIKE_COLUMN = "length_of_hike";
    private static final String SPECIAL_REQUIREMENTS_COLUMN = "special_requirements";
    private static final String RECOMMENDED_GEAR = "recommended_gear";
    private static final String ID = "id";
    private static final String OBSERVATIONS_TABLE_NAME = "observatoins_tbl";
    private static final String OBSERVATION_ID_COLUMN = "observation_id";
    private static final String OBSERVATION_COLUMN = "observation";
    private static final String OBSERVATION_TIME_COLUMN = "observation_time";
    private static final String OBSERVATION_COMMENT_COLUMN = "observation_comment";
    private static final String OBSERVATION_REF_COLUMN = "observation_ref";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 2);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "  + MAIN_TABLE_NAME +
                " (" + ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                + HIKING_NAME_COLUMN + " TEXT NOT NULL,"
                + LOCATION_COLUMN + " TEXT NOT NULL,"
                + LEVEL_OF_DIFFICULTY_COLUMN + " TEXT CHECK( " + LEVEL_OF_DIFFICULTY_COLUMN + " IN ('easy','medium', 'hard')) NOT NULL,"
                + DATE_COLUMN + " DATE NOT NULL,"
                + LENGTH_OF_HIKE_COLUMN + " INTEGER NOT NULL,"
                + PARKING_AVAILABLE_COLUMN + " TEXT CHECK( " + PARKING_AVAILABLE_COLUMN + " IN ('yes','no')) NOT NULL,"
                + SPECIAL_REQUIREMENTS_COLUMN + " TEXT NOT NULL,"
                + RECOMMENDED_GEAR + " TEXT NOT NULL,"
                + DESCRIPTION_COLUMN + " TEXT )");


        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "  + OBSERVATIONS_TABLE_NAME +
                "(" + OBSERVATION_ID_COLUMN + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                + OBSERVATION_COLUMN + " TEXT NOT NULL,"
                + OBSERVATION_TIME_COLUMN + " DATETIME NOT NULL,"
                + OBSERVATION_COMMENT_COLUMN+ " TEXT ,"
                + OBSERVATION_REF_COLUMN + " INTEGER NOT NULL,"
                + " FOREIGN KEY (" + OBSERVATION_REF_COLUMN + ") REFERENCES " + MAIN_TABLE_NAME + " (" + ID + ") ON UPDATE CASCADE ON DELETE CASCADE)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + MAIN_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OBSERVATIONS_TABLE_NAME);
        onCreate(db);
    }

    public void insertHike(String hiking_name, String location, String levelOfDifficulty, String date, int lengthOfHike, String parkingAvailable, String specialRequirements, String recommendedGear, String description )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(HIKING_NAME_COLUMN, hiking_name);
        values.put(LOCATION_COLUMN, location);
        values.put(LEVEL_OF_DIFFICULTY_COLUMN, levelOfDifficulty.toLowerCase());
        values.put(DATE_COLUMN, String.valueOf(date));
        values.put(LENGTH_OF_HIKE_COLUMN, lengthOfHike);
        values.put(PARKING_AVAILABLE_COLUMN, parkingAvailable);
        values.put(SPECIAL_REQUIREMENTS_COLUMN, specialRequirements);
        values.put(RECOMMENDED_GEAR, recommendedGear);
        values.put(DESCRIPTION_COLUMN, description);

        long result = db.insert(MAIN_TABLE_NAME, null, values);
        if (result == -1) {
            Toast.makeText(context, "Insert Error", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(context, "Hiking Trip Successfully added", Toast.LENGTH_SHORT).show();
        }
    }

    public String updateHike(int id, String hiking_name, String location, String levelOfDifficulty, String date, int lengthOfHike, String parkingAvailable, String specialRequirements, String recommendedGear, String description )
    {
        Log.e("MyLog" , levelOfDifficulty);
        ContentValues values = new ContentValues();

        SQLiteDatabase db = this.getWritableDatabase();

        values.put(HIKING_NAME_COLUMN, hiking_name);
        values.put(LOCATION_COLUMN, location);
        values.put(LEVEL_OF_DIFFICULTY_COLUMN, levelOfDifficulty);
        values.put(DATE_COLUMN, date);
        values.put(LENGTH_OF_HIKE_COLUMN, lengthOfHike);
        values.put(PARKING_AVAILABLE_COLUMN,parkingAvailable);
        values.put(SPECIAL_REQUIREMENTS_COLUMN, specialRequirements);
        values.put(RECOMMENDED_GEAR, recommendedGear);
        values.put(DESCRIPTION_COLUMN, description);

        String res = "fail";

        long result = db.update(MAIN_TABLE_NAME, values, "id = ?", new String[]{String.valueOf(id)});
        if (result == -1) {
            res = "failed";
        } else {
            res = "successful";
        }
        return res;
    }
    public String updateHikeObservation(int id, String o_name, String o_time, String o_comment)
    {
        ContentValues values = new ContentValues();

        SQLiteDatabase db = this.getWritableDatabase();

        values.put(OBSERVATION_COLUMN, o_name);
        values.put(OBSERVATION_TIME_COLUMN, o_time);
        values.put(OBSERVATION_COMMENT_COLUMN, o_comment);

        String res = "fail";

        long result = db.update(OBSERVATIONS_TABLE_NAME, values, "observation_id = ?", new String[]{String.valueOf(id)});
        if (result == -1) {
            res = "failed";
        } else {
            res = "successful";
        }
        return res;
    }
    public String deleteHike(int id)
    {
        db = this.getWritableDatabase();
        long result = db.delete(MAIN_TABLE_NAME,  "id = ?", new String[]{String.valueOf(id)});
        String res ="fail";
        if (result == -1) {
            res = "fail";
        } else {
            res = "success";
        }
        return res;
    }
    public String deleteHikingObservation(int id)
    {
        db = this.getWritableDatabase();
        long result = db.delete(OBSERVATIONS_TABLE_NAME,  "observation_id = ?", new String[]{String.valueOf(id)});
        String res ="fail";
        if (result == -1) {
            res = "fail";
        } else {
            res = "success";
        }
        return res;
    }

    public String deleteAll()
    {
        db = this.getWritableDatabase();
        long result = db.delete(MAIN_TABLE_NAME,  null, null);
        String res ="fail";
        if (result == -1) {
            res = "fail";
        } else {
            res = "success";
        }
        return res;
    }

    @SuppressLint("Range")
    public List<MHikeModal> getAllHikes(){
        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<MHikeModal> modalList = new ArrayList<>();

        db.beginTransaction();
        try{
            cursor = db.query(MAIN_TABLE_NAME, null, null, null, null, null, null);
            if (cursor != null)
            {
                if (cursor.moveToFirst()){
                    do {
                        MHikeModal modal = new MHikeModal();
                        modal.getId(cursor.getInt(cursor.getColumnIndex(ID)));
                        modal.setHiking_name(cursor.getString(cursor.getColumnIndex(HIKING_NAME_COLUMN)));
                        modal.setLocation(cursor.getString(cursor.getColumnIndex(LOCATION_COLUMN)));
                        modal.setLevel_of_difficulty(cursor.getString(cursor.getColumnIndex(LEVEL_OF_DIFFICULTY_COLUMN)));
                        modal.setDate(cursor.getString(cursor.getColumnIndex(DATE_COLUMN)));
                        modal.setLength_of_hike(cursor.getInt(cursor.getColumnIndex(LENGTH_OF_HIKE_COLUMN)));
                        modal.setParking_available(cursor.getString(cursor.getColumnIndex(PARKING_AVAILABLE_COLUMN)));
                        modal.setSpecial_requirements(cursor.getInt(cursor.getColumnIndex(SPECIAL_REQUIREMENTS_COLUMN)));
                        modal.setRecommended_gear(cursor.getInt(cursor.getColumnIndex(RECOMMENDED_GEAR)));
                        modal.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION_COLUMN)));
                        modalList.add(modal);
                    }
                    while (cursor.moveToFirst());
                }
            }
        }
        finally {
            db.endTransaction();
            cursor.close();
        }
        return modalList;
    }

    public Cursor retrieveAllObservations() {
        String retrieve_observatoins = "SELECT * FROM " + MAIN_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(retrieve_observatoins, null);
        }

        Log.e("myTag", String.valueOf(cursor));
        return cursor;
    }

    public Cursor readSpecificHikingObservation(int hiking_id)
    {
        String retrieve_trips = "SELECT * FROM " + OBSERVATIONS_TABLE_NAME + " WHERE " + OBSERVATION_REF_COLUMN + " = '" + hiking_id  + "';";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(retrieve_trips, null);
        }
        return cursor;
    }

    public boolean insertHikeObservation(String observationName, String ObservationTime, String additionalColunm, int hiking_id)
    {
        boolean res = false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(OBSERVATION_COLUMN, observationName);
        values.put(OBSERVATION_TIME_COLUMN, ObservationTime);
        values.put(OBSERVATION_COMMENT_COLUMN, additionalColunm);
        values.put(OBSERVATION_REF_COLUMN, hiking_id);

        long result = db.insert(OBSERVATIONS_TABLE_NAME, null, values);
        if (result == -1) {
            res = false;
        } else {
            Toast.makeText(context, "Hiking Observation Added Successfully", Toast.LENGTH_SHORT).show();
            res = true;
        }

        return res;
    }
    public void deleteHikeObservation(int id)
    {
        db = this.getWritableDatabase();
        db.delete(OBSERVATIONS_TABLE_NAME, "id=", new String[]{String.valueOf(id)});
    }
    @SuppressLint("Range")
    public Cursor getAllHikesObservations(int id) {
        String retrieve_observation = "SELECT * FROM " + OBSERVATIONS_TABLE_NAME + " WHERE " + OBSERVATION_REF_COLUMN + " = '" + id + "';";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(retrieve_observation, null);
        }
        return cursor;
    }
    public Cursor getSpecificHikesObservations(int id) {
        String retrieve_observation = "SELECT * FROM " + MAIN_TABLE_NAME + " WHERE " + ID + " = '" + id + "';";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(retrieve_observation, null);
        }
        return cursor;
    }
    public Cursor getSpecificHikingObservations(int observation_id) {
        String retrieve_observation = "SELECT * FROM " + OBSERVATIONS_TABLE_NAME + " WHERE " + OBSERVATION_ID_COLUMN + " = '" + observation_id + "';";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(retrieve_observation, null);
        }
        return cursor;
    }
}