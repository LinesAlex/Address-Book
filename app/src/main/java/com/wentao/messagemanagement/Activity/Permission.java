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
import com.wentao.messagemanagement.tool.GetContactsInfo;


/**
 * Created by Administrator on 2017/11/10.
 */

public class Permission extends AppCompatActivity {
    private static Permission instance;
    public static Permission getInstance() {
        return instance;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_begin);
        instance = Permission.this;
        GetContactsInfo.getContacts(instance);
        String[] permission = new String[] {
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.RECEIVE_SMS};
        getPermission(permission, 0);
    }
    private void openPage() {
        Intent intent = new Intent(Permission.this, MessageAndCall.class);
        startActivity(intent);
        finish();
    }
    private void getPermission(String[] permission, int permissionId){
            boolean a = ContextCompat.checkSelfPermission(Permission.this, permission[0]) != PackageManager.PERMISSION_GRANTED;
            if (a) {//没有权限需要动态获取
                ActivityCompat.requestPermissions(Permission.this, permission, permissionId); //动态请求权限
            } else {
                openPage();
            }
    }
//    onRequestPermissionsResult获取动态权限后调用函数
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
            case 1:
            case 2:
            case 3:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openPage();
                } else {
                    Toast.makeText(Permission.this, "请给予权限", Toast.LENGTH_SHORT).show();
                }break;
            default:
        }
    }
}
