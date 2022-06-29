package com.relax.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.relax.models.Note;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class dbHelper extends SQLiteOpenHelper {

    public static final int dbVersion = 2;
    public static final String databaseName = "relaxDB.db";
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor = null;
    boolean flag = false;

    //region Variables
    private static final String Table_Users = "users";
    private static final String Table_User_Flags = "user_flags";
    private static final String Table_Sessions = "user_session";
    private static final String Table_Notification_Settings = "notification_settings";
    private static final String Table_User_Notes = "user_notes";
    private static final String Table_Notes_Analysis = "notes_analysis";
    private static final String Table_Statement_Session = "statement_session";
    private static final String Table_Rate_Feedback = "rate_feedback";

    private static final String PK_UserID = "user_id";
    private static final String Col_UserName = "userName";
    private static final String Col_UserPass = "userPass";
    private static final String Col_UserJoinDate = "userJoinDate";
    private final String[] Columns_Users = new String[]{PK_UserID};//the returned column name. If it is set to null, all columns will be returned

    private static final String Col_Physical = "physical_flag";
    private static final String Col_Sleep = "sleep_flag";
    private static final String Col_Behavior = "behavioral_flag";
    private static final String Col_Emotion = "emotional_flag";
    private static final String Col_Physical_handled = "Physical_handled";
    private static final String Col_Sleep_handled = "Sleep_handled";
    private static final String Col_Behavior_handled = "Behavior_handled";
    private static final String Col_Emotion_handled = "Emotion_handled";
    private static final String Col_Survey_Date = "survey_date";
    private final String[] Columns_Flags = new String[]{PK_UserID, Col_Physical, Col_Sleep, Col_Behavior, Col_Emotion,
            Col_Physical_handled, Col_Sleep_handled, Col_Behavior_handled, Col_Emotion_handled};

    private static final String PK_SessionID = "session_id";
    private static final String FK_UserID = "user_id";
    private static final String Col_SessionDate = "session_date";
    private static final String Col_SessionStart = "session_start";
    private static final String Col_SessionEnd = "session_end";
    private final String[] Columns_SessionID = new String[]{PK_SessionID, PK_UserID};

    private static final String PK_NotificationID = "row_id";
    private static final String Col_NotificationTime = "notification_time";
    private final String[] Columns_Notification = new String[]{Col_NotificationTime};

    private static final String Col_NoteText = "note_text";
    private static final String Col_NoteDate = "note_date";
    private final String[] Columns_Notes = new String[]{PK_UserID, Col_NoteText, Col_NoteDate};

    private static final String Col_NoteSummary = "note_summary";
    private static final String Col_NoteScore = "note_score";
    private static final String Col_NoteFlag = "note_flag";

    private static final String Col_StatementID = "id";
    private static final String Col_SessionID = "session_id";

    private static final String Col_Rate = "rate";
    private static final String Col_Feedback = "feedback";

    //endregion

    //region Create Statements
    private static final String Create_Table_Users = "CREATE TABLE " + Table_Users + " (\n" +
            "    " + PK_UserID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "    " + Col_UserName + " varchar(200) NOT NULL,\n" +
            "    " + Col_UserPass + " varchar(200) NOT NULL,\n" +
            "    " + Col_UserJoinDate + " datetime NOT NULL" +
            ");";

    private static final String Create_Table_User_Flags = "CREATE TABLE "
            + Table_User_Flags + "(" + PK_UserID + " INTEGER NOT NULL  PRIMARY KEY," + Col_Physical + " TEXT," + Col_Sleep + " TEXT," +
            Col_Behavior + " TEXT," + Col_Emotion + " TEXT," + Col_Physical_handled + " INTEGER," + Col_Sleep_handled + " INTEGER,"
            + Col_Behavior_handled + " INTEGER," + Col_Emotion_handled + " INTEGER, " + Col_Survey_Date + " TEXT " + ")";

    private static final String Create_Table_User_Session = "CREATE TABLE " + Table_Sessions +
            "(" + PK_SessionID + " INTEGER PRIMARY KEY AUTOINCREMENT,  " +
            FK_UserID + " INTEGER," +
            Col_SessionDate + " datetime, " +
            Col_SessionStart + " TEXT, " +
            Col_SessionEnd + " TEXT, " +
            " FOREIGN KEY (" + FK_UserID + ") REFERENCES " + Table_Users + " (" + PK_UserID + ") " +
            ")";

    private static final String Create_Table_Notification_Settings = "CREATE TABLE "
            + Table_Notification_Settings + "(" + PK_NotificationID + " INTEGER PRIMARY KEY AUTOINCREMENT,  " + PK_UserID + "  INTEGER NOT NULL," + Col_NotificationTime + " TEXT " + ")";

    private static final String Create_Table_User_Notes = "CREATE TABLE "
            + Table_User_Notes + "(" + PK_UserID + "  INTEGER NOT NULL," + Col_NoteText + " TEXT," + Col_NoteDate + " TEXT" + ")";

    private static final String Create_Table_Notes_Analysis = "CREATE TABLE "
            + Table_Notes_Analysis + "(" + PK_UserID + "  INTEGER NOT NULL," + Col_NoteSummary + " TEXT," + Col_NoteScore + " TEXT," + Col_NoteFlag + " INTEGER" + ")";

    private static final String Create_Table_Statement_Session = "CREATE TABLE "
            + Table_Statement_Session + "(" + Col_StatementID + "  INTEGER NOT NULL," + Col_SessionID + " INTEGER NOT NULL" + ")";

    private static final String Create_Table_Rate_Feedback = "CREATE TABLE "
            + Table_Rate_Feedback + "(" + PK_UserID + " INTEGER NOT NULL," + Col_Rate + " TEXT," + Col_Feedback + " TEXT " + ")";
    //endregion

    public dbHelper(Context context) {
        super(context, databaseName, null, dbVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Table_Users);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Table_User_Flags);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Table_Sessions);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Table_Notification_Settings);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Table_User_Notes);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Table_Notes_Analysis);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Table_Statement_Session);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Table_Rate_Feedback);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Create_Table_Users);
        sqLiteDatabase.execSQL(Create_Table_User_Flags);
        sqLiteDatabase.execSQL(Create_Table_User_Session);
        sqLiteDatabase.execSQL(Create_Table_Notification_Settings);
        sqLiteDatabase.execSQL(Create_Table_User_Notes);
        sqLiteDatabase.execSQL(Create_Table_Notes_Analysis);
        sqLiteDatabase.execSQL(Create_Table_Statement_Session);
        sqLiteDatabase.execSQL(Create_Table_Rate_Feedback);
    }

    //region User Operations
    public int getUserID(String username, String password) {
        int result = 0;
        try {
            sqLiteDatabase = this.getReadableDatabase();
            String selection = Col_UserName + "=? and " + Col_UserPass + "=?";
            String[] selectionArgs = {username, password};
            cursor = sqLiteDatabase.query(Table_Users, Columns_Users, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        result = cursor.getInt(0);
                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLiteException ex) {
            Log.d("getUserID", " " + ex);
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return result;
    }

    public int checkUserName(String username) {
        int result = 0;
        try {
            sqLiteDatabase = this.getReadableDatabase();
            String[] selectionArgs = {username};
            cursor = sqLiteDatabase.query(Table_Users, Columns_Users, Col_UserName + "=? ", selectionArgs, null, null, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        result = cursor.getInt(0);
                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLiteException ex) {
            Log.d("checkUserName", " " + ex);
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return result;
    }

    public long addUser(String name, String pass, String joiningDate) {
        long row_id = 0;
        try {
            sqLiteDatabase = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Col_UserName, name);
            values.put(Col_UserPass, pass);
            values.put(Col_UserJoinDate, joiningDate);
            row_id = sqLiteDatabase.insert(Table_Users, null, values);
        } catch (SQLiteException ex) {
            Log.d("addUser", " " + ex);
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return row_id;
    }

    public void setUserNotificationTime(int userID, String time) {
        flag = false;
        long row_id;
        try {
            sqLiteDatabase = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(PK_UserID, userID);
            values.put(Col_NotificationTime, time);
            row_id = sqLiteDatabase.insert(Table_Notification_Settings, null, values);
            if (row_id != -1) {
                flag = true;
            }
        } catch (SQLiteException ex) {
            Log.d("setUserNotificationTime", " " + ex);
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
    }

    public String getUserNotificationTime(int userID) {
        String notificationTime = "";
        try {
            sqLiteDatabase = this.getReadableDatabase();
            String whereClause = "row_id=(SELECT MAX(row_id) from notification_settings) and user_id=" + userID;
            cursor = sqLiteDatabase.query(Table_Notification_Settings, Columns_Notification, whereClause, null, null, null, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
            } else {
                if (cursor.moveToFirst()) {
                    notificationTime = cursor.getString(0);
                }
            }
        } catch (SQLiteException ex) {
            notificationTime = ex.toString();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return notificationTime;
    }

    public boolean checkUserSurvey(int userID) {
        flag = false;
        try {
            sqLiteDatabase = this.getReadableDatabase();
            String sql = "SELECT * FROM " + Table_User_Flags + " where " + PK_UserID + "=?";
            String[] selectionArgs = new String[]{String.valueOf(userID)};
            cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
            flag = cursor.getCount() > 0;
        } catch (SQLiteException ex) {
            Log.d("checkUserSurvey", " " + ex);
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return flag;
    }

    public void getUserFlags(int userID) {
        try {
            sqLiteDatabase = this.getReadableDatabase();
            String[] selectionArgs = new String[]{String.valueOf(userID)};
            cursor = sqLiteDatabase.query(Table_User_Flags, Columns_Flags, PK_UserID + "=?  ", selectionArgs, null, null, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
            } else {
                if (cursor.moveToFirst()) {
                    do {
                        globalVariables.physicalFlag = cursor.getString(1);
                        globalVariables.sleepFlag = cursor.getString(2);
                        globalVariables.behaviorFlag = cursor.getString(3);
                        globalVariables.emotionalFlag = cursor.getString(4);
                        globalVariables.physicalHandled = cursor.getInt(5);
                        globalVariables.sleepHandled = cursor.getInt(6);
                        globalVariables.behaviorHandled = cursor.getInt(7);
                        globalVariables.emotionalHandled = cursor.getInt(8);
                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLiteException ex) {
            Log.d("GetUserFlag", " " + ex);
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
    }

    public void insertUserFlags(int userID, String physical_flag, String sleep_flag, String behavioral_flag, String emotional_flag) {
        try {
            sqLiteDatabase = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(PK_UserID, userID);
            values.put(Col_Physical, physical_flag);
            values.put(Col_Sleep, sleep_flag);
            values.put(Col_Behavior, behavioral_flag);
            values.put(Col_Emotion, emotional_flag);
            values.put(Col_Physical_handled, 0);
            values.put(Col_Sleep_handled, 0);
            values.put(Col_Behavior_handled, 0);
            values.put(Col_Emotion_handled, 0);
            values.put(Col_Survey_Date, String.valueOf(LocalDate.now()));
            sqLiteDatabase.insert(Table_User_Flags, null, values);
        } catch (SQLiteException ex) {
            Log.d("InsertUserFlags", " " + ex);
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
    }

    public void updateUserFlags(int userID, int physical_handled, int sleep_handled, int behavioral_handled, int emotional_handled) {
        try {
            sqLiteDatabase = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Col_Physical_handled, physical_handled);
            values.put(Col_Sleep_handled, sleep_handled);
            values.put(Col_Behavior_handled, behavioral_handled);
            values.put(Col_Emotion_handled, emotional_handled);
            sqLiteDatabase.update(Table_User_Flags, values, PK_UserID + "=?", new String[]{String.valueOf(userID)});
        } catch (SQLiteException ex) {
            Log.d("UpdateUserFlags", " " + ex);
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
    }

    public String getSurveyDate(int userID) {
        String surveyDate = "";
        try {
            sqLiteDatabase = this.getReadableDatabase();
            String[] selectionArgs = new String[]{String.valueOf(userID)};
            String[] selectedColumns = new String[]{Col_Survey_Date};
            cursor = sqLiteDatabase.query(Table_User_Flags, selectedColumns, PK_UserID + "=?  ", selectionArgs, null, null, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
            } else {
                if (cursor.moveToFirst()) {
                    do {
                        surveyDate = cursor.getString(0);
                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLiteException ex) {
            Log.d("getSurveyDate", " " + ex);
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return surveyDate;
    }


//endregion

    //region Session Operations
    public void insertNewSession(int userID) {

        try {
            sqLiteDatabase = getWritableDatabase();
            ContentValues values = new ContentValues();//PK_SessionID, FK_UserID
            // values.put(PK_SessionID, session_id); is autoincrement col, so it'll be inserted automatically
            values.put(FK_UserID, userID);
            values.put(Col_SessionDate, globalVariables.currentDate);
            values.put(Col_SessionStart, globalVariables.sessionStart);
            values.put(Col_SessionEnd, "00:00:00");
            int session_id = (int) sqLiteDatabase.insert(Table_Sessions, null, values);
            if (session_id != -1) {
                globalVariables.sessionID = session_id;
            }
        } catch (SQLiteException ex) {
            Log.d("insertNewSession", " " + ex);
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
    }

    public void updateSessionEndTime(int userID) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String session_end =String.valueOf(LocalTime.now().format(formatter));
        String whereClause = PK_UserID + "=? AND " + Col_SessionDate + "=? AND " + Col_SessionStart + "=?";
        String[] whereArgs = new String[]{String.valueOf(userID), globalVariables.currentDate, globalVariables.sessionStart};
        try {
            sqLiteDatabase = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Col_SessionEnd, session_end);
            sqLiteDatabase.update(Table_Sessions, values, whereClause, whereArgs);
        } catch (SQLiteException ex) {
            Log.d("updateSessionEndTime", " " + ex);
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
    }

    public List<Integer> getSessionCountPerMonth(int userID) {
        List<Integer> sessions_counts = new ArrayList<>();

        try {
            sqLiteDatabase = this.getReadableDatabase();
            ArrayList<String> months = new ArrayList<String>() {
                {
                    add("01");
                    add("02");
                    add("03");
                    add("04");
                    add("05");
                    add("06");
                    add("07");
                    add("08");
                    add("09");
                    add("10");
                    add("11");
                    add("12");
                }
            };

            for (String mon : months) {
                String sql = "select count(*)  from user_session where user_id= " + userID + " and strftime('%m', session_date)= '" + mon + "'";
                cursor = sqLiteDatabase.rawQuery(sql, null);
                if (cursor.getCount() <= 0) {
                    cursor.close();
                } else {
                    cursor.moveToFirst();
                    sessions_counts.add(cursor.getInt(0));
                }
            }
        } catch (SQLiteException ex) {
            Log.d("getSessionCountPerMonth", " " + ex);
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }

        return sessions_counts;
    }

    public String getUserTotalTime(int userID) {
        String time = "00:00:00";
        int minutes = 0;
        Duration timeElapsed;
        LocalTime session_start, session_end;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        try {
            sqLiteDatabase = this.getReadableDatabase();
            String sql = "select session_start, session_end from user_session where user_id= " + userID;
            cursor = sqLiteDatabase.rawQuery(sql, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
            } else {
                if (cursor.moveToFirst()) {
                    do {
                        session_start = LocalTime.parse(cursor.getString(0),formatter);
                        if (!cursor.getString(1).isEmpty()) {
                            session_end = LocalTime.parse(cursor.getString(1),formatter);
                        } else {
                            session_end = LocalTime.parse("00:00:00",formatter);
                        }

                        timeElapsed = Duration.between(session_start, session_end);
                        minutes += (int) timeElapsed.toMinutes();
                    } while (cursor.moveToNext());
                }
                time = convertMinuteToHourMinute(minutes);
            }
        } catch (SQLiteException ex) {
            Log.d("getUserTotalTime", " " + ex);
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return time;
    }

    private String convertMinuteToHourMinute(int t) {
        String time;
        int hours = t / 60; //since both are ints, you get an int
        int minutes = t % 60;
        time = hours + ":" + minutes;
        return time;
    }

    public boolean checkPreviousSession(String persona) {
        flag = false;
        try {
            sqLiteDatabase = this.getReadableDatabase();
            String sql = "SELECT id from statement where persona = '" + persona + "' LIMIT 1";
            cursor = sqLiteDatabase.rawQuery(sql, null);
            flag = cursor.getCount() > 0;
        } catch (SQLiteException ex) {
            Log.d("checkPreviousSession", String.valueOf(ex));
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return flag;
    }

    public int getSessionCount(int userID) {
        int count = 0;
        try {
            sqLiteDatabase = this.getReadableDatabase();
            String sql = "SELECT count(*) from user_session where user_id = " + userID;
            cursor = sqLiteDatabase.rawQuery(sql, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
            } else {
                if (cursor.moveToFirst()) {
                    count = cursor.getInt(0);
                }
            }
        } catch (SQLiteException ex) {
            Log.d("checkPreviousSession", String.valueOf(ex));
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return count;
    }

    public String getLastSessionDate(int userID) {
        String sessionDate = "";
        try {
            sqLiteDatabase = this.getReadableDatabase();
            String sql = "SELECT max(session_date) from user_session where user_id = " + userID;
            cursor = sqLiteDatabase.rawQuery(sql, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
            } else {
                if (cursor.moveToFirst()) {
                    sessionDate = cursor.getString(0);
                }
            }
        } catch (SQLiteException ex) {
            Log.d("getLastSessionDate", " " + ex);
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return sessionDate;
    }

    public String getLastSessionDuration(int userID) {
        String time = "00:00";
        int minutes = 0;
        Duration timeElapsed;
        LocalTime session_start, session_end;
        try {
            sqLiteDatabase = this.getReadableDatabase();
            String sql = "select session_start, session_end from user_session where user_id= " + userID + " AND " +
                    "session_date= (select max(session_date) from user_session where user_id=" + userID + ")";
            cursor = sqLiteDatabase.rawQuery(sql, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
            } else {
                if (cursor.moveToFirst()) {
                    session_start = LocalTime.parse(cursor.getString(0));
                    session_end = LocalTime.parse(cursor.getString(1));
                    timeElapsed = Duration.between(session_start, session_end);
                    minutes += (int) timeElapsed.toMinutes();
                    time = convertMinuteToHourMinute(minutes);
                }
            }
        } catch (SQLiteException ex) {
            Log.d("getLastSessionDuration", " " + ex);
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return time;
    }

    public int getLastSessionID(int userID) {
        int sessionID = 0;
        try {
            sqLiteDatabase = this.getReadableDatabase();
            String whereClause = "session_id=(SELECT MAX(session_id) from user_session where session_id < (select max(session_id) from user_session)) and user_id=" + userID;
            cursor = sqLiteDatabase.query(Table_Sessions, Columns_SessionID, whereClause, null, null, null, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
            } else {
                if (cursor.moveToFirst()) {
                    sessionID = cursor.getInt(0);
                }
            }
        } catch (SQLiteException ex) {
            Log.d("getLastSessionID", " " + ex);
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return sessionID;
    }

    public String loadLastSession(int userID) {
        int lastSessionID = getLastSessionID(userID);
        StringBuilder lastSessionText = new StringBuilder();
        try {
            sqLiteDatabase = this.getReadableDatabase();
            String sql = "SELECT text  from statement where id in (select id from statement_session where session_id=" + lastSessionID + ")";
            cursor = sqLiteDatabase.rawQuery(sql, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
            } else {
                if (cursor.moveToFirst()) {
                    do {
                        lastSessionText.append(cursor.getString(0)).append(", ");//text
                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLiteException ex) {
            Log.d("loadLastSession", " " + ex);
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return lastSessionText.toString();
    }

    public String lastPerson(int userID) {//who was the last person to talk in the last session?
        String persona = "";
        int lastSessionID = getLastSessionID(userID); //get last session id for current user
        try {
            sqLiteDatabase = this.getReadableDatabase();
            String sql = "SELECT persona from statement where id = (select max(id) from statement_session where session_id=" + lastSessionID + ")";
            cursor = sqLiteDatabase.rawQuery(sql, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
            } else {
                if (cursor.moveToFirst()) {
                    persona = cursor.getString(0);
                }
            }
        } catch (SQLiteException ex) {
            Log.d("lastPerson", " " + ex);
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return persona;
    }

    public String lastStatement(int statementID) {
        String statement = "";
        try {
            sqLiteDatabase = this.getReadableDatabase();
            String[] selectedCols = new String[]{"text"};
            String whereClause = "id=" + statementID;
            cursor = sqLiteDatabase.query("Statement", selectedCols, whereClause, null, null, null, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
            } else {
                if (cursor.moveToFirst()) {
                    statement = cursor.getString(0) + ". ";
                }
            }
        } catch (SQLiteException ex) {
            Log.d("lastSentence", " " + ex);
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return statement;
    }

    public int getLastStatementID(String persona) {
        int lastStatementID = 0;
        try {
            if (!persona.isEmpty()) {
                sqLiteDatabase = this.getReadableDatabase();
                String sql = "SELECT MAX(id) from statement where persona='" + persona + "' ";
                cursor = sqLiteDatabase.rawQuery(sql, null);
                if (cursor.getCount() <= 0) {
                    cursor.close();
                } else {
                    if (cursor.moveToFirst()) {
                        lastStatementID = cursor.getInt(0);
                    }
                }
            }
        } catch (SQLiteException ex) {
            Log.d("getLastStatementID", " " + ex);
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return lastStatementID;
    }

    public void insertStatementSession(int session_id, int statement_id) {
        try {
            sqLiteDatabase = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Col_StatementID, statement_id);
            values.put(Col_SessionID, session_id);
            sqLiteDatabase.insert(Table_Statement_Session, null, values);
        } catch (SQLiteException ex) {
            Log.d("insertStatementSession", " " + ex);
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
    }
    //endregion

    //region Notes Operation
    public boolean addNote(int userID, Note note) {
        boolean flag = false;
        String noteText = note.getNoteText();
        String noteDate = globalVariables.currentDate;
        try {
            sqLiteDatabase = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(PK_UserID, userID);
            values.put(Col_NoteText, noteText);
            values.put(Col_NoteDate, noteDate);
            if (sqLiteDatabase.insert(Table_User_Notes, null, values) != -1) {
                flag = true;
            }
        } catch (SQLiteException ex) {
            Log.d("addNote", " " + ex);
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return flag;
    }

    public boolean updateNote(int userID, Note note) {
        boolean flag = false;
        String noteText = note.getNoteText();
        String noteDate = note.getNoteDate();
        String whereClause = PK_UserID + "=? AND " + Col_NoteDate + "=?";
        String[] whereArgs = new String[]{String.valueOf(userID), String.valueOf(noteDate)};
        try {
            sqLiteDatabase = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(PK_UserID, userID);
            values.put(Col_NoteText, noteText);
            values.put(Col_NoteDate, noteDate);
            if (sqLiteDatabase.update(Table_User_Notes, values, whereClause, whereArgs) != -1) {
                flag = true;
            }
        } catch (SQLiteException ex) {
            Log.d("updateNote", " " + ex);
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return flag;
    }

    public void delNote(int userID, Note note) {
        String noteDate = note.getNoteDate();
        String whereClause = PK_UserID + "=? AND " + Col_NoteDate + "=?";
        String[] whereArgs = new String[]{String.valueOf(userID), String.valueOf(noteDate)};
        try {
            sqLiteDatabase = getWritableDatabase();
            sqLiteDatabase.delete(Table_User_Notes, whereClause, whereArgs);
        } catch (SQLiteException ex) {
            Log.d("delNote", " " + ex);
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
    }

    public int getTotalOfJournaling(int userID) {
        int count = 0;
        try {
            sqLiteDatabase = this.getReadableDatabase();
            String sql = "SELECT count(*) from user_notes where user_id = " + userID;
            cursor = sqLiteDatabase.rawQuery(sql, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
            } else {
                if (cursor.moveToFirst()) {
                    count = cursor.getInt(0);
                }
            }
        } catch (SQLiteException ex) {
            Log.d("getTotalOfJournaling", String.valueOf(ex));
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return count;
    }

    public Note getLastNote(int userID) {
        Note note = new Note();
        try {
            sqLiteDatabase = getWritableDatabase();
            String whereClause = PK_UserID + "=? AND " + Col_NoteDate + "=?";
            String[] selectionArgs = {String.valueOf(userID), "SELECT MAX(note_date) FROM user_notes"};
            cursor = sqLiteDatabase.query(Table_User_Notes, Columns_Notes, whereClause, selectionArgs, null, null, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
            } else {
                if (cursor.moveToFirst()) {
                    do {
                        String noteText = cursor.getString(1);
                        String noteDate = cursor.getString(2);
                        note = new Note(noteText, noteDate);
                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLiteException ex) {
            Log.d("getLastNote", " " + ex);
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return note;
    }

    public List<Note> getNotes(int userID) {
        List<Note> noteList = new ArrayList<>();
        try {
            sqLiteDatabase = getWritableDatabase();
            String whereClause = PK_UserID + "=?  ";
            String[] selectionArgs = {String.valueOf(userID)};
            ContentValues values = new ContentValues();
            values.put(PK_UserID, userID);
            cursor = sqLiteDatabase.query(Table_User_Notes, Columns_Notes, whereClause, selectionArgs, null, null, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
            } else {
                if (cursor.moveToFirst()) {
                    do {
                        String noteText = cursor.getString(1);
                        String noteDate = cursor.getString(2);
                        noteList.add(new Note(noteText, noteDate));
                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLiteException ex) {
            Log.d("getNotes", " " + ex);
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return noteList;
    }

    //endregion

    //region Note Analysis
    public void insertNoteAnalysis(int userID, String summary, int prediction) {
        try {
            sqLiteDatabase = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(PK_UserID, userID);
            values.put(Col_NoteSummary, summary);
            values.put(Col_NoteScore, prediction);
            values.put(Col_NoteFlag, 0);//default to 0 means bot had not commented yet on user entry
            sqLiteDatabase.insert(Table_Notes_Analysis, null, values);
        } catch (SQLiteException ex) {
            Log.d("insertNoteAnalysis", " " + ex);
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
    }

    public ArrayList<String> getGratitudeNotes(int userID) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            sqLiteDatabase = this.getReadableDatabase();
            String sql = "SELECT note_summary from notes_analysis where user_id = " + userID + " AND (note_score=3 OR note_score=4) ";
            cursor = sqLiteDatabase.rawQuery(sql, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
            } else {
                if (cursor.moveToFirst()) {
                    do {
                        arrayList.add(cursor.getString(0));
                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLiteException ex) {
            Log.d("getGratitudeNotes", String.valueOf(ex));
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return arrayList;
    }

    public ArrayList<String> getResentmentNotes(int userID) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            sqLiteDatabase = this.getReadableDatabase();
            String sql = "SELECT note_summary from notes_analysis where user_id = " + userID + " AND (note_score=0 OR note_score=1) ";
            cursor = sqLiteDatabase.rawQuery(sql, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
            } else {
                if (cursor.moveToFirst()) {
                    do {
                        arrayList.add(cursor.getString(0));
                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLiteException ex) {
            Log.d("getResentmentNotes", String.valueOf(ex));
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return arrayList;
    }

    public int ifUserNotifyTimeIsNowGetPrediction(int userID) {
        int prediction = 0;
        //first get user preferred time for receiving msgs from Bot
        String selectedTime = getUserNotificationTime(globalVariables.userID);
        if (!selectedTime.isEmpty()) {
            //get all records with flag=0 from table Table_Notes_Analysis
            try {
                sqLiteDatabase = this.getReadableDatabase();
                String sql = "SELECT " + Col_NoteScore + " FROM " + Table_Notes_Analysis + " where " + PK_UserID + "=? AND " + Col_NoteFlag + "=?";
                String[] selectionArgs = new String[]{String.valueOf(userID), String.valueOf(0)};
                cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
                if (cursor.getCount() <= 0) {
                    cursor.close();
                } else {
                    if (cursor.moveToFirst()) {
                        do {
                            prediction = cursor.getInt(0);
                        } while (cursor.moveToNext());
                    }
                }
            } catch (SQLiteException ex) {
                Log.d("checkIfUserNotifyTimeIsNow", " " + ex);
            } finally {
                if (cursor != null && !cursor.isClosed()) cursor.close();
                if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
            }
        }
        return prediction;
    }


    public void updateNoteAnalysis(int userID) {//update Col_NoteFlag=1
        try {
            sqLiteDatabase = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Col_NoteFlag, 1);
            sqLiteDatabase.update(Table_Notes_Analysis, values, PK_UserID + "=?", new String[]{String.valueOf(userID)});
        } catch (SQLiteException ex) {
            Log.d("updateNoteAnalysis", " " + ex);
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
    }

    //endregion

    //region Rate&Feedback

    public long insertRateFeedback(int userID, String rate, String feedback) {
        long row_id = 0;
        try {
            sqLiteDatabase = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(PK_UserID, userID);
            values.put(Col_Rate, rate);
            values.put(Col_Feedback, feedback);
            row_id = sqLiteDatabase.insert(Table_Users, null, values);
        } catch (SQLiteException ex) {
            Log.d("addUser", " " + ex);
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return row_id;
    }

    public String getUserRate(int userID) {
        String rate = "";
        try {
            sqLiteDatabase = this.getReadableDatabase();
            String sql = "SELECT "+ Col_Rate +" from "+ Table_Rate_Feedback +" where user_id = " + userID;
            cursor = sqLiteDatabase.rawQuery(sql, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
            } else {
                if (cursor.moveToFirst()) {
                    rate = cursor.getString(0);
                }
            }
        } catch (SQLiteException ex) {
            Log.d("getUserRate", String.valueOf(ex));
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return rate;
    }

    public String getUserFeedback(int userID) {
        String feedback = "";
        try {
            sqLiteDatabase = this.getReadableDatabase();
            String sql = "SELECT "+ Col_Feedback +" from "+ Table_Rate_Feedback +" where user_id = " + userID;
            cursor = sqLiteDatabase.rawQuery(sql, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
            } else {
                if (cursor.moveToFirst()) {
                    feedback = cursor.getString(0);
                }
            }
        } catch (SQLiteException ex) {
            Log.d("getUserFeedback", String.valueOf(ex));
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) sqLiteDatabase.close();
        }
        return feedback;
    }

    //endregion


}
