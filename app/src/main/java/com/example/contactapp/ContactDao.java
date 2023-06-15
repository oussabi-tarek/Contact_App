package com.example.contactapp;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Dao
public interface ContactDao {
 @Query("SELECT * FROM contact")
 List<Contact> getAll();
 @Insert
    void insertContact(Contact contact);
 @Update
    void updateContact(Contact contact);
 @Delete
    void deleteContact(Contact contact);
 @Query("SELECT * FROM contact WHERE id=:id")
    Contact getContactById(int id);
@Query("DELETE FROM contact")
    void deleteAll();
 @Query("SELECT * FROM contact WHERE nom LIKE :nom")
    List<Contact> chercherContact(String nom);
}
