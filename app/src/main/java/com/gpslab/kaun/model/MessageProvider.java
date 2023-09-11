package com.gpslab.kaun.model;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import de.ub0r.android.logg0r.Log;

public final class MessageProvider extends ContentProvider {

    /**
     * Tag for logging.
     */
    static final String TAG = "mp";

    /**
     * Content {@link Uri} for messages.
     */
    public static final Uri CONTENT_URI = Uri.parse("content://de.ub0r.android.smsdroid/msg");

    /**
     * {@inheritDoc}
     */
    @Override
    public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType(final Uri uri) {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Uri insert(final Uri uri, final ContentValues values) {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreate() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cursor query(final Uri uri, final String[] projection, final String selection,
                        final String[] selectionArgs, final String sortOrder) {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int update(final Uri uri, final ContentValues values, final String selection,
                      final String[] selectionArgs) {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParcelFileDescriptor openFile(final Uri uri, final String mode)
            throws FileNotFoundException {
        Log.d(TAG, "openFile(", uri, ")");
        final long mid = ContentUris.parseId(uri);
        String body = null;
        final Cursor cursor = getContext().getContentResolver()
                .query(Uri.parse("content://sms/" + mid), Message.PROJECTION_SMS, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            body = cursor.getString(Message.INDEX_BODY);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        if (body != null) {
            try {
                File f = File.createTempFile("message." + mid, "txt");
                f.createNewFile();
                FileWriter fw = new FileWriter(f);
                fw.append(body);
                fw.close();
                return ParcelFileDescriptor.open(f, ParcelFileDescriptor.MODE_READ_ONLY);
            } catch (IOException e) {
                Log.e(TAG, "IO ERROR", e);
                Toast.makeText(getContext(), "IO ERROR", Toast.LENGTH_LONG).show();
            }
        }
        return null;
    }
}
