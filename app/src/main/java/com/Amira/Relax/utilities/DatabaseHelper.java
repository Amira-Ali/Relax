package com.Amira.Relax.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.Amira.Relax.activities.Login;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final Context context;
    private SQLiteDatabase db;
    private final String DATABASE_PATH;
    private static final String DATABASE_NAME = "BotData.sqlite3";
    private static final int DATABASE_VERSION = 1;
    private final String USER_TABLE = "user";
    private static final String COLUMN_UserName = "UserName";
    private static final String COLUMN_UserPass = "UserPass";

    private final String USER_Total_TABLE = "user_total";
    private final String COLUMN_Physical_Total = "physical_total";
    private final String COLUMN_Sleep_Total = "sleep_total";
    private final String COLUMN_Behavioral_Total = "behavioral_total";
    private final String COLUMN_Emotional_Total = "emotional_total";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        File outFile = context.getDatabasePath(DATABASE_NAME);
        DATABASE_PATH = outFile.getPath();
        createDb();
    }

    private void createDb() {
        boolean dbExist = checkDbExist();
        if (!dbExist) {
            this.getReadableDatabase();
            copyDatabase();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //here we create all tables
//        db.execSQL("CREATE TABLE " + User_Notes + " (" +
//                COLUMN_Note_ID + " TEXT, " +
//                COLUMN_UserName + " TEXT, " +
//                COLUMN_Note_Date + " TEXT, " +
//                COLUMN_Note_Content + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //upgrade all tables
//        db.execSQL("DROP TABLE IF EXISTS " + User_Notes);
//        onCreate(db);
    }

    private boolean checkDbExist() {
        db = null;

        try {
            db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READONLY);
        } catch (Exception ex) {
            Log.d("CheckDbExist:", ex.toString());
        }

        if (db != null) {
            db.close();
            return true;
        }

        return false;
    }

    private void copyDatabase() {
        try {
            InputStream inputStream = context.getAssets().open(DATABASE_NAME);

            String outFileName = DATABASE_PATH + DATABASE_NAME;

            OutputStream outputStream = new FileOutputStream(outFileName);

            byte[] b = new byte[1024];
            int length;

            while ((length = inputStream.read(b)) > 0) {
                outputStream.write(b, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private SQLiteDatabase openDatabase() {
        String path = DATABASE_PATH + DATABASE_NAME;
        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        return db;
    }

    public void close() {
        if (db != null) {
            db.close();
        }
    }

    public boolean CheckUserExist(String username, String password) {
        String[] columns = {COLUMN_UserName};
        db = openDatabase();

        String selection = COLUMN_UserName + " = ? and " + COLUMN_UserPass + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(USER_TABLE, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        close();

        return count > 0;
    }

    public int[] GetFlagsFromDB(String name) {
        int[] data = new int[4];
        if (name == null) {
            Intent loginIntent = new Intent(context, Login.class);
            context.startActivity(loginIntent);
        } else {
            String[] columns = {COLUMN_Physical_Total, COLUMN_Sleep_Total, COLUMN_Behavioral_Total,
                    COLUMN_Emotional_Total};
            db = openDatabase();
            String selection = COLUMN_UserName + " = ?";
            String[] selectionArgs = {name};

            Cursor cursor = this.db.query(USER_Total_TABLE, columns, selection, selectionArgs, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    data[0] = cursor.getInt(0);
                    data[1] = cursor.getInt(1);
                    data[2] = cursor.getInt(2);
                    data[3] = cursor.getInt(3);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return data;
    }

    public String RegisterNewUser(String name, String password) {
        String msg;
        try {
            db = openDatabase();
            ContentValues c = new ContentValues();
            c.put(COLUMN_UserName, name);
            c.put(COLUMN_UserPass, password);
            long result = db.insert(USER_TABLE, null, c);
            if (result != -1) {
                msg = "Registration Successfully";
            } else {
                msg = "Registration Failed";
            }
        } catch (Exception ex) {
            msg = ex.toString();
        }
        return msg;
    }

    public void SvUserTotal(String UserName, int physical_total, int sleep_total,
                            int behavioral_total, int emotional_total) {
        db = openDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_UserName, UserName);
        values.put(COLUMN_Physical_Total, physical_total);
        values.put(COLUMN_Sleep_Total, sleep_total);
        values.put(COLUMN_Behavioral_Total, behavioral_total);
        values.put(COLUMN_Emotional_Total, emotional_total);
        db.insert(USER_Total_TABLE, null, values);
    }

    public boolean IsSurveyTaken(String username) {
        String[] columns = {COLUMN_UserName};
        db = openDatabase();

        String selection = COLUMN_UserName + " = ? ";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(USER_Total_TABLE, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        close();
        return count > 0;
    }

//    public void SvUserNotes(String UserName, String note_date, String note_content) {
//        db = openDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_UserName, UserName);
//        values.put(COLUMN_Note_Date, note_date);
//        values.put(COLUMN_Note_Content, note_content);
//        db.insert(User_Notes, null, values);
//    }

}
