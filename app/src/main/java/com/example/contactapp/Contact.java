package com.example.contactapp;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contact")
public class Contact {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "profileimage")
    private String profileimage;
    @ColumnInfo(name = "nom")
    private String nom;
    @ColumnInfo(name = "numero")
    private String numero;
    @ColumnInfo(name = "email")
    private String email;
    @ColumnInfo(name = "note")
    private String note;

    public Contact(String profileimage,String nom, String numero, String email, String note){
        this.nom = nom;
        this.profileimage = profileimage;
        this.numero = numero;
        this.email = email;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public String getNom() {
        return nom;
    }

    public String getNumero() {
        return numero;
    }

    public String getEmail() {
        return email;
    }

    public String getNote() {
        return note;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
