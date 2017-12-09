package com.selecto.vladrevuk.test.app.Classes;

import android.Manifest;

/**
 * Created by user on 08.12.2017.
 */

public class Constants {

    public static final int PERMISSION_REQUEST_CODE = 1;
    public static final String[] PERMISSIONS_REQUIRED = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS
    };
}
