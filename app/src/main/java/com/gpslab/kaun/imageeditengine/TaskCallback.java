package com.gpslab.kaun.imageeditengine;

public interface TaskCallback<T> {
    void onTaskDone(T data);
}
