package com.gpslab.kaun.ui;

import java.util.concurrent.ExecutionException;

public abstract class AssertedSuccessListener<T> implements ListenableFuture.Listener<T> {
    @Override
    public void onFailure(ExecutionException e) {
        throw new AssertionError(e);
    }
}

