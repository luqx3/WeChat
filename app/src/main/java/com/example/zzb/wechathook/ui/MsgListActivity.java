package com.example.zzb.wechathook.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.zzb.wechathook.R;
import com.example.zzb.wechathook.common.Config;
import com.example.zzb.wechathook.common.Share;
import com.example.zzb.wechathook.database.Task;
import com.example.zzb.wechathook.friendsCircle.adapter.SnsInfoAdapter;
import com.example.zzb.wechathook.message.adapter.MsgAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by zzb on 2017/12/15.
 */

public class MsgListActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView snsListView = (ListView)findViewById(R.id.sns_list_view);
        MsgAdapter adapter = new MsgAdapter(this, R.layout.sns_item, Share.msgData.msgList);
        snsListView.setAdapter(adapter);

    }


}
