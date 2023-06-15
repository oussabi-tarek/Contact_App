package com.example.contactapp;



import static androidx.core.app.ActivityCompat.requestPermissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

// la classe ContactAdapter hérite de la classe
// ArrayAdapter et utilisé avec notre ListView pour
// afficher la liste des contacts en utilisant
// notre vue personnalisée définie
// dans le fichier xml activity_contact_text, qui
// contient  l'image ou bien icone, le nom et le numéro.

public class ContactAdapter  extends ArrayAdapter<Contact> {
    // le contexte de l'application (l'activité
    // qui va afficher la liste des contacts)
    // dans ce cas c'est ContactList
    private static final int REQUEST_PERMISSIONS = 100;
    private Context context;
    // le layout qui va afficher les données
    // de chaque contact (l'image, le nom et le numéro)
    // dans ce cas c'est activity_contact_text
    private int resource;
    // notre constructeur qui prend en parametre
    // le contexte, le layout et la liste des contacts
    public ContactAdapter(@NonNull Context context, int resource,
                          @NonNull ArrayList<Contact> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }
    // la méthode getView() est appelée pour chaque
    // élément de la liste et elle retourne la vue
    // qui sera affichée à l'utilisateur avec
    // les données de l'élément correspondant.
    // donc on modifie les 2 elements de
    // la vue (l'image et le text) avec les
    // données de notre Contact
    @NonNull
    @Override
    public View getView(int position,
                        @NonNull View convertView,
                        @NonNull ViewGroup parent){
        // on recupere le layout inflater pour
        // pouvoir modifier les elements de la vue
        LayoutInflater layoutInflater=
                LayoutInflater.from(context);

        // on recupere la vue qui va afficher
        // les données de notre contact
        convertView=layoutInflater
                .inflate(resource,parent,false);
        // on recupere l image view de
        // notre vue(activity_contact_text) pour la modifier
        ImageView imageView=convertView
                .findViewById(R.id.contactimage);
        // on recupere le text view de notre
        // vue(activity_contact_text) pour le modifier
        // on a 2 text view dans notre vue
        // donc on recupere les 2
        // le premier pour le nom et le
        // deuxieme pour le numéro
        TextView txtNom=convertView.findViewById(R.id.contactnom);
        // TextView numero=convertView.findViewById(R.id.numero);
        // on change l'image de notre vue par
        // l'icone qu'on a dans le dossier drawable
        // imageView.setImageResource(R.drawable.baseline_person_24);
        // on change le nom et le numéro de notre
        // vue par ceux de notre contact

        txtNom.setText(getItem(position).getNom());
        String image = getItem(position).getProfileimage();
        if(image.equals("")) {
            imageView.setImageResource(R.drawable.baseline_person_24);
        }
        else {
            imageView.setImageURI(Uri.parse(getItem(position).getProfileimage()));
        }
        // numero.setText(getItem(position).getNumero());

        convertView.findViewById(R.id.contacttelephone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tester sur la permission CALL_PHONE
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, Manifest.permission.CALL_PHONE)) {
                        // Afficher une explication à l'utilisateur si nécessaire
                    } else {
                        requestPermissions((Activity) context,new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSIONS);
                    }
                }
            else {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+Uri.parse(getItem(position).getNumero())));
                context.startActivity(intent);
            }
        };
        });
        // listener click sur le composant
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(context,Detailsducontact.class);
               intent.putExtra("contactId",(getItem(position).getId())+"");
               context.startActivity(intent);
            }
        });

        // on retourne la vue qui va afficher les
        // donnees de notre contact
        return convertView;
    }






}
