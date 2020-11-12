package me.rekii.contacts.data;

import android.provider.BaseColumns;

import java.util.Objects;

public class Person {
    private String name;
    private String phone;

    public Person(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    private Person() {
    }

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

    public static class PersonEntry implements BaseColumns {
        public static final String TABLE_NAME = "person";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_AVATAR = "avatar";
        public static final String COLUMN_NAME_OWNER = "owner";
    }
}