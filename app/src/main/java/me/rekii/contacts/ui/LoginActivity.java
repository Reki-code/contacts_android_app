package me.rekii.contacts.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import me.rekii.contacts.R;
import me.rekii.contacts.data.User;
import me.rekii.contacts.data.UserDbHelper;
import me.rekii.contacts.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "me.rekii.EXTRA_USERNAME";
    private static UserDbHelper dbHelper;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SharedPreferences preferences = getSharedPreferences(getString(R.string.user_data_preference_file_key), MODE_PRIVATE);
        String currUser = preferences.getString(EXTRA_USERNAME, "");

        if (!currUser.isEmpty()) {
            startMainActivity(currUser);
        }

        dbHelper = new UserDbHelper(this);
    }

    // 登陆按钮处理
    public void login(View view) {
        if (getUsername().isEmpty()) {
            binding.usernameText.setError("不能为空");
            return;
        }
        if (getPassword().isEmpty()) {
            binding.passwordText.setError("不能为空");
            return;
        }
        if (validate()) {
            // save username
            SharedPreferences preferences = getSharedPreferences(getString(R.string.user_data_preference_file_key), MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(EXTRA_USERNAME, getUsername());
            editor.apply();
            Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
            startMainActivity(getUsername());
        } else {
            Toast.makeText(this, "登陆失败", Toast.LENGTH_SHORT).show();
        }

    }

    private String getUsername() {
        TextInputLayout username_input = binding.usernameText;
        return Objects.requireNonNull(username_input.getEditText()).getText().toString();
    }

    private String getPassword() {
        TextInputLayout password_input = binding.passwordText;
        return Objects.requireNonNull(password_input.getEditText()).getText().toString();
    }

    private void startMainActivity(String name) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(LoginActivity.EXTRA_USERNAME, getUsername());
        startActivity(intent);
        finish();
    }

    private boolean validate() {
        boolean valid = false;

        String username = getUsername();
        String password = getPassword();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                BaseColumns._ID,
                User.UserEntry.COLUMN_NAME_USERNAME,
                User.UserEntry.COLUMN_NAME_PASSWORD
        };

        Cursor cursor = db.query(
                User.UserEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );
        boolean find = false;
        while (cursor.moveToNext()) {

            String name = cursor.getString(
                    cursor.getColumnIndex(User.UserEntry.COLUMN_NAME_USERNAME));
            if (username.equals(name)) {
                // find user
                find = true;
                break;
            }
        }
        if (find) {
            String pass = cursor.getString(
                    cursor.getColumnIndex(User.UserEntry.COLUMN_NAME_PASSWORD));
            if (password.equals(pass)) {
                valid = true;
            }
        } else { // not found
            addUser(username, password);
            valid = true;
        }
        cursor.close();
        return valid;
    }

    private void addUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(User.UserEntry.COLUMN_NAME_USERNAME, username);
        values.put(User.UserEntry.COLUMN_NAME_PASSWORD, password);

        db.insert(User.UserEntry.TABLE_NAME, null, values);
    }
}