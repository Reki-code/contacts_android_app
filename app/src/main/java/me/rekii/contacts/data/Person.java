package me.rekii.contacts.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Person implements Parcelable {
    private String name;
    private String phone;

    public Person(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public static final Parcelable.Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return name.equals(person.name) &&
                phone.equals(person.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone);
    }

    public Person(Parcel source) {
        this.name = source.readString();
        this.phone = source.readString();
    }

    @NonNull
    @Override
    public String toString() {
        return "Person {" +
                "name" + getName() + ", " +
                "phone" + getPhone() + "｝";
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getName());
        dest.writeString(getPhone());
    }

    public static class PersonEntry implements BaseColumns {
        public static final String TABLE_NAME = "person";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_AVATAR = "avatar";
        public static final String COLUMN_NAME_OWNER = "owner";
    }
}