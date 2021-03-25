package au.edu.utas.sakther.assignment2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import au.edu.utas.sakther.assignment2.model.JournalModel;
import au.edu.utas.sakther.assignment2.model.MoodModel;

import static android.content.ContentValues.TAG;

/**
 * Created by User on 6/5/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "mydb.db";
    private static String TABLE_NAME_JOURNAL_LIST = "journal_list";
    private static String TABLE_NAME_MOOD_LIST = "mood_list";

    private static String ID = "id";
    private static String Title = "title";
    private static String Body = "body";
    private static String Mood = "mood";
    private static String MoodValue = "mood_value";
    private static String Image = "image";
    private static String Time = "time";
    private static String Date = "date";
    private static String Location = "location";
    private static String Tag = "tag";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + TABLE_NAME_JOURNAL_LIST + " ( " + ID + " integer primary key autoincrement, " + Title + " text not null, " + Body + " text not null, " + Mood + " text, " + Image + " blob," + Date + " date," + Time + " text not null," + Location + " text," + Tag + " text)";
        db.execSQL(query);
       String query2 = "create table " + TABLE_NAME_MOOD_LIST + " ( " + ID + " integer primary key autoincrement, " + Mood + " text not null, " + MoodValue + " integer not null," + Date + " date," + Time + " text not null)";
        db.execSQL(query2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = " Drop table if exists " + TABLE_NAME_JOURNAL_LIST;
        db.execSQL(query);
        String query2 = " Drop table if exists " + TABLE_NAME_MOOD_LIST;
        db.execSQL(query2);
    }



    public int insertList(JournalModel data) {
        SQLiteDatabase sd = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Title, data.getTitle());
        values.put(Body, data.getBody());
        values.put(Mood, data.getMood());
        values.put(Image, data.getImage());
        values.put(Date, data.getDate());
        values.put(Time, data.getTime());
        values.put(Location, data.getLocation());
        values.put(Tag, data.getTag());

        long id = sd.insert(TABLE_NAME_JOURNAL_LIST, null, values);
        sd.close();
        Log.d(TAG, String.valueOf(id));
        return (int) id;
    }

    public int updateList(JournalModel data) {
        SQLiteDatabase sd = getWritableDatabase();
        ContentValues values = new ContentValues();

        int id=data.getId();

        values.put(Title, data.getTitle());
        values.put(Body, data.getBody());
        values.put(Mood, data.getMood());
        values.put(Image, data.getImage());
        values.put(Date, data.getDate());
        values.put(Time, data.getTime());
        values.put(Location, data.getLocation());
        values.put(Tag, data.getTag());

        long code = sd.update(TABLE_NAME_JOURNAL_LIST, values,"id=" + id, null );
        sd.close();
        Log.d(TAG, String.valueOf(code));
        return (int) code;
    }

    public ArrayList<JournalModel> showJournalList() {
        SQLiteDatabase sd = getReadableDatabase();
        String query = "Select * from " + TABLE_NAME_JOURNAL_LIST + "  order by "+ID+" desc ";
        Cursor cur = sd.rawQuery(query, null);
        ArrayList<JournalModel> data = new ArrayList<>();

        cur.moveToFirst();

        if (cur.moveToFirst()) {
            do {
                int id = cur.getInt(0);
                String title = cur.getString(1);
                String body = cur.getString(2);
                String mood = cur.getString(3);
                byte[] image = cur.getBlob(4);
                String date = cur.getString(5);
                String time = cur.getString(6);
                String location = cur.getString(7);
                String tag = cur.getString(8);
                data.add(new JournalModel(id,title,body,mood,image,date,time,location,tag));

            } while (cur.moveToNext());
        }
        cur.close();
        return data;
    }

    public  byte[] getByte(int id) {
        SQLiteDatabase sd = getReadableDatabase();
        String query = "Select image from " + TABLE_NAME_JOURNAL_LIST + " where  " + ID + " = '" + id + "' " ;
        Cursor cur = sd.rawQuery(query, null);
        byte[] image = new byte[0];

        cur.moveToFirst();

        if (cur.moveToFirst()) {
            do {
                image = cur.getBlob(0);

            } while (cur.moveToNext());
        }
        cur.close();

        Log.d("check", "getByte");

        return image;
    }

    public void deleteJournal(int id) {
        SQLiteDatabase sd = getWritableDatabase();
        String query = " Delete from " + TABLE_NAME_JOURNAL_LIST + " where " + ID + " = '" + id + "'";
       sd.execSQL(query);
        sd.close();
    }

    public int insertMood(MoodModel data) {
        SQLiteDatabase sd = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Mood, data.getMood());
        values.put(MoodValue, data.getMoodValue());
        values.put(Date, data.getDate());
        values.put(Time, data.getTime());

        long id = sd.insert(TABLE_NAME_MOOD_LIST,null, values );
        sd.close();
        Log.d(TAG, String.valueOf(id));
        return (int) id;
    }

    public int updateMood(MoodModel data) {
        SQLiteDatabase sd = getWritableDatabase();
        ContentValues values = new ContentValues();

        int id=data.getId();

        values.put(Mood, data.getMood());
        values.put(Date, data.getDate());
        values.put(Time, data.getTime());

        long code = sd.update(TABLE_NAME_MOOD_LIST, values,"id=" + id, null );
        sd.close();
        Log.d(TAG, String.valueOf(code));
        return (int) code;
    }

    public ArrayList<MoodModel> showMoodList() {
        SQLiteDatabase sd = getReadableDatabase();
        String query = "Select * from " + TABLE_NAME_MOOD_LIST + "  order by "+ID+" desc ";
        Cursor cur = sd.rawQuery(query, null);
        ArrayList<MoodModel> data = new ArrayList<>();

        cur.moveToFirst();

        if (cur.moveToFirst()) {
            do {
                int id = cur.getInt(0);
                String mood = cur.getString(1);
                int moodValue = cur.getInt(2);
                String date = cur.getString(3);
                String time = cur.getString(4);

                data.add(new MoodModel(id,mood,moodValue,date,time));

            } while (cur.moveToNext());
        }
        cur.close();
        return data;
    }

    public void deleteMood(int id) {
        SQLiteDatabase sd = getWritableDatabase();
        String query = " Delete from " + TABLE_NAME_MOOD_LIST + " where " + ID + " = '" + id + "'";
        sd.execSQL(query);
        sd.close();
    }



    public ArrayList<MoodModel> getMoodInMonth(String month) {
        SQLiteDatabase sd = getReadableDatabase();
        String query = "Select * from " + TABLE_NAME_MOOD_LIST + " where date like '%"+month+"%'  ";
        Cursor cur = sd.rawQuery(query, null);

        ArrayList<MoodModel> data = new ArrayList<>();

        cur.moveToFirst();

        if (cur.moveToFirst()) {
            do {
                int id = cur.getInt(0);
                String mood = cur.getString(1);
                int moodValue = cur.getInt(2);
                String date = cur.getString(3);
                String time = cur.getString(4);

                data.add(new MoodModel(id,mood,moodValue,date,time));

            } while (cur.moveToNext());
        }
        cur.close();
        return data;
    }

    public int getMoodCount(String mood,String month) {
        SQLiteDatabase sd = getReadableDatabase();
        String query = "Select count(id) from " + TABLE_NAME_MOOD_LIST + " where  " + Mood + " = '" + mood + "' and " + Date + " like '%"+month+"%' " ;
        Cursor cur = sd.rawQuery(query, null);
        int count = 0;

        cur.moveToFirst();

        if (cur.moveToFirst()) {
            do {
               count = cur.getInt(0);

            } while (cur.moveToNext());
        }
        cur.close();

        Log.d("check", "getMoodCount: "+count);

        return count;
    }




}
