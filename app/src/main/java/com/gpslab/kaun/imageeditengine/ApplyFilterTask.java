package com.gpslab.kaun.imageeditengine;

import android.graphics.Bitmap;
import android.os.AsyncTask;

public final class ApplyFilterTask extends AsyncTask<ImageFilter, Void, Bitmap> {
    private final TaskCallback<Bitmap> listenerRef;
    private Bitmap srcBitmap;

    public ApplyFilterTask(TaskCallback<Bitmap> taskCallbackWeakReference, Bitmap srcBitmap) {
        this.srcBitmap = srcBitmap;
        this.listenerRef = taskCallbackWeakReference;
    }

    @Override protected void onCancelled() {
        super.onCancelled();
    }

    @Override protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        if(listenerRef!=null){
            listenerRef.onTaskDone(result);
        }
    }

    @Override protected Bitmap doInBackground(ImageFilter... imageFilters) {
        if(imageFilters!=null && imageFilters.length>0) {
            ImageFilter imageFilter = imageFilters[0];
            return PhotoProcessing.filterPhoto(srcBitmap, imageFilter);
        }
        return null;
    }

    @Override protected void onPreExecute() {
        super.onPreExecute();
    }
}
