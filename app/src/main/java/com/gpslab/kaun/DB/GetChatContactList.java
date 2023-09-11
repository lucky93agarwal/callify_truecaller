package com.gpslab.kaun.DB;

public class GetChatContactList {
    public String id;
    public String contacts_id;
    public String contacts_image;
    public String contacts_status;
    public String contacts_chat;
    public String contacts_name;

    public String getContacts_chat() {
        return contacts_chat;
    }

    public void setContacts_chat(String contacts_chat) {
        this.contacts_chat = contacts_chat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContacts_id() {
        return contacts_id;
    }

    public void setContacts_id(String contacts_id) {
        this.contacts_id = contacts_id;
    }

    public String getContacts_image() {
        return contacts_image;
    }

    public void setContacts_image(String contacts_image) {
        this.contacts_image = contacts_image;
    }

    public String getContacts_status() {
        return contacts_status;
    }

    public void setContacts_status(String contacts_status) {
        this.contacts_status = contacts_status;
    }

    public String getContacts_name() {
        return contacts_name;
    }

    public void setContacts_name(String contacts_name) {
        this.contacts_name = contacts_name;
    }
}
