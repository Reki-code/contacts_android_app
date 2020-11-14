package me.rekii.contacts.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import me.rekii.contacts.R;
import me.rekii.contacts.data.Person;
import me.rekii.contacts.data.PersonDao;

import static me.rekii.contacts.ui.LoginActivity.EXTRA_USERNAME;

public class MainActivity extends AppCompatActivity {

    private me.rekii.contacts.databinding.ActivityMainBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = me.rekii.contacts.databinding.ActivityMainBinding.inflate(getLayoutInflater());
        View view = viewBinding.getRoot();
        setContentView(view);

        Toolbar toolbar = viewBinding.toolbar;
        setSupportActionBar(toolbar);

        setupRecycler();
        setupFab();

        Intent intent = getIntent();
        String name = intent.getStringExtra(EXTRA_USERNAME);
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

        List<Person> persons = new PersonDao(this).getAll();
        if (persons.isEmpty()) {
            viewBinding.emptyHint.setText(R.string.empty_contacts);
        } else {
            viewBinding.emptyHint.setText("");
        }

    }

    private void setupFab() {
        FloatingActionButton fab = viewBinding.addPersonButton;
        fab.setOnClickListener(view -> {
            startAddPersonActivity();
        });
    }

    private void setupRecycler() {
        PersonDao personDao = new PersonDao(this);
        RecyclerView recyclerView = viewBinding.recyclerView;
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter<PersonAdapter.PersonViewHolder> mAdapter = new PersonAdapter(personDao.getAll());
        recyclerView.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_search:
                return true;
            case R.id.app_bar_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        // clear sp
        SharedPreferences preferences = getSharedPreferences(getString(R.string.user_data_preference_file_key), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(EXTRA_USERNAME);
        editor.apply();
        // back to login screen
        startLoginActivity();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void startAddPersonActivity() {
        Intent intent = new Intent(this, AddPersonActivity.class);
        startActivity(intent);
    }
}