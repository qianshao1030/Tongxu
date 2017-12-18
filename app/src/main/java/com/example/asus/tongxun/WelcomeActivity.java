package com.example.asus.tongxun;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.demonstrate.DemonstrateUtil;
import com.example.demonstrate.DialogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_recivetv_in;
    private TextView tv_send_out;
    private EditText et_input;
    private Button btn_select_people;
    private Button btn_send;
    private Button btn_out;

    private Observer<List<IMMessage>> incomingMessageObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
        //注册消息观察者,registerObserver
        registerObserver();
    }

    private void registerObserver() {
        // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
        //消息接收观察者
        incomingMessageObserver = new Observer<List<IMMessage>>() {
            @Override
            public void onEvent(List<IMMessage> imMessages) {
                // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
                IMMessage imMessage = imMessages.get(0);
                tv_recivetv_in.setText(imMessage.getFromNick() + "-->:" + imMessage.getContent());
                account = imMessage.getFromAccount();
            }
        };
        //注册消息接收观察者,
        //true,代表注册.false,代表注销
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, true);
    }

    private void initView() {
        tv_recivetv_in = (TextView) findViewById(R.id.tv_recivetv_in);
        tv_send_out = (TextView) findViewById(R.id.tv_send_out);
        et_input = (EditText) findViewById(R.id.et_input);
        btn_select_people = (Button) findViewById(R.id.btn_select_people);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_out = (Button) findViewById(R.id.btn_out);

        btn_select_people.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        btn_out.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select_people:
               //选择联系人.
                final String[] accounts = {"1690152226", "2871794243","123456"};
                final String[] items = {
                        "小鸟",
                        "大鸟",
                        "小梁",
                };
                DialogUtil.showListDialog(WelcomeActivity.this, "请选择联系人!", items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        account = accounts[which];
                        DemonstrateUtil.showToastResult(WelcomeActivity.this, items[which]);
                    }
                });
                break;
            case R.id.btn_send:
                //发送消息
                sendMessage();
                break;
            case R.id.btn_out:
               //退出登录
                loginOut();
                break;
        }
    }
    private String account = "zxn002";

    private void sendMessage() {
        // 以单聊类型为例
        SessionTypeEnum sessionType = SessionTypeEnum.P2P;
        String text = et_input.getText().toString();
        // 创建一个文本消息
        IMMessage textMessage = MessageBuilder.createTextMessage(account, sessionType, text);

        // 发送给对方
        NIMClient.getService(MsgService.class).sendMessage(textMessage, false);
        tv_send_out.setText(text);
    }

    private void loginOut() {
        NIMClient.getService(AuthService.class).logout();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销消息接收观察者.
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, false);
    }
}
