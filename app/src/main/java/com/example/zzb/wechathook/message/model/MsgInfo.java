package com.example.zzb.wechathook.message.model;

import android.database.Cursor;

import com.example.zzb.wechathook.friendsCircle.model.SnsInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zzb on 2017/12/15.
 */

public class MsgInfo {
    public String msgId = "";
    public String talker = "";
    public String content = "";
    public String createTime = "";
    public long tallkerId = 0;
    public long timestamp = 0;

    public boolean ready = false;
    public boolean isCurrentUser = false;
    public boolean selected = true;


    public MsgInfo(Cursor cursor){
        this.content=cursor.getString(cursor.getColumnIndex("content"));
        this.tallkerId=cursor.getLong(cursor.getColumnIndex("talkerId"));
        this.talker=cursor.getString(cursor.getColumnIndex("talker"));
        timestamp=cursor.getLong(cursor.getColumnIndex("createTime"));
        this.createTime=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date(timestamp));

    }
}
