package com.example.contactapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton ajoutcontact;
    private ListView lv;
    private ArrayList<Contact> contacts=new ArrayList<>();
    private ContactDatabase contactDatabase;
    private FetchAsyncTask fetchAsyncTask;
    private SupprimerTousAsyncTask supprimerTousAsyncTask;
    private ChercherContactAsyncTask chercherContactAsyncTask;

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialiser l'action bar
        actionBar=getSupportActionBar();

        // initialisation
        contactDatabase = ContactDatabase.getInstance(this);
        fetchAsyncTask = new FetchAsyncTask(contactDatabase.contactDao());
        ajoutcontact = findViewById(R.id.ajoutcontact);
        chercherContactAsyncTask= new ChercherContactAsyncTask(contactDatabase.contactDao());
        // listener
        ajoutcontact.setOnClickListener(v -> {
          // se depalcer vers l'activité AjoutContact
            Intent intent = new Intent(MainActivity.this, AjoutModifContact.class);
            startActivity(intent);
        }
        );
        lv=findViewById(R.id.lv);
        // recuperer les contacts du telephone
        try {
            contacts= fetchAsyncTask.execute().get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // creer un adapter pour afficher les contacts dans
        ContactAdapter contactAdapter=new ContactAdapter
                (this,R.layout.contact_ligne,contacts);
        // on affecte l'adapter a notre liste view
        lv.setAdapter(contactAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main_top_menu,menu);

        // search view
        MenuItem item=menu.findItem(R.id.chercher);
        // search zone
        SearchView searchView=(SearchView) item.getActionView();
        // mettre un max valeur pour le width
        searchView.setMaxWidth(Integer.MAX_VALUE);
        // listener sur le search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // quand on tape sur le bouton chercher
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filtrer les contacts
                chercherContact(query);
                return true;
            }
            // quand on tape sur le bouton chercher
            @Override
            public boolean onQueryTextChange(String newText) {
                // filtrer les contacts
                chercherContact(newText);
                return true;
            }
        });

        return  true;
    }

    private void chercherContact(String newText)  {
        ArrayList<Contact>  contacts_filtrer=new ArrayList<>();
        for (Contact contact:contacts){
           if(contact.getNom().toLowerCase().contains(newText.toLowerCase())){
               contacts_filtrer.add(contact);
           }
       }
        // creer un adapter pour afficher les contacts dans
        ContactAdapter contactAdapter=new ContactAdapter
                (this,R.layout.contact_ligne,contacts_filtrer);
        // on affecte l'adapter a notre liste view
        lv.setAdapter(contactAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()){
            case R.id.supprimertout:
                // suppression de tous les contacts
                supprimerTousAsyncTask=new SupprimerTousAsyncTask(contactDatabase.contactDao());
                supprimerTousAsyncTask.execute();
                Toast.makeText(this, "Suppression reussie", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}