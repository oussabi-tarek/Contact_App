package com.example.contactapp;

import android.os.AsyncTask;

public class InsertionAsyncTask extends AsyncTask<Contact,Void,Void> {
private ContactDao contactDao;
public InsertionAsyncTask(ContactDao contactDao) {
        this.contactDao = contactDao;
        }
@Override
protected Void doInBackground(Contact... contacts) {
        if(contacts[0].getProfileimage()==null){
        contacts[0].setProfileimage("");
        }
        contactDao.insertContact(contacts[0]);
        return null;
        }
}
