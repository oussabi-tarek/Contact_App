package com.example.contactapp;

import android.os.AsyncTask;

import java.util.ArrayList;

public class ChercherContactAsyncTask extends AsyncTask<String,Void, ArrayList<Contact>> {
        private ContactDao contactDao;
        public ChercherContactAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }
        @Override
        protected ArrayList<Contact> doInBackground(String... strings) {
            return (ArrayList<Contact>) contactDao.chercherContact(strings[0]);
        }
}
