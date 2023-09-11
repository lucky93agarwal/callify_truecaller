package com.gpslab.kaun.Contact;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class ImportContactsAsync extends AsyncTask<Void, Void, Void>
{
    private Activity activity;
    private ICallback client;

    @Expose
    private ArrayList<ContactNew> contacts;

    public ImportContactsAsync(Activity activity, ICallback client)
    {
        this.activity = activity;
        this.client = client;
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        client.mobileContacts(contacts);
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        try
        {
            ImportContacts importContacts = new ImportContacts(activity);
            contacts = importContacts.getContacts();
            String str = "";
        }
        catch(Exception e)
        {
            Log.e(Utilities.TAG_LIB, e.getMessage());
        }

        return null;
    }


    public interface ICallback
    {
        void mobileContacts(ArrayList<ContactNew> contactList);
    }

}


