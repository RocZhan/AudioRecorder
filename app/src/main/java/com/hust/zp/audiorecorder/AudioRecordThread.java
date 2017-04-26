package com.hust.zp.audiorecorder;

/**
 * Created by zhanpeng on 2017/4/24.
 */

public class AudioRecordThread implements Runnable {

    private AudioRecorder audioRecorder;
    public AudioRecordThread(){
        audioRecorder = new AudioRecorder();
    }

    @Override
    public void run(){
        if (AudioRecorder.isRecording == true){
            audioRecorder.startRecord();
        }
        if (AudioRecorder.isRecording == false){
            audioRecorder.stopRecord();
        }
    }



}
