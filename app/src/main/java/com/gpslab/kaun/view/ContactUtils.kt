package com.gpslab.kaun.view

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.thoughtbot.expandablecheckrecyclerview.models.MultiCheckExpandableGroup
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.wafflecopter.multicontactpicker.ContactResult
import io.realm.RealmList
import java.util.ArrayList
import com.gpslab.kaun.view.PhoneNumber
import ezvcard.Ezvcard
import ezvcard.VCard
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

object ContactUtils {
    @JvmStatic
    fun getContactAsVcard(context: Context, uri: Uri?): List<VCard> {
        val cr = context.contentResolver
        var stream: InputStream? = null
        try {
            stream = cr.openInputStream(uri!!)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        val fileContent = StringBuffer("")
        var ch: Int
        try {
            while (stream!!.read().also { ch = it } != -1) fileContent.append(ch.toChar())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val data = String(fileContent)
        return Ezvcard.parse(data).all()
    }
    @JvmStatic
    fun getContactNamesFromVcard(vcards: List<VCard>): List<ExpandableContact> {
        val contactNameList: MutableList<ExpandableContact> = ArrayList()
        for (vcard in vcards) {
            //get contact name
            val fullName = vcard.formattedName.value
            //get contact numbers
            val telephoneNumbers = vcard.telephoneNumbers
            //create new List to fill it with phone numbers
            val numberList = RealmList<PhoneNumber>()

            //add numbers to list
            for (telephoneNumber in telephoneNumbers) {
                numberList.add(PhoneNumber(telephoneNumber.text))
            }

            //create new ExpandableContact object
            val contactName = ExpandableContact(fullName, numberList)
            //add contact to final list
            contactNameList.add(contactName)
        }
        return contactNameList
    }
    //convert the contacts that the user's picked into an ExpandableContact list
    @JvmStatic
    fun getContactsFromContactResult(results: List<ContactResult>): List<ExpandableContact> {
        val contactList: MutableList<ExpandableContact> = ArrayList()
        for (result in results) {
            val phoneNumbers = RealmList<PhoneNumber>()
            for (s in result.phoneNumbers) {
                if (!phoneNumbers.contains(PhoneNumber(s.number))) phoneNumbers.add(PhoneNumber(s.number))
            }
            val contactName = ExpandableContact(result.displayName, phoneNumbers)
            contactList.add(contactName)
        }
        return contactList
    }

    //get the Contact name from phonebook by number
    @JvmStatic
    fun queryForNameByNumber(phone: String): String {
        val context = MyApp.context()
        var name = phone
        try {
            val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone))
            val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
            val cursor = context.contentResolver.query(uri, projection, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    name = cursor.getString(0)
                }
                cursor.close()
            }
        } catch (e: Exception) {
            return name
        }
        return name
    }
    //check if a contact is exists in phonebook
    @JvmStatic
    fun contactExists(context: Context, number: String?): Boolean {
        var cur: Cursor? = null

        try {
            val lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number))
            val mPhoneNumberProjection = arrayOf(ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME)
            cur = context.contentResolver.query(lookupUri, mPhoneNumberProjection, null, null, null)
            if (cur != null) {
                if (cur.moveToFirst()) {
                    return true
                }
            }
        } catch (e: Exception) {
        } finally {
            cur?.close()
        }

        return false


    }
    //get only selected phone numbers
    @JvmStatic
    fun getContactsFromExpandableGroups(groups: List<ExpandableGroup<*>?>): List<ExpandableContact> {
        val contactNameList: MutableList<ExpandableContact> = ArrayList()
        for (x in groups.indices) {
            val group = groups[x] as MultiCheckExpandableGroup? ?: continue
            val name = group.title
            val phoneNumberList = RealmList<PhoneNumber>()
            for (i in group.items.indices) {
                val phoneNumber = group.items[i] as PhoneNumber
                //get only selected numbers && prevent duplicate numbers
                if (group.selectedChildren[i] && !phoneNumberList.contains(phoneNumber)) {
                    phoneNumberList.add(phoneNumber)
                }
            }
            if (!phoneNumberList.isEmpty()) contactNameList.add(ExpandableContact(name, phoneNumberList))
        }
        return contactNameList
    }
}