package com.projects050414.myapplication3.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by obelix on 12/05/2014.
 */
public class TripSQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "TripDB";

    // Trips table name
    private static final String TABLE_TRIPS = "trips";

    private static final String TABLE_RECENT_SUGGESTIONS = "searchsuggestions";

    // Books Table Columns names
    private static final String KEY_ID = "trip_id";
    private static final String KEY_FROM = "from";
    private static final String KEY_TO = "to";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_OWNER = "owner";

    private static final String[] COLUMNS = {KEY_ID,KEY_FROM,KEY_TO, KEY_DATE, KEY_TIME, KEY_OWNER};



    public TripSQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        // SQL statement to create book table
        String CREATE_TRIP_TABLE = "CREATE TABLE trips ( " +
                "trip_id TEXT PRIMARY KEY, " +
                "from TEXT, "+
                "to TEXT, "+
                "date TEXT, "+
                "time TEXT, "+
                "owner TEXT )";

        // create trips table
        db.execSQL(CREATE_TRIP_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS trips");

        // create fresh trips table
        this.onCreate(db);

    }

    public void addTrip(Trips trip){
        //for logging
        Log.d("addBook", trip.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_FROM, trip.getFrom()); // get title
        values.put(KEY_TO, trip.getTo()); // get author
        values.put(KEY_DATE, trip.getDate());
        values.put(KEY_TIME, trip.getTime());
        values.put(KEY_OWNER, trip.getOwner());
        // 3. insert
        db.insert(TABLE_TRIPS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Trips getTrip(String trip_id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_TRIPS, // a. table
                        COLUMNS, // b. column names
                        " trip_id = ?", // c. selections
                        new String[] { String.valueOf(trip_id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        Trips trip = new Trips();
        trip.setTripId(cursor.getString(0));
        trip.setFrom(cursor.getString(1));
        trip.setTo(cursor.getString(2));
        trip.setDate(cursor.getString(3));
        trip.setTime(cursor.getString(4));

        //log
        Log.d("getTrip("+trip_id+")", trip.toString());

        // 5. return book
        return trip;
    }


    public List<Trips> getAllTrips() {
        List<Trips> mytrips = new LinkedList<Trips>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_TRIPS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Trips trip = null;
        if (cursor.moveToFirst()) {
            do {
                trip = new Trips();
                trip.setTripId(cursor.getString(0));
                trip.setFrom(cursor.getString(1));
                trip.setTo(cursor.getString(2));
                trip.setDate(cursor.getString(3));
                trip.setTime(cursor.getString(4));
                // Add book to books
                mytrips.add(trip);
            } while (cursor.moveToNext());
        }

        Log.d("getAllBooks()", mytrips.toString());

        // return books
        return mytrips;
    }
    public int updateTrip(Trips trip) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("from", trip.getFrom()); // get title
        values.put("to", trip.getTo()); // get author
        values.put("date", trip.getDate()); // get title
        values.put("time", trip.getTime()); // get author

        // 3. updating row
        int i = db.update(TABLE_TRIPS, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(trip.getTripId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }
    public void deleteTrip(Trips trip) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_TRIPS, //table name
                KEY_ID+" = ?",  // selections
                new String[] { String.valueOf(trip.getTripId()) }); //selections args

        // 3. close
        db.close();

        //log
        Log.d("deleteTrip", trip.toString());

    }
}
