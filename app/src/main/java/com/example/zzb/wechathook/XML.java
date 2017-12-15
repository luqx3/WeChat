package com.example.zzb.wechathook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.zzb.wechathook.common.Config;
import com.example.zzb.wechathook.common.Share;
import com.example.zzb.wechathook.database.SqlCipher;
import com.example.zzb.wechathook.message.model.MsgInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zzb on 2017/12/15.
 */

public class XML {
    static public void getDbKey(){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
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
                                if (parse.getAttributeValue(0).equals("default_uin")) {
                                    isParse = false;
                                    e.onNext(parse.getAttributeValue(1));
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
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Share.KEY=s;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
