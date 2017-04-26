package com.hust.zp.audiorecorder;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by zhanpeng on 2017/4/26.
 * 定义MyAdapter（继承于BaseAdapter的适配器），将文件传给ListView
 */

public class MyAdapter extends BaseAdapter {

    File[] file;
    private int resourceId;
    private Context context;

    //MyAdapter的构造函数，把MainActivity的活动和listview的子布局xml文件的Id传过来
    public MyAdapter(Context context,int textViewResourceId) {
        super();
        query();
        this.context = context;
        resourceId = textViewResourceId;
    }

    //构造一个队列读取根目录下的现存的录音文件
    public void query() {
        String mFileName = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        File f = new File(mFileName);
        //通过FileFilter筛选出以“Record ”开头的文件
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
        View v = LayoutInflater.from(context).inflate(resourceId, null);//获得listview的子布局layout(item.xml)
        TextView txtText = (TextView) v.findViewById(R.id.textView1);
        TextView txtSize = (TextView) v.findViewById(R.id.textView2);

        File f = file[arg0];
        txtText.setText(f.getName());
        txtSize.setText(f.length() / 1024 + " kb");

        return v;
    }

}
