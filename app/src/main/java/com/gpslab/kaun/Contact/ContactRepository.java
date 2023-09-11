package com.gpslab.kaun.Contact;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ContactRepository {

    private Context context;
    private List<NewContact> contacts;

    public ContactRepository(Context context) {
        this.context = context;
        contacts = new ArrayList<>();
    }

    public List<NewContact> fetchContacts() {
        NewContact contact;
        //hold a list of Contacts without duplicates
        Map<String, NewContact> cleanList = new LinkedHashMap<String, NewContact>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if ((cursor != null ? cursor.getCount() : 0) > 0) {
            while (cursor.moveToNext()) {
                contact = new NewContact();
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String phoneNo = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                Log.e("contact", "getAllContacts: " + name + " " + phoneNo + " " + photoUri);
//                Log.d("WalletYuyWalletYuyckeyckey", "phoneNo ==  " + phoneNo.toString());
                contact.setName(name);
                contact.setPhoneNumber(phoneNo);
                contact.setPhotoUri(photoUri);
                //contacts.add(contact);
                cleanList.put(contact.getPhoneNumber(), contact);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return new ArrayList<NewContact>(cleanList.values());
    }

    //Using LinkedHashMap to eliminate duplicate Contacts
    private List<NewContact> clearListFromDuplicatePhoneNumber(List<NewContact> list1) {
        Map<String, NewContact> cleanMap = new LinkedHashMap<String, NewContact>();
        for (int i = 0; i < list1.size(); i++) {
            cleanMap.put(list1.get(i).getPhoneNumber(), list1.get(i));
        }
        return new ArrayList<NewContact>(cleanMap.values());
    }

    //Format Phone Number
    private static String formatPhoneNumber(String phone) {
        String formatedPhone = phone.replaceAll(" ", "");
        int phoneNumberLength = formatedPhone.length();
        if (phoneNumberLength == 13) {
            formatedPhone = "0" + formatedPhone.substring(4);
        } else if (phoneNumberLength == 12) {
            formatedPhone = "0" + formatedPhone.substring(3);
        } else if (phoneNumberLength == 10) {
            return formatedPhone;
        }
        return formatedPhone;
    }
}
