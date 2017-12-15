package com.example.zzb.wechathook;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zzb.wechathook.common.Config;
import com.example.zzb.wechathook.common.Share;
import com.example.zzb.wechathook.common.WeixinMD5;
import com.example.zzb.wechathook.database.Task;
import com.example.zzb.wechathook.friendsCircle.SnsStat;
import com.example.zzb.wechathook.message.MessageTable;
import com.example.zzb.wechathook.message.MsgStat;
import com.example.zzb.wechathook.message.model.MsgInfo;
import com.example.zzb.wechathook.ui.MomentStatActivity;
import com.example.zzb.wechathook.ui.MsgListActivity;

import net.sqlcipher.database.SQLiteDatabase;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    Task task = null;
    SnsStat snsStat = null;
    Button circleBtn,msgBtn;
    public ArrayList<MsgInfo> msgList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteDatabase.loadLibs(this);
        task = new Task(this.getApplicationContext());
        init();
        setContentView(R.layout.activity_main);

        task.testRoot();
        circleBtn=(Button)findViewById(R.id.launch_circle);
        msgBtn=(Button)findViewById(R.id.launch_message);
        circleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleBtn.setText(R.string.exporting_sns);
                circleBtn.setEnabled(false);
                new RunningTask().execute();

            }
        });
        msgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msgBtn.setText(R.string.exporting_msg);
                msgBtn.setEnabled(false);
                try {
                    loadMsgTask();
                }catch (Throwable e){
                    ((TextView)findViewById(R.id.description_textview_2)).setText("Error: " + e.getMessage());
                }
            }
        });
    }

    void loadMsgTask() throws Exception{
        MessageTable.getInstance(getApplicationContext())
                .getAllMsg()
                .subscribe(new Observer<List<MsgInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(List<MsgInfo> msgInfos) {
                        Log.i("Msg Size:",msgInfos.size()+"");
                        msgList.addAll(msgInfos);

                    }

                    @Override
                    public void onError(Throwable e) {
                        msgBtn.setText(R.string.launch_message);
                        msgBtn.setEnabled(true);
                        Toast.makeText(MainActivity.this,"导出消息失败",Toast.LENGTH_LONG);
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        msgBtn.setText(R.string.launch_message);
                        msgBtn.setEnabled(true);
                        MsgStat msgStat=new MsgStat(msgList);
                        Share.msgData=msgStat;
                        Intent intent = new Intent(MainActivity.this, MsgListActivity.class);
                        startActivity(intent);

                    }
                });
    }

    void init(){
        Share.UIN=getEnDBKey();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)== PackageManager.PERMISSION_GRANTED){
            Share.IMEI = ((TelephonyManager)  MainActivity.this.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            Share.KEY= WeixinMD5.n((Share.IMEI + Share.UIN).getBytes()).substring(0,7);
        }
    }
    class RunningTask extends AsyncTask<Void, Void, Void> {

        Throwable error = null;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                task.copySnsDB();
                task.initSnsReader();
                task.snsReader.runSnsMicroMsg();
                snsStat = new SnsStat(task.snsReader.getSnsList());
            } catch (Throwable e) {
                this.error = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void voidParam) {
            super.onPostExecute(voidParam);
            circleBtn.setText(R.string.launch_circle);
            circleBtn.setEnabled(true);
            if (this.error != null) {
                Toast.makeText(MainActivity.this, R.string.not_rooted, Toast.LENGTH_LONG).show();
                Log.e("wechatmomentstat", "exception", this.error);

                try {
                    ((TextView)findViewById(R.id.description_textview_2)).setText("Error: " + this.error.getMessage());
                } catch (Throwable e) {
                    Log.e("wechatmomentstat", "exception", e);
                }

                return;
            }
            Share.snsData = snsStat;
            Intent intent = new Intent(MainActivity.this, MomentStatActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private String getEnDBKey() {
        String result = "";
        try {
            InputStream inputStream = new FileInputStream(Config.EXT_DIR + "system_config_prefs.xml");
            boolean isParse = true;
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parse = factory.newPullParser();
            parse.setInput(inputStream, "UTF-8");
            int eventType = parse.getEventType();
            while (XmlPullParser.END_DOCUMENT != eventType && isParse) {
                String nodeName = parse.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("int".equals(nodeName)) {
                           if(parse.getAttributeValue(0).equals("default_uin")){
                               return parse.getAttributeValue(1);
                           }
                        }
                        break;
                    default:
                        break;
                }
                if (isParse) {
                    eventType = parse.next();
                }
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
