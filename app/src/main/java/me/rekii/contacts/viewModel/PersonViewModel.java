package me.rekii.contacts.viewModel;

import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import me.rekii.contacts.data.Person;
import me.rekii.contacts.data.PersonDao;

public class PersonViewModel extends ViewModel implements Filterable {
    private static volatile PersonViewModel INSTANCE = null;
    private List<Person> personListFull;
    private MutableLiveData<List<Person>> persons;
    private PersonDao personDao;

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Person> filteredList;

            if (constraint == null || constraint.length() == 0) {
                filteredList = new ArrayList<>(personListFull);
            } else {
                String filterPattern = constraint.toString().trim();
                filteredList = personListFull.stream()
                        .filter(person -> person.getName().contains(filterPattern)
                                || person.getPhone().contains(filterPattern))
                        .collect(Collectors.toList());
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            persons.setValue((List<Person>) results.values);
        }
    };

    private PersonViewModel(Context context) {
        personDao = new PersonDao(context);
    }

    public static PersonViewModel getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (PersonViewModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PersonViewModel(context);
                }
            }
        }
        return INSTANCE;
    }

    public MutableLiveData<List<Person>> getPersons() {
        if (persons == null) {
            persons = new MutableLiveData<>();
            getPersonsFromDataBase();
        }
        return persons;
    }

    public boolean insert(Person person) {
        boolean success = personDao.insert(person);
        if (success) {
            getPersonsFromDataBase();
        }
        return success;
    }

    public boolean delete(Person person) {
        boolean success = personDao.delete(person);
        if (success) {
            getPersonsFromDataBase();
        }
        return success;
    }

    private void getPersonsFromDataBase() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(() -> {
            personListFull = personDao.getAll();
            persons.postValue(personListFull);
        });
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public static class PersonViewModelFactory implements ViewModelProvider.Factory {
        private Context context;

        public PersonViewModelFactory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) getInstance(context);
        }
    }
}

