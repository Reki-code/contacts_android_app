package me.rekii.contacts.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.rekii.contacts.R;
import me.rekii.contacts.data.Person;

public class PersonAdapter extends ListAdapter<Person, PersonAdapter.PersonViewHolder> {
    public static final DiffUtil.ItemCallback<Person> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Person>() {
                @Override
                public boolean areItemsTheSame(@NonNull Person oldItem, @NonNull Person newItem) {
                    return oldItem.getName().equals(newItem.getName());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Person oldItem, @NonNull Person newItem) {
                    return oldItem.equals(newItem);
                }
            };
    private List<Person> mDataset;

    public PersonAdapter(List<Person> persons) {
        super(DIFF_CALLBACK);
        mDataset = persons;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_view, parent, false);

        return new PersonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        holder.bindTo(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        private View personView;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            personView = itemView;
        }

        public void bindTo(Person person) {
            TextView name = personView.findViewById(R.id.person_name);
            TextView phone = personView.findViewById(R.id.person_phone);
            name.setText(person.getName());
            phone.setText(person.getPhone());
        }
    }
}
