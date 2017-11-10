package com.wentao.messagemanagement.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.wentao.messagemanagement.R;

/**
 * Created by Administrator on 2017/11/10.
 */

public class ActivityOfPermission extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.begin_page);
        String[] permission = new String[]{
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.RECEIVE_SMS,};
        getPermission(permission, 1);
    }
    private void openPage() {
        Intent intent = new Intent(ActivityOfPermission.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void getPermission(String[] permission, int permissionId){
        ActivityCompat.requestPermissions(ActivityOfPermission.this, permission, permissionId++);
        for(String per : permission) {

            if (ContextCompat.checkSelfPermission(ActivityOfPermission.this, per) != PackageManager.PERMISSION_GRANTED) {//没有权限需要动态获取
                ActivityCompat.requestPermissions(ActivityOfPermission.this, new String[]{per}, permissionId++);//动态请求权限
            } else {
                openPage();
            }
        }
    }
    //onRequestPermissionsResult获取动态权限后调用函数
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            openPage();
        } else {
            Toast.makeText(ActivityOfPermission.this,"请给予权限", Toast.LENGTH_SHORT).show();
        }
    }
}
