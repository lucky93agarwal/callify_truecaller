package com.gpslab.kaun.Contact;

import android.database.Cursor;

public abstract class ElementParent
{
    public abstract String getElementType();
    public abstract String getValue();
    public abstract void setValue(Cursor cursor);
}
