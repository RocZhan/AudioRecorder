package com.hust.zp.audiorecorder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import static com.hust.zp.audiorecorder.AudioRecorder.isRecording;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Roc";

    private Button startRecord;
    private Button stopRecord;
    private Button playRecord;
    private ListView listView;
    MyAdapter myAdapter;

    //private int sampleRateInHz;
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


        listView = (ListView)findViewById(R.id.listView);
        myAdapter = new MyAdapter(MainActivity.this,R.layout.item);
        listView.setAdapter(myAdapter);
        refreshViewByRecordingState();
}



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
    public void onClick(View v){
        switch(v.getId()){
            case R.id.startRecord://点击开始录音按钮

                //AudioRecorder.isRecording = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(Thread.currentThread().getName(), "onClick: "+"开始录音");
                        audioRecorder = new AudioRecorder();
                        audioRecorder.startRecord();
                    }
                }).start();
                break;
            case R.id.stopRecord://点击停止录音按钮

                //AudioRecorder.isRecording = false;
                Log.i(Thread.currentThread().getName(), "onClick: "+"停止录音");
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
