package me.rekii.contacts.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import me.rekii.contacts.R;
import me.rekii.contacts.data.Person;
import me.rekii.contacts.databinding.ActivityAddPersonBinding;
import me.rekii.contacts.view.PersonViewModel;
import me.rekii.contacts.view.PersonViewModelFactory;

public class AddPersonActivity extends AppCompatActivity {

    private ActivityAddPersonBinding viewBinding;
    private PersonViewModel personViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityAddPersonBinding.inflate(getLayoutInflater());
        View view = viewBinding.getRoot();
        setContentView(view);

        Toolbar toolbar = viewBinding.toolbarAddPerson;
        setSupportActionBar(toolbar);

        personViewModel = new ViewModelProvider(
                getViewModelStore()
                , new PersonViewModelFactory(getApplicationContext()))
                .get(PersonViewModel.class);
    }

    public void onAvatarButtonClick(View view) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_person_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_button) {
            savePerson();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void savePerson() {
        personViewModel.insert(new Person(getName(), getPhone()));
        Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
    }

    private String getName() {
        return Objects.requireNonNull(viewBinding.nameText.getEditText()).getText().toString();
    }

    private String getPhone() {
        return Objects.requireNonNull(viewBinding.phoneText.getEditText()).getText().toString();
    }
}