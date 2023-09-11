package com.gpslab.kaun.Contact;

import android.database.Cursor;
import android.provider.ContactsContract;

import com.google.gson.annotations.Expose;

public class NumberTypeElement extends ElementParent
{
    @Expose
    private String numberType = "";
    @Expose
    private final String elementType;
    public static final String column = ContactsContract.CommonDataKinds.Phone.TYPE;
    public static final String mime = ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE;

    public NumberTypeElement(Cursor cursor)
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
        return numberType;
    }
    @Override
    public void setValue(Cursor cursor)
    {
        if (cursor==null)
        {
            return;
        }

        numberType = Utilities.getNumberType(Utilities.getColumnIndex(cursor, column));

        if (numberType == null)
        {
            numberType = "UNKNOWN";
        }
    }

}
