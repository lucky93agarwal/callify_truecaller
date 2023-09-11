package com.gpslab.kaun.status;

import android.app.Activity;

import com.gpslab.kaun.ImageEditor;

public class ImageEditorRequest {
    public static void open(Activity activity, String path) {
        new ImageEditor.Builder(activity, path)
                .setStickerAssets("stickers")
                .open();
    }
}
