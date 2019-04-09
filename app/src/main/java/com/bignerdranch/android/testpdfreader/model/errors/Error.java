package com.bignerdranch.android.testpdfreader.model.errors;

public abstract class Error extends Exception {

    private String mMessage;

    public Error(String message) {
        mMessage = message;
    }

    @Override
    public String toString() {
        return mMessage;
    }


}
