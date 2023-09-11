package com.gpslab.kaun.ui;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "groups")
public class GroupModel {

    @NonNull
    @PrimaryKey
    private String _id;

    private String created;

    private String image;


    private String name;

    @ForeignKey(entity = UsersModel.class, parentColumns = "_id", childColumns = "ownerId")
    private String ownerId;

    private String owner_phone;


/*
    @Embedded
    private UsersModel owner;nfkko*/



    @NonNull
    public String get_id() {
        return _id;
    }

    public void set_id(@NonNull String _id) {
        this._id = _id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
/*
    public UsersModel getOwner() {
        return owner;
    }

    public void setOwner(UsersModel owner) {
        this.owner = owner;
    }*/
/*
    public List<MembersModel> getMembers() {
        return members;
    }

    public void setMembers(List<MembersModel> members) {
        this.members = members;
    }*/

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwner_phone() {
        return owner_phone;
    }

    public void setOwner_phone(String owner_phone) {
        this.owner_phone = owner_phone;
    }

    @Override
    public String toString() {
        return "GroupModel{" +
                "_id='" + _id + '\'' +
                ", created='" + created + '\'' +
                ", image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", owner_phone='" + owner_phone + '\'' +
                '}';
    }
}

