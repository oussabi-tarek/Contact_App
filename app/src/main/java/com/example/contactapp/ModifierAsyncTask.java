package com.example.contactapp;

import android.os.AsyncTask;

public class ModifierAsyncTask extends AsyncTask<Contact,Void,Void> {
    private ContactDao contactDao;
    public ModifierAsyncTask(ContactDao contactDao) {
        this.contactDao = contactDao;
    }
    @Override
    protected Void doInBackground(Contact... contacts) {
        contactDao.updateContact(contacts[0]);
        return null;
    }
}
