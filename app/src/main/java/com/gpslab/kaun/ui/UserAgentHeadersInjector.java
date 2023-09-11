package com.gpslab.kaun.ui;

import com.danikula.videocache.headers.HeaderInjector;

import java.util.HashMap;
import java.util.Map;

public class UserAgentHeadersInjector implements HeaderInjector {

    @Override
    public Map<String, String> addHeaders(String url) {
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", PreferenceManager.getInstance().getToken(WhatsCloneApplication.getInstance()));
        return map;
    }
}
