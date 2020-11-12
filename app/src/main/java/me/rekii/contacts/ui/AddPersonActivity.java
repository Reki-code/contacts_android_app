package me.rekii.contacts.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import me.rekii.contacts.R;
import me.rekii.contacts.databinding.ActivityAddPersonBinding;

public class AddPersonActivity extends AppCompatActivity {

    private ActivityAddPersonBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityAddPersonBinding.inflate(getLayoutInflater());
        View view = viewBinding.getRoot();
        setContentView(view);

        Toolbar toolbar = viewBinding.toolbarAddPerson;
        setSupportActionBar(toolbar);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_button) {
            savePerson();
        }
        return super.onOptionsItemSelected(item);
    }

    private void savePerson() {

    }
}