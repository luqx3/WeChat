package com.example.zzb.wechathook.message;

import com.example.zzb.wechathook.friendsCircle.model.SnsInfo;
import com.example.zzb.wechathook.message.model.MsgInfo;

import java.util.ArrayList;

/**
 * Created by zzb on 2017/12/15.
 */

public class MsgStat {
    public ArrayList<MsgInfo> msgList = null;
    public MsgStat(ArrayList<MsgInfo> msgList) {
        this.msgList = msgList;
    }
}
