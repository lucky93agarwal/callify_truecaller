package com.gpslab.kaun.Contact;

import android.content.Context;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableArrayList;

import com.gpslab.kaun.CallLogInfo;
import com.gpslab.kaun.CallLogUtils;
import com.gpslab.kaun.model.MessageInfo;
import com.gpslab.kaun.model.MessageUtils;

import java.util.List;

public class ContactViewModel extends BaseObservable {
    private ObservableArrayList<NewContact> contacts;
    private ContactRepository repository;



    private ObservableArrayList<CallLogInfo> calllog;
    private CallLogUtils repositorycalllog;



    private ObservableArrayList<MessageInfo> messlog;
    private MessageUtils repositorymessage;





    public ContactViewModel(Context context) {
        contacts = new ObservableArrayList<>();
        calllog = new ObservableArrayList<>();
        messlog = new ObservableArrayList<>();

        repositorycalllog = new CallLogUtils(context);
        repository = new ContactRepository(context);
        repositorymessage = new MessageUtils(context);

    }

    public List<NewContact> getContacts() {
        contacts.addAll(repository.fetchContacts());
        return contacts;
    }


    public List<MessageInfo> getMessage(){
        Log.d("GetAllMessage", "repositorymessage size = " + repositorymessage.readCallLogs().size());
        messlog.addAll(repositorymessage.readCallLogs());
        return messlog;
    }



    public List<CallLogInfo> getCallLog(){
        calllog.addAll(repositorycalllog.readCallLogs());
        return calllog;
    }
}
