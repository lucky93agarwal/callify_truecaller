package com.gpslab.kaun.Contact;

import android.database.Cursor;

import java.util.Set;

public abstract class FieldParent
{
    public abstract Set<String> registerElementsColumns();
    public abstract void execute(String mime, Cursor cursor);
}
