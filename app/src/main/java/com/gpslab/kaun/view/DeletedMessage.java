package com.gpslab.kaun.view;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DeletedMessage extends RealmObject {

    public DeletedMessage() {
    }

    @PrimaryKey
    private String messageId;

    public DeletedMessage(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
    }

}
