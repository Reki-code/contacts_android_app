package me.rekii.contacts.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class PersonDao {
    private static PersonDbHelper dbHelper;

    public PersonDao(Context context) {
        dbHelper = new PersonDbHelper(context);
    }

    public List<Person> getAll() {
        List<Person> persons = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {
                BaseColumns._ID,
                Person.PersonEntry.COLUMN_NAME_NAME,
                Person.PersonEntry.COLUMN_NAME_PHONE
        };

        Cursor cursor = db.query(
                Person.PersonEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        cursor.close();

        return persons;
    }
}
