package me.rekii.contacts.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import me.rekii.contacts.R;
import me.rekii.contacts.data.Person;
import me.rekii.contacts.databinding.ActivityDetailsBinding;
import me.rekii.contacts.viewModel.PersonViewModel;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding viewBinding;
    private PersonViewModel personViewModel;
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = viewBinding.getRoot();
        setContentView(view);

        setupToolbar();

        setupContent();

        personViewModel = new ViewModelProvider(
                getViewModelStore()
                , new PersonViewModel.PersonViewModelFactory(getApplicationContext()))
                .get(PersonViewModel.class);
    }

    private void setupContent() {
        Intent intent = getIntent();
        person = intent.getParcelableExtra(getString(R.string.extra_key_person));
        assert person != null;
        viewBinding.nameText.setText(person.getName());
        viewBinding.phoneText.setText(person.getPhone());
    }

    private void setupToolbar() {
        Toolbar toolbar = viewBinding.toolbar2;
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_button) {
            deletePerson();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deletePerson() {
        if (personViewModel.delete(person)) {
            Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }
}