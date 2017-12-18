package com.example.asus.tongxun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.demonstrate.DemonstrateUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText tv_account;
    private EditText tv_pw;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        tv_account = (EditText) findViewById(R.id.tv_account);
        tv_pw = (EditText) findViewById(R.id.tv_pw);
        btn_login = (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                //封装登录信息.
                LoginInfo info
                        = new LoginInfo(tv_account.getText().toString(),tv_pw.getText().toString());
                //请求服务器的回调
                RequestCallback<LoginInfo> callback =
                        new RequestCallback<LoginInfo>() {
                            @Override
                            public void onSuccess(LoginInfo param) {
                                DemonstrateUtil
                                        .showLogResult("onSuccess--"+param.getAccount()+"--"+param.getToken());
                                DemonstrateUtil.showToastResult(MainActivity.this,"登录成功!");

                                // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用

                                //跳转到消息页面
                                startActivity(new Intent(MainActivity.this,WelcomeActivity.class));
                                finish();
                            }

                            @Override
                            public void onFailed(int code) {
                                DemonstrateUtil.showLogResult("登录失败!返回码"+code);
                            }

                            @Override
                            public void onException(Throwable exception) {
                                DemonstrateUtil.showLogResult(exception.toString());
                            }

                        };
                //发送请求.
                NIMClient.getService(AuthService.class).login(info)
                        .setCallback(callback);
                break;
        }
    }
}
