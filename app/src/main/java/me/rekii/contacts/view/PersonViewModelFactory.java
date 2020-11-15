package me.rekii.contacts.view;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class PersonViewModelFactory implements ViewModelProvider.Factory {
    private Context context;

    public PersonViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) PersonViewModel.getInstance(context);
    }
}
