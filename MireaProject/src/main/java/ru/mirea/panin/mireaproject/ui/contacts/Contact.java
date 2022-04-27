package ru.mirea.panin.mireaproject.ui.contacts;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Contact {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String number;
    public String name;
}
