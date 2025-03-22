package com.example.contacts.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "fullname")
    public String fullname;

    @ColumnInfo(name = "email")
    public String email;
    @ColumnInfo(name = "phone")
    public String phone;
    @ColumnInfo(name = "isFavorite")
    public boolean isFavorite;
    @ColumnInfo(name = "photoPath")
    public String photoPath;
}
