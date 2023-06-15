package com.example.contactapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.ExecutionException;

public class Detailsducontact extends AppCompatActivity {

    private TextView contactnom,note,numero,email;
    private ImageView imagecontact;
    private String id;
    private ContactDatabase contactDatabase;
    private FetchParIdAsyncTask fetchParIdAsyncTask;

    private Contact contact;

    private FloatingActionButton modifbutton,supprimebutton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailsducontact);
        // prendre les données du intent
        Intent intent=getIntent();
        id=intent.getStringExtra("contactId");
        System.out.println("id du contact est "+id);

        // initialiser la base de données et la tache asynchrone
        contactDatabase=ContactDatabase.getInstance(this);
        fetchParIdAsyncTask=new FetchParIdAsyncTask(contactDatabase.contactDao());

        // initialiser les composants
        contactnom=findViewById(R.id.contactnom);
        note=findViewById(R.id.note);
        numero=findViewById(R.id.numero);
        email=findViewById(R.id.email);
        imagecontact=findViewById(R.id.contactimage);
        modifbutton=findViewById(R.id.modfiercontact);
        supprimebutton=findViewById(R.id.supprimercontact);
        try {
            fetchDonneesParId();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // lsitener sur mdofier button
        modifbutton.setOnClickListener(v -> {
            Intent intent1=new Intent(Detailsducontact.this,AjoutModifContact.class);
            intent1.putExtra("contactId",id);
            startActivity(intent1);
        });
        // listener sur supprimer button
        supprimebutton.setOnClickListener(v -> {
            SupprimerAsyncTask supprimerAsyncTask=new SupprimerAsyncTask(contactDatabase.contactDao());
            supprimerAsyncTask.execute(contact);
            Intent intent1=new Intent(Detailsducontact.this,MainActivity.class);
            startActivity(intent1);
            Toast.makeText(this, "Contact supprime avec succes", Toast.LENGTH_SHORT).show();
        });
    }

    @SuppressLint("SuspiciousIndentation")
    public void fetchDonneesParId() throws ExecutionException, InterruptedException {
     contact=fetchParIdAsyncTask.execute(Integer.parseInt(id)).get();
        contactnom.setText(contact.getNom());
        note.setText(contact.getNote());
        numero.setText(contact.getNumero());
        email.setText(contact.getEmail());
        if(contact.getProfileimage().equals("")){
            imagecontact.setImageResource(R.drawable.baseline_person_24);
        }
        else
            imagecontact.setImageURI(Uri.parse(contact.getProfileimage()));
    }



}