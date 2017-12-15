package com.example.zzb.wechathook.message.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zzb.wechathook.R;
import com.example.zzb.wechathook.friendsCircle.adapter.SnsInfoAdapter;
import com.example.zzb.wechathook.friendsCircle.model.SnsInfo;
import com.example.zzb.wechathook.message.model.MsgInfo;

import java.util.ArrayList;


/**
 * Created by zzb on 2017/12/15.
 */

public class MsgAdapter extends ArrayAdapter<MsgInfo> {
    ArrayList<MsgInfo> msgInfosList;
    public  MsgAdapter(Context context, int resource, ArrayList<MsgInfo> msgInfosList){
            super(context, resource, msgInfosList);
            this.msgInfosList = msgInfosList;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder viewHolder;

        MsgInfo msgInfo = msgInfosList.get(position);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.sns_item, null);

            final CheckBox selectedCheckBox = (CheckBox) view.findViewById(R.id.sns_item_username);
            TextView snsContentTextView = (TextView) view.findViewById(R.id.sns_item_text_content);
            TextView snsTimeTextView = (TextView) view.findViewById(R.id.sns_item_time);
            LinearLayout photoContainer = (LinearLayout) view.findViewById(R.id.sns_item_photo_layout);

            viewHolder = new ViewHolder();
            viewHolder.selectedCheckBox = selectedCheckBox;
            viewHolder.snsContentTextView = snsContentTextView;
            viewHolder.snsTimeTextView = snsTimeTextView;
            viewHolder.photoContainer = photoContainer;

//            for (int i=0;i<10;i++) {
//                ImageView snsImageView = new ImageView(getContext());
//                viewHolder.imageViewList.add(snsImageView);
//            }

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.selectedCheckBox.setText(msgInfo.talker);
        viewHolder.selectedCheckBox.setChecked(msgInfo.selected);
        viewHolder.snsContentTextView.setText(msgInfo.content);
        viewHolder.snsTimeTextView.setText(msgInfo.createTime);
        viewHolder.selectedCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MsgInfo snsInfo = msgInfosList.get(position);
                snsInfo.selected = viewHolder.selectedCheckBox.isChecked();
            }
        });

        //viewHolder.photoContainer.removeAllViews();
        return view;
    }

    static protected class ViewHolder {
        CheckBox selectedCheckBox;
        TextView snsContentTextView;
        TextView snsTimeTextView;
        LinearLayout photoContainer;
        //ArrayList<ImageView> imageViewList = new ArrayList<ImageView>();
    }
}
