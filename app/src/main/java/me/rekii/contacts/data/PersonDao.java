package me.rekii.contacts.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class PersonDao {
    private static PersonDbHelper dbHelper;
    private Context context;

    public PersonDao(Context context) {
        this.context = context;
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
        String selection = Person.PersonEntry.COLUMN_NAME_OWNER + " = ?";
        String currUser = getCurrentUser();
        String[] selectionArgs = {currUser};

        String sortOrder = Person.PersonEntry.COLUMN_NAME_NAME + " DESC";

        Cursor cursor = db.query(
                Person.PersonEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        while (cursor.moveToNext()) {
            String name = cursor.getString(
                    cursor.getColumnIndex(Person.PersonEntry.COLUMN_NAME_NAME)
            );
            String phone = cursor.getString(
                    cursor.getColumnIndex(Person.PersonEntry.COLUMN_NAME_PHONE)
            );
            persons.add(new Person(name, phone));
        }
        cursor.close();

        return persons;
    }

    public Person getPersonByName(String name) {
        return null;
    }

    public boolean insert(Person person) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Person.PersonEntry.COLUMN_NAME_OWNER, getCurrentUser());
        values.put(Person.PersonEntry.COLUMN_NAME_NAME, person.getName());
        values.put(Person.PersonEntry.COLUMN_NAME_PHONE, person.getPhone());

        long row = db.insert(Person.PersonEntry.TABLE_NAME, null, values);
        return row != -1;
    }

    public boolean delete(Person person) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String where = Person.PersonEntry.COLUMN_NAME_NAME + " = ?";
        String[] whereArgs = {person.getName()};
        int row = db.delete(Person.PersonEntry.TABLE_NAME, where, whereArgs);
        return row == 1;
    }

    public void deleteByOwner(String owner) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String where = Person.PersonEntry.COLUMN_NAME_NAME + " = ?";
        String[] whereArgs = {owner};
        db.delete(Person.PersonEntry.TABLE_NAME, where, whereArgs);
    }

    private String getCurrentUser() {
        return User.getCurrUser(context);
    }
}
