package com.gpslab.kaun.DB;

public class Temp {    // अब हम static variable बनायेगे।
    // अब हमें seter and getter बनना होगा।
    public static MyDbHandler myDbHandler;

    public static MyDbHandler getMyDbHandler() {
        return myDbHandler;
    }

    public static void setMyDbHandler(MyDbHandler myDbHandler) {
        Temp.myDbHandler = myDbHandler;
    }

}
