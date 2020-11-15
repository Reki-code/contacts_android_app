package me.rekii.contacts.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import me.rekii.contacts.R;
import me.rekii.contacts.data.PersonDao;
import me.rekii.contacts.data.User;
import me.rekii.contacts.data.UserDbHelper;
import me.rekii.contacts.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {
    public static final String TAG = "ProfileActivity";
    private static UserDbHelper dbHelper;
    private ActivityProfileBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = viewBinding.getRoot();
        setContentView(view);

        Toolbar toolbar = viewBinding.toolbar;
        setSupportActionBar(toolbar);
        dbHelper = new UserDbHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout_button) {
            logout();
        }
        return true;
    }

    private void logout() {
        // clear
        User.removeCurrUser(getApplicationContext());
        // back to login screen
        startLoginActivity();
        finish();
    }

    public void changePassword(View view) {
        if (verifyOldPassword()) {
            savePasswordToDatabase(getNewPassword());
            Toast.makeText(this, R.string.successful, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.password_incorrect, Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteUser(View view) {
        if (verifyOldPassword()) {
            deleteUserAndPeopleFromDatabase(User.getCurrUser(getApplicationContext()));
            Toast.makeText(this, R.string.successful, Toast.LENGTH_SHORT).show();
            logout();
        } else {
            Toast.makeText(this, R.string.password_incorrect, Toast.LENGTH_SHORT).show();
        }

    }

    private String getOldPassword() {
        return Objects.requireNonNull(viewBinding.oldPasswordText.getEditText()).getText().toString();
    }

    private String getNewPassword() {
        return Objects.requireNonNull(viewBinding.newPasswordText.getEditText()).getText().toString();
    }

    private boolean verifyOldPassword() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                BaseColumns._ID,
                User.UserEntry.COLUMN_NAME_USERNAME,
                User.UserEntry.COLUMN_NAME_PASSWORD
        };
        String selection = User.UserEntry.COLUMN_NAME_USERNAME + " = ?";
        String[] selectionArgs = {User.getCurrUser(getApplicationContext())};

        Cursor cursor = db.query(
                User.UserEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex(User.UserEntry.COLUMN_NAME_PASSWORD));
        cursor.close();

        String oldPassword = getOldPassword();
        return oldPassword.equals(password);
    }

    private void savePasswordToDatabase(String newPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(User.UserEntry.COLUMN_NAME_PASSWORD, newPassword);
        String whereClause = User.UserEntry.COLUMN_NAME_USERNAME + " = ?";
        String[] whereArgs = new String[]{User.getCurrUser(getApplicationContext())};
        db.update(User.UserEntry.TABLE_NAME, values, whereClause, whereArgs);
    }

    private void deleteUserAndPeopleFromDatabase(String name) {
        deleteUserFromDatabase(name);
        deletePeopleFromDatabase(name);
    }

    private void deleteUserFromDatabase(String name) {
        Log.i(TAG, "deleteUserFromDatabase: " + name);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String where = User.UserEntry.COLUMN_NAME_USERNAME + " = ?";
        String[] whereArgs = {name};
        db.delete(User.UserEntry.TABLE_NAME, where, whereArgs);
    }

    private void deletePeopleFromDatabase(String owner) {
        Log.i(TAG, "deletePeopleFromDatabase: owner:" + owner);
        PersonDao personDao = new PersonDao(getApplicationContext());
        personDao.deleteByOwner(owner);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}