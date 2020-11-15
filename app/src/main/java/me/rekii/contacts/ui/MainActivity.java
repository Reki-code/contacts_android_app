package me.rekii.contacts.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
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
import me.rekii.contacts.viewModel.PersonViewModel;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

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
        String name = intent.getStringExtra(getString(R.string.user_data_preference_file_key));
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

    }

    private void setupViewModel() {
        personViewModel = new ViewModelProvider(
                getViewModelStore()
                , new PersonViewModel.PersonViewModelFactory(getApplicationContext()))
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

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    // SearchView.OnQueryTextListener
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        personViewModel.getFilter().filter(newText);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_search:
                return true;
            case R.id.app_bar_logout:
                startProfileActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void startProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}