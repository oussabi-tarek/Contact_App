package com.example.contactapp;

import android.os.AsyncTask;

public class FetchParIdAsyncTask extends AsyncTask<Integer,Void,Contact> {

                private ContactDao contactDao;

                public FetchParIdAsyncTask(ContactDao contactDao) {
                    this.contactDao = contactDao;
                }

                @Override
                protected Contact doInBackground(Integer... integers) {

                    Contact contact = contactDao.getContactById(integers[0]);
                    return contact;
                }
}
