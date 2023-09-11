package com.gpslab.kaun.Contact;

import android.database.Cursor;

import com.google.gson.annotations.Expose;

import java.util.HashSet;
import java.util.Set;

public class WebsiteContainer
{
    @Expose
    private WebsiteElement website;

    public WebsiteContainer(Cursor cursor)
    {
        website = new WebsiteElement(cursor);
    }

    public static Set<String> getFieldColumns()
    {
        Set<String> columns = new HashSet<>();
        columns.add(WebsiteElement.column);
        return columns;
    }

    public String getWebsite()
    {
        String result = Utilities.elementValue(website);
        return result;
    }

}
