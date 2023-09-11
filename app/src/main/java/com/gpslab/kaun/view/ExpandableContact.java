package com.gpslab.kaun.view;

import android.os.Parcelable;

import com.thoughtbot.expandablecheckrecyclerview.models.MultiCheckExpandableGroup;
import com.gpslab.kaun.view.PhoneNumber;

import java.util.List;

public class ExpandableContact extends MultiCheckExpandableGroup implements Parcelable {


    public ExpandableContact(String contactName, List<PhoneNumber> phoneNumbers) {
        super(contactName, phoneNumbers);

    }


}
