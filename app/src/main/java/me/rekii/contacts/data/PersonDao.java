package me.rekii.contacts.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import me.rekii.contacts.R;
import me.rekii.contacts.ui.LoginActivity;

import static android.content.Context.MODE_PRIVATE;

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
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.user_data_preference_file_key), MODE_PRIVATE);
        String currUser = preferences.getString(LoginActivity.EXTRA_USERNAME, "");
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

    public boolean insert(Person person) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Person.PersonEntry.COLUMN_NAME_OWNER, getCurrentUser());
        values.put(Person.PersonEntry.COLUMN_NAME_NAME, person.getName());
        values.put(Person.PersonEntry.COLUMN_NAME_PHONE, person.getPhone());

        db.insert(Person.PersonEntry.TABLE_NAME, null, values);
        return false;
    }

    private String getCurrentUser() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.user_data_preference_file_key), MODE_PRIVATE);
        String currUser = preferences.getString(LoginActivity.EXTRA_USERNAME, "");
        return currUser;
    }
}
