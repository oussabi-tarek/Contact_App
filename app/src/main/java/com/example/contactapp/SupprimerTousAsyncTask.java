package com.example.contactapp;

import android.os.AsyncTask;

public class SupprimerTousAsyncTask extends AsyncTask<Void,Void,Void> {
        private ContactDao contactDao;
        public SupprimerTousAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            contactDao.deleteAll();
            return null;
        }
}
