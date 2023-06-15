package com.example.contactapp;

import android.os.AsyncTask;

public class SupprimerAsyncTask extends AsyncTask<Contact,Void,Void> {
        private ContactDao contactDao;
        public SupprimerAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }
        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDao.deleteContact(contacts[0]);
            return null;
        }
}
