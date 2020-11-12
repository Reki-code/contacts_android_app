package me.rekii.contacts.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PersonDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Person.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Person.PersonEntry.TABLE_NAME + " (" +
                    Person.PersonEntry._ID + " INTEGER PRIMARY KEY," +
                    Person.PersonEntry.COLUMN_NAME_NAME + " TEXT," +
                    Person.PersonEntry.COLUMN_NAME_PHONE + " TEXT," +
                    Person.PersonEntry.COLUMN_NAME_OWNER + " TEXT," +
                    Person.PersonEntry.COLUMN_NAME_AVATAR + " BLOB)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Person.PersonEntry.TABLE_NAME;

    public PersonDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
