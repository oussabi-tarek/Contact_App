package com.example.contactapp;

import android.os.AsyncTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FetchAsyncTask extends AsyncTask<Void,Void,ArrayList<Contact>> {

                private ContactDao contactDao;

                public FetchAsyncTask(ContactDao contactDao) {
                    this.contactDao = contactDao;
                }

                @Override
                protected ArrayList<Contact> doInBackground(Void... voids) {
                    ArrayList<Contact> contacts =(ArrayList<Contact>) contactDao.getAll();
                    return contacts;
                }
}
