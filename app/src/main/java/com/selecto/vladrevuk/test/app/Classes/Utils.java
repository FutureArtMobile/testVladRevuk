package com.selecto.vladrevuk.test.app.Classes;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * Created by user on 08.12.2017.
 */

public class Utils {

    public static boolean checkPermission(Context context, final String[] PERMISSIONS,
                                          final int PERMISSION_REQUEST_CODE, boolean needRequest) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        final ArrayList<String> permissionsMissing = new ArrayList<>();
        for (final String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsMissing.add(permission);
            }
        }
        if (!permissionsMissing.isEmpty()) {
            final String[] array = new String[permissionsMissing.size()];
            permissionsMissing.toArray(array);
            if (needRequest)
                ActivityCompat.requestPermissions((Activity) context, array, PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }
}
