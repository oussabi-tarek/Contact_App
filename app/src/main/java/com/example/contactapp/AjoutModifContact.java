package com.example.contactapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.concurrent.ExecutionException;

public class AjoutModifContact extends AppCompatActivity {

    private ImageView profileimage;
    private EditText nompersonne,email,numero,note;
    private FloatingActionButton ajoutcontact;
    private String nompersonne1,email1,numero1,note1,profileimage1;

    // action bar
    private ActionBar actionBar;

    // constantes pour les permissions
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 200;
    private static final int IMAGE_FROM_GALLERY_CODE = 300;
    private static final int IMAGE_FROM_CAMERA_CODE = 400;

    // tableau de permissions
    private String[] cameraPermission;
    private String[] storagePermission;
    // uri de l'image
    private Uri image_uri;

    private ContactDatabase contactDatabase;
    private InsertionAsyncTask insertionAsyncTask;
    private String id;
    private ModifierAsyncTask modifierAsyncTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_modif_contact);

        // prendre les données du intent
        Intent intent = getIntent();
        id=intent.getStringExtra("contactId");



        // initialisation de la base de données
        contactDatabase = ContactDatabase.getInstance(this);
        insertionAsyncTask= new InsertionAsyncTask(contactDatabase.contactDao());


        // initialisation des permissions
        cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // initialisation de l'action bar
        actionBar = getSupportActionBar();
        // titre de l'action bar
        actionBar.setTitle("Ajouter un contact");
        // bouton retour
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


       // initialisation des variables
        profileimage = findViewById(R.id.profileimage);
        nompersonne = findViewById(R.id.nompersonne);
        email = findViewById(R.id.email);
        numero = findViewById(R.id.numero);
        note = findViewById(R.id.note);
        ajoutcontact = findViewById(R.id.ajoutcontact);
        Contact contact_to_modify = null;
        if(id!=null){
            // modification du titre de l'action bar
            actionBar.setTitle("Modifier le contact");
            // modification du bouton ajoutcontact
            ajoutcontact.setImageResource(R.drawable.baseline_mode_edit_24);
            // récupération des données du contact
            FetchParIdAsyncTask fetchParIdAsyncTask = new FetchParIdAsyncTask(contactDatabase.contactDao());

            try {
                contact_to_modify = fetchParIdAsyncTask.execute(Integer.parseInt(id)).get();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // initialisation des variables
            nompersonne.setText(contact_to_modify.getNom());
            email.setText(contact_to_modify.getEmail());
            numero.setText(contact_to_modify.getNumero());
            note.setText(contact_to_modify.getNote());
            profileimage.setImageURI(Uri.parse(contact_to_modify.getProfileimage()));
            image_uri=Uri.parse(contact_to_modify.getProfileimage());
        }

        // listener sur le bouton ajoutcontact
        Contact finalContact_to_modify = contact_to_modify;
        ajoutcontact.setOnClickListener(v -> {
            // enregistrement des données saisies
            if(id==null) {
                enregistrerDonnees();
            }
            else
                modifierContact(finalContact_to_modify);
        });
        profileimage.setOnClickListener(v -> {
            // affichage de la boite de dialogue pour choisir la source de l'image
            afficherBoiteDialogue();
        });



    }

    private void modifierContact(Contact contact) {
        // récupération des données saisies
        nompersonne1 = nompersonne.getText().toString().trim();
        email1 = email.getText().toString().trim();
        numero1 = numero.getText().toString().trim();
        note1 = note.getText().toString().trim();
            profileimage1 = image_uri.toString();
        contact.setNom(nompersonne1);
        contact.setEmail(email1);
        contact.setNumero(numero1);
        contact.setNote(note1);
        contact.setProfileimage(profileimage1);
        // vérification des données
        if (nompersonne1.isEmpty()) {
            // nompersonne vide
            Toast.makeText(this, "Veuillez saisir le nom de la personne", Toast.LENGTH_SHORT).show();
        } else if (email1.isEmpty()) {
            // email vide
            Toast.makeText(this, "Veuillez saisir l'email", Toast.LENGTH_SHORT).show();
        } else if (numero1.isEmpty()) {
            // numero vide
            Toast.makeText(this, "Veuillez saisir le numero", Toast.LENGTH_SHORT).show();
        } else if (note1.isEmpty()) {
            // note vide
            Toast.makeText(this, "Veuillez saisir la note", Toast.LENGTH_SHORT).show();
        } else {
            // toutes les données sont saisies
            // insertion dans la base de données
            modifierAsyncTask = new ModifierAsyncTask(contactDatabase.contactDao());
            modifierAsyncTask.execute(contact);
            Toast.makeText(this, "Contact modifié avec succès", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void afficherBoiteDialogue() {
        // options à afficher dans la boite de dialogue
        String[] options = {"Camera","Galerie"};
        // boite de dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // titre de la boite de dialogue
        builder.setTitle("Choisir une image depuis");
        // ajout des options
        builder.setItems(options, (dialog, which) -> {
            // gestion des options
            if (which == 0) {
                // option camera choisie
                if (!testCameraPermission()) {
                    // permissions camera non accordées
                    demandeCameraPermission();
                } else {
                    // permissions camera accordées
                    prendrePhoto();
                }
            } else if (which == 1) {
                // option galerie choisie
                if (!testStockagePermission()) {
                    // permissions stockage non accordées
                    demandeStockagePermission();
                } else {
                    // permissions stockage accordées
                    choisirImage();
                }
            }
        });
        // création et affichage de la boite de dialogue
        builder.create().show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_PERMISSION_CODE:
                if (grantResults.length >0){

                    // si toutes les permissions sont accordées
                    boolean cameraAccepte = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepte = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepte && storageAccepte){
                        // les 2 permissions sont accordées
                        prendrePhoto();
                    }else {
                        // les 2 permissions ne sont pas accordées
                        Toast.makeText(getApplicationContext(), "besoin du permission camera et stockage !!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case STORAGE_PERMISSION_CODE:
                if (grantResults.length >0){

                    // si toutes les permissions sont accordées
                    boolean storageAccepte = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepte){
                        // permission accordée
                        choisirImage();
                    }else {
                        // permission non accordée
                        Toast.makeText(getApplicationContext(), "besoin du stockage permission !!!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }

    }


    private void choisirImage() {
        // intent pour choisir une image depuis la galerie
        Intent intent = new Intent(Intent.ACTION_PICK);
        // type de données à choisir (image)
        intent.setType("image/*");
        // démarrer l'activité pour obtenir l'image choisie
        startActivityForResult(Intent.createChooser(intent,"Choisir une image"),IMAGE_FROM_GALLERY_CODE);
    }

    private void prendrePhoto() {
        // intent pour prendre une photo depuis la camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Nouvelle photo");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Description de la photo");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        // intent pour démarrer la camera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        // démarrer l'activité pour obtenir l'image prise
        startActivityForResult(intent,IMAGE_FROM_CAMERA_CODE);
    }

    public void enregistrerDonnees() {
        // récupération des données saisies
        profileimage1 = profileimage.toString();
        nompersonne1 = nompersonne.getText().toString();
        email1 = email.getText().toString();
        numero1 = numero.getText().toString();
        note1 = note.getText().toString();
        if (nompersonne1.isEmpty()) {
            // nompersonne vide
            Toast.makeText(this, "Veuillez saisir le nom de la personne", Toast.LENGTH_SHORT).show();
        } else if (email1.isEmpty()) {
            // email vide
            Toast.makeText(this, "Veuillez saisir l'email", Toast.LENGTH_SHORT).show();
        } else if (numero1.isEmpty()) {
            // numero vide
            Toast.makeText(this, "Veuillez saisir le numero", Toast.LENGTH_SHORT).show();
        } else if (note1.isEmpty()) {
            // note vide
            Toast.makeText(this, "Veuillez saisir la note", Toast.LENGTH_SHORT).show();
        }
        else{
            // enregistrement des données dans la base de données
            Contact contact = new Contact(image_uri.toString(),nompersonne1,numero1,email1,note1);
            insertionAsyncTask.execute(contact);
            // affichage d'un message de succès
            Toast.makeText(getApplicationContext(), "Contact: "+ nompersonne1 +" ajouté avec succès",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AjoutModifContact.this, MainActivity.class);
            startActivity(intent);
        }
    }

    // bouton retour
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    // test si les permissions du camera sont accordées
    private boolean testCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result & result1;
    }

    // demande de permission pour le camera
    private void demandeCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_PERMISSION_CODE); // handle request permission on override method
    }

    // test si les permissions du stockage sont accordées
    private boolean testStockagePermission(){
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result1;
    }

    // demande de permission pour le stockage
    private void demandeStockagePermission(){
        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_PERMISSION_CODE);
    }

    // gestion des résultats de l'activité pour obtenir l'image choisie
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == IMAGE_FROM_GALLERY_CODE){
                // avoir l'image choisie depuis la galerie
                // crop Image
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(AjoutModifContact.this);

            }else if (requestCode == IMAGE_FROM_CAMERA_CODE){
                // avoir l'image prise depuis la camera
                // crop Image
                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(AjoutModifContact.this);
            }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                // récupération de l'image cropée
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                image_uri = result.getUri();
                profileimage.setImageURI(image_uri);

            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                // erreur
                Toast.makeText(getApplicationContext(), "quelque chosese est incorrect!", Toast.LENGTH_SHORT).show();
            }
        }
    }



}