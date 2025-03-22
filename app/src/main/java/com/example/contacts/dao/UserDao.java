package com.example.contacts.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contacts.models.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll(); // e uma consulta que seleciona todos os contatos getall

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE email LIKE :email AND " +
            "phone LIKE :phone LIMIT 1")
    User findByName(String email, String phone);
    @Query("SELECT * FROM user WHERE uid = :uid")
    User loadById(int uid);
    @Query("SELECT * FROM user WHERE isFavorite = 1")
    List<User> getFavorites();

    @Update
    void update(User user);
    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
}
