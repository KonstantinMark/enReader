package com.bignerdranch.android.testpdfreader.controller.resource.resource_view.page.service;

public interface TextSelector {

    void unSelect();

    void select();

    void set(int start, int end);
}
