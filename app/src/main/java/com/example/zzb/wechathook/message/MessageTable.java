package com.example.zzb.wechathook.message;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.zzb.wechathook.common.Config;
import com.example.zzb.wechathook.common.Share;
import com.example.zzb.wechathook.database.SqlCipher;
import com.example.zzb.wechathook.message.model.MsgInfo;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by zzb on 2017/12/15.
 */

public class MessageTable {
    static MessageTable instance=null;
    static SQLiteDatabase MsgDb;
    Context mContext;
    String dbPath = Config.EXT_DIR + "deEnMicroMsg.db";
    String key;

    private MessageTable(Context context){
        this.mContext=context;

    }

    public static MessageTable getInstance(Context context){
        if(instance==null){
            instance=new MessageTable(context);
        }
        return instance;
    }


    public Observable<List<MsgInfo>> getAllMsg() throws Exception {
        SqlCipher.decrypt(mContext,"EnMicroMsg.db","deEnMicroMsg.db", Share.KEY);
        if (!new File(dbPath).exists()) {
            Log.e("luqx", "EnMicroMsg DB file not found");
            throw new Exception("DB file not found");
        }
        MsgDb = SQLiteDatabase.openDatabase(dbPath, null, 0);
        return Observable.create(new ObservableOnSubscribe<List<MsgInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<MsgInfo>> emitter) throws Exception {
                Cursor cursor = null;
                try {
                    cursor = MsgDb.rawQuery("select * from message", new String[]{});
                    List<MsgInfo> result = new ArrayList<>();
                    while (cursor.moveToNext()) {
                        result.add(new MsgInfo(cursor));
                    }
                    emitter.onNext(result);
                    emitter.onComplete();
                } finally {
                    MsgDb.close();
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
