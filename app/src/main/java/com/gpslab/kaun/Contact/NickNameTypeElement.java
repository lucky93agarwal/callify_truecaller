package com.gpslab.kaun.Contact;

import android.database.Cursor;
import android.provider.ContactsContract;

import com.google.gson.annotations.Expose;

public class NickNameTypeElement  extends ElementParent
{
    @Expose
    private String nickNameType = "";
    @Expose
    private final String elementType;
    public static final String column = ContactsContract.CommonDataKinds.Nickname.TYPE;
    public static final String mime = ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE;

    public NickNameTypeElement(Cursor cursor)
    {
        elementType = getClass().getSimpleName();
        setValue(cursor);
    }
    @Override
    public String getElementType()
    {
        return elementType;
    }
    @Override
    public String getValue()
    {
        return nickNameType;
    }
    @Override
    public void setValue(Cursor cursor)
    {
        if (cursor==null)
        {
            return;
        }

        nickNameType = Utilities.getNickNameType(Utilities.getColumnIndex(cursor, column));

        if (nickNameType == null)
        {
            nickNameType = "UNKNOWN";
        }
    }

}
