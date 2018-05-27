package com.esgi.virtualclassroom.data.api;

public interface ApiListener<T> {
    void onSuccess(T response);
    void onError(Throwable throwable);
}
