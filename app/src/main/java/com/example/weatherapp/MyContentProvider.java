package com.example.weatherapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.concurrent.ExecutionException;

public class MyContentProvider extends ContentProvider {
    private SQLiteDatabase myDB;
    static final String DB_NAME = "company";
    static final String DB_TABLE = "weather";
    static final int DB_VER = 1;
    static final String CREATE_DB_TABLE =
            "CREATE TABLE " + DB_TABLE + "( _id integer PRIMARY KEY AUTOINCREMENT , location TEXT ,temperature TEXT , wind TEXT , humidity TEXT , weather_icon TEXT) ;";


    public static final String AUTHIRITY = "com.example.weatherapp.mycontentprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHIRITY + "/weather");

    static int WEATHER = 1;
    static int WEATHER_ID = 2;

    static final UriMatcher myUri = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        myUri.addURI(AUTHIRITY,"weather", WEATHER);
        myUri.addURI(AUTHIRITY, "weather/#", WEATHER_ID);
    }





    private class MyOwnDatabase extends SQLiteOpenHelper{

        public MyOwnDatabase(Context ct){
            super(ct, DB_NAME, null, DB_VER);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
//            onCreate(db);
//            db.execSQL("CREATE TABLE " + DB_TABLE + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,temperature TEXT,wind TEXT,humidity TEXT,weather_icon TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }
    }



    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long row = myDB.insert(DB_TABLE, null, values);

        if (row > 0){
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, row);
            getContext().getContentResolver().notifyChange(_uri, null);
//            return uri;
        }

        return uri;

//        throw new SQLException("Failed to add a record into " +uri);
    }

    @Override
    public boolean onCreate() {
        MyOwnDatabase myHelper = new MyOwnDatabase(getContext());

        myDB = myHelper.getWritableDatabase();

        return (myDB == null)?false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        SQLiteQueryBuilder myQuery = new SQLiteQueryBuilder();
        myQuery.setTables(DB_TABLE);

        Cursor cr = myQuery.query(myDB, null, null, null, null,null,"_id");

        cr.setNotificationUri(getContext().getContentResolver(),uri);

        return cr;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
