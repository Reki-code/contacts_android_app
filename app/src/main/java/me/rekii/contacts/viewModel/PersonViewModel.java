package me.rekii.contacts.viewModel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.rekii.contacts.data.Person;
import me.rekii.contacts.data.PersonDao;

public class PersonViewModel extends ViewModel {
    private static volatile PersonViewModel INSTANCE = null;
    private MutableLiveData<List<Person>> persons;
    private PersonDao personDao;

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
            List<Person> freshUserList = personDao.getAll();
            persons.postValue(freshUserList);
        });
    }
}

