package me.rekii.contacts.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import me.rekii.contacts.R;
import me.rekii.contacts.data.Person;
import me.rekii.contacts.view.PersonViewModel;
import me.rekii.contacts.view.PersonViewModelFactory;

import static me.rekii.contacts.ui.LoginActivity.EXTRA_USERNAME;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    private me.rekii.contacts.databinding.ActivityMainBinding viewBinding;
    private PersonViewModel personViewModel;
    private PersonAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = me.rekii.contacts.databinding.ActivityMainBinding.inflate(getLayoutInflater());
        View view = viewBinding.getRoot();
        setContentView(view);

        Toolbar toolbar = viewBinding.toolbar;
        setSupportActionBar(toolbar);

        setupViewModel();
        setupRecycler();
        setupFab();

        Intent intent = getIntent();
        String name = intent.getStringExtra(EXTRA_USERNAME);
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

    }

    private void setupViewModel() {
        personViewModel = new ViewModelProvider(
                getViewModelStore()
                , new PersonViewModelFactory(getApplicationContext()))
                .get(PersonViewModel.class);
    }

    private void setupFab() {
        FloatingActionButton fab = viewBinding.addPersonButton;
        fab.setOnClickListener(view -> {
            startAddPersonActivity();
        });
    }

    private void setupRecycler() {
        RecyclerView recyclerView = viewBinding.recyclerView;
        // recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new PersonAdapter();
        personViewModel.getPersons().observe(this, list -> {
            Log.i(TAG, "setupRecycler: update" + list);
            mAdapter.submitList(null);
            mAdapter.submitList(list);
            if (list.isEmpty()) {
                viewBinding.emptyHint.setText(R.string.empty_contacts);
            } else {
                viewBinding.emptyHint.setText("");
            }
        });
        recyclerView.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener((parent, view, position, id) -> {
                    // display details
                    startDetailsActivity(mAdapter.getPerson(position));
                })
                .setOnItemLongClickListener((parent, view, position, id) -> {
                    Toast.makeText(this, "Long click", Toast.LENGTH_SHORT).show();
                    return true;
                });
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
        finish();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void startAddPersonActivity() {
        Intent intent = new Intent(this, AddPersonActivity.class);
        startActivity(intent);
    }

    private void startDetailsActivity(Person person) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(getString(R.string.extra_key_person), person);
        startActivity(intent);
    }
}