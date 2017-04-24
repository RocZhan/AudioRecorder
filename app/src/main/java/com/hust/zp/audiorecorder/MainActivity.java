package com.hust.zp.audiorecorder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button startRecord;
    private Button stopRecord;
    private Button playRecord;
    private ListView listView;
    //MyAdatpter myAdapter;

    private AudioRecorder audioRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startRecord = (Button)findViewById(R.id.startRecord);
        startRecord.setOnClickListener(this);
        stopRecord = (Button)findViewById(R.id.stopRecord);
        stopRecord.setOnClickListener(this);
        playRecord = (Button)findViewById(R.id.playRecord);
        playRecord.setOnClickListener(this);

        //myAdapter = new MyAdatpter();
        listView = (ListView)findViewById(R.id.listView);
        //listView.setAdapter(myAdapter);
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0,View arg1,int arg2,long arg3){
                File file = myAdapter.file[arg2];
                startPlayRecord(file.getAbsolutePath());
            }
        });*/

        audioRecorder = new AudioRecorder();
}
/*
    public class MyAdatpter extends BaseAdapter {
        File[] file;

        public MyAdatpter() {
            super();
            query();
        }

        public void query() {
            String mFileName = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
            File f = new File(mFileName);
            File [] found = f.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    if (pathname.getName().startsWith("Record ")) {
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
            View v = getLayoutInflater().inflate(R.layout.item, null);
            TextView txtText = (TextView) v.findViewById(R.id.textView1);
            TextView txtSize = (TextView) v.findViewById(R.id.textView2);

            File f = file[arg0];
            txtText.setText(f.getName());
            txtSize.setText(f.length() / 1024 + " kb");

            return v;
        }

    }
*/
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.startRecord://点击开始录音按钮
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        audioRecorder.startRecord();
                    }
                }).start();
                break;
            case R.id.stopRecord://点击停止录音按钮
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        audioRecorder.stopRecord();
                    }
                }).start();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(audioRecorder != null){
            audioRecorder = null;
        }
    }

}
