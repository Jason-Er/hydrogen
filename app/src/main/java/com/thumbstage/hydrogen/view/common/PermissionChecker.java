package com.thumbstage.hydrogen.view.common;

import java.util.List;
import java.util.LinkedList;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.content.pm.PackageManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

import com.thumbstage.hydrogen.R;

import javax.inject.Singleton;

public class PermissionChecker {
    private Activity activity;
    private String[] permissions;
    private PermissionCheckCallback mCallback;
    private static final String TAG = "PermissionChecker";
    private static final int REQUST_CODE = 0;
    private boolean isDefaultDialog = false;

    public PermissionChecker setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public PermissionChecker setPermissions(String[] permissions) {
        this.permissions = permissions;
        return this;
    }

    public PermissionChecker setCallback(PermissionCheckCallback callback) {
        this.mCallback = callback;
        return this;
    }

    public PermissionChecker setDefaultDialog(boolean isDefaultDialog) {
        this.isDefaultDialog = isDefaultDialog;
        return this;
    }

    public boolean isPermissionGrant(String permission) {
        return ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void checkPermission() {
        if (permissions == null || activity == null || permissions == null) return;
        List<String> permissionToRequestList = new LinkedList<String>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
                permissionToRequestList.add(permission);
        }
        String[] permissionToRequest = (String[]) permissionToRequestList.toArray(new String[permissionToRequestList.size()]);
        if (permissionToRequest.length > 0) {
            ActivityCompat.requestPermissions(activity, permissionToRequest, REQUST_CODE);
            if (mCallback != null) mCallback.onRequest();
        } else {
            if (mCallback != null) {
                mCallback.onGranted();
            }
        }
    }

    public interface PermissionCheckCallback {
        void onRequest();

        void onGranted();

        void onGrantSuccess();

        void onGrantFail();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Grant permission successfully");
                    if (mCallback != null) mCallback.onGrantSuccess();
                } else {
                    if (isDefaultDialog) {
                        popupWarningDialog();
                        return;
                    }
                    if (mCallback != null) {
                        mCallback.onGrantFail();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void popupWarningDialog() {
        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        if (mCallback != null) mCallback.onGranted();
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        activity.finish();
                        break;
                    default:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.Warning);
        builder.setMessage(R.string.PermissionNotGrant);
        builder.setPositiveButton(R.string.OK, dialogOnclicListener);
        builder.setNegativeButton(R.string.Cancel, dialogOnclicListener);
        builder.create().show();
    }
}
