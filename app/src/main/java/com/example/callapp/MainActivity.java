package com.example.callapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.role.RoleManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private static final String[] permissions = {
            android.Manifest.permission.CALL_PHONE,
            android.Manifest.permission.READ_PHONE_STATE
    };
    private PermissionUtility permissionUtility;
    private static final int REQUEST_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionUtility = new PermissionUtility(this, permissions);
        if (permissionUtility.arePermissionsEnabled()) {
            Log.d(TAG, "Permission granted 1");
        } else {
            permissionUtility.requestMultiplePermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionUtility.onRequestPermissionsResult(requestCode, permissions, grantResults))
            Log.d(TAG, "Permission granted 2");
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void requestRole() {
        RoleManager roleManager = (RoleManager) getSystemService(ROLE_SERVICE);
        Intent intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER);
        startActivityForResult(intent, REQUEST_ID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ID) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Ahora Call App es la aplicación predeterminada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Call App no tiene permisos", Toast.LENGTH_SHORT).show();
            }
        }
    }
}