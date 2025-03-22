package com.example.contacts.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.contacts.dao.UserDao;
import com.example.contacts.models.User;

@Database(entities = {User.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}