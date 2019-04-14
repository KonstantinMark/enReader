package com.bignerdranch.android.testpdfreader.model.errors;

import org.jetbrains.annotations.NotNull;

public abstract class Error extends Exception {

    private String mMessage;

    public Error(String message) {
        mMessage = message;
    }

    @NotNull
    @Override
    public String toString() {
        return mMessage;
    }


}
