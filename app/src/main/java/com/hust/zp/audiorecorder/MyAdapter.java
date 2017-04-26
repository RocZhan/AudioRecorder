package com.hust.zp.audiorecorder;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.FileFilter;

import static android.content.ContentValues.TAG;

/**
 * Created by zhanpeng on 2017/4/26.
 * 定义MyAdapter（继承于BaseAdapter的适配器），将文件传给ListView
 */

public class MyAdapter extends BaseAdapter {

    File[] file;
    private int resourceId;
    private Context context;

    public MyAdapter(Context context,int textViewResourceId) {
        super();
        query();
        this.context = context;
        resourceId = textViewResourceId;
    }

    public void query() {
        String mFileName = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        Log.d(TAG, "query: " + mFileName);
        File f = new File(mFileName);
        File [] found = f.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().startsWith("Record")) {
                    return true;
                }
                return false;
            }
        });

        file = new File[found.length];
        for (int i = 0; i < found.length; i++) {
            file[i] = found[found.length-i-1];
        }
        Log.d(TAG, "file: " + found.length);
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return file == null ? 0 : file.length;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        View v = LayoutInflater.from(context).inflate(resourceId, null);
        TextView txtText = (TextView) v.findViewById(R.id.textView1);
        TextView txtSize = (TextView) v.findViewById(R.id.textView2);

        File f = file[arg0];
        txtText.setText(f.getName());
        txtSize.setText(f.length() / 1024 + " kb");

        return v;
    }

}
