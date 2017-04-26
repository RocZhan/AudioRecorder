package com.hust.zp.audiorecorder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;

import static com.hust.zp.audiorecorder.AudioRecorder.isRecording;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener{

    private static final String TAG = "Roc";

    private Button startRecord;
    private Button stopRecord;
    private Button playRecord;
    private ListView listView;
    private AudioRecorder audioRecorder;
    private AudioPlayer audioPlayer;
    MyAdapter myAdapter;//传递信息给listview的适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI：Button，listview
        startRecord = (Button)findViewById(R.id.startRecord);
        startRecord.setOnClickListener(this);
        stopRecord = (Button)findViewById(R.id.stopRecord);
        stopRecord.setOnClickListener(this);
        playRecord = (Button)findViewById(R.id.playRecord);
        playRecord.setOnClickListener(this);

        listView = (ListView)findViewById(R.id.listView);
        myAdapter = new MyAdapter(MainActivity.this,R.layout.item);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(this);
        refreshViewByRecordingState();//根据录音状态刷新view
}

    /*
     *定义刷新view的方法
     */
    protected void refreshViewByRecordingState() {
        if (isRecording) {
            isRecording = true;
            Log.d(TAG, "refreshViewByRecordingState: "+ isRecording);
        } else {
            isRecording = false;
            Log.d(TAG, "refreshViewByRecordingState: " + isRecording);
        }

        myAdapter.query();
        myAdapter.notifyDataSetChanged();
    }




        @Override
        public void onItemClick(AdapterView<?> arg0,View arg1,int arg2,long arg3){
            final File file = myAdapter.file[arg2];
            new Thread(new Runnable() {
                @Override
                public void run() {
                    audioPlayer = new AudioPlayer(file.getAbsolutePath());
                    audioPlayer.startPlay();
                }
            }).start();
        }


    @Override
    public void onClick(View v){
        switch(v.getId()){
            //点击开始录音按钮
            case R.id.startRecord:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(Thread.currentThread().getName(), "onClick: "+"开始录音");
                        audioRecorder = new AudioRecorder();
                        audioRecorder.startRecord();
                    }
                }).start();
                break;
            //点击停止录音按钮
            case R.id.stopRecord:
                audioRecorder.stopRecord();
                refreshViewByRecordingState();
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
