package com.gpslab.kaun.view;

import java.net.URLConnection;

public class FileFilter {
    //this is called when user picks a "File"
    //we want to ensure that the user did not select an image or video or audio
    public static boolean isOkExtension(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && !mimeType.startsWith("image")
                && !mimeType.startsWith("video")
                && !mimeType.startsWith("audio");
    }
}
