package com.bignerdranch.android.testpdfreader.controller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public abstract class AbstractActivityWithPermissions extends AppCompatActivity {
    private static final String TAG = "AbstractActivityWithPermissions";

    private static final int PERMISSIONS_REQUEST_INTERNET = 1;
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isPermissionAllowed(Manifest.permission.INTERNET)) {
            requestInternetPermission(Manifest.permission.INTERNET, PERMISSIONS_REQUEST_INTERNET);
        }
        if (!isPermissionAllowed(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            requestInternetPermission(Manifest.permission.INTERNET, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }


    private boolean isPermissionAllowed(String p) {
        int result = ContextCompat.checkSelfPermission(this, p);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestInternetPermission(String p, int r) {
        ActivityCompat.shouldShowRequestPermissionRationale(this, p);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, r);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_INTERNET) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Toast.makeText(this, "PERMISSIONS_REQUEST_INTERNET granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "PERMISSIONS_REQUEST_INTERNET denied", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE denied", Toast.LENGTH_LONG).show();
            }
        }
    }

}
