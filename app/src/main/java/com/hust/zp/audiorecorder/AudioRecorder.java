package com.hust.zp.audiorecorder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhanpeng on 2017/4/24.
 */

public class AudioRecorder {

    public static final int RECORDER_SAMPLE_RATE = 48000;
    public static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_OUT_MONO;
    public static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    public int bufferSize = 0;

    private boolean isRecording;
    private AudioRecord audioRecord;


    public AudioRecorder(){
        initParam();
    }

    private void initParam(){
        bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLE_RATE,RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,RECORDER_SAMPLE_RATE,RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING,bufferSize);

    }

    /*
     *开始录音
     */
    public void startRecord(){
        audioRecord.startRecording();//开始录音
        isRecording = true;
        //new Thread(new AudioRecordThread()).start();
        writeDataToFile();
    }

    /*
     *将录音的音频存入文件中，以便后面进行播放
     */
    private void writeDataToFile(){
        //newFile();
        short[] sAudioData = new short[bufferSize];
        byte[] bAudioData = new byte[bufferSize * 2];
        FileOutputStream fos = null;
        int size;//audioRecor.read返回的数值
        try{
            File file = new File(Environment.getExternalStorageDirectory(),audioName());
            if (file.exists()){
                file.delete();
            }
            fos = new FileOutputStream(file);
        }catch(Exception e){
            e.printStackTrace();
        }
        while(isRecording == true){
            int idx = 0;
            size = audioRecord.read(sAudioData,0,bufferSize);
            for (int i = 0; i < sAudioData.length; i++) {
                bAudioData [idx++] = (byte) (sAudioData[i] & 0x00ff);
                bAudioData [idx++] = (byte) ((sAudioData[i] & 0xff00) >>> 8);
            }
            if (AudioRecord.ERROR_INVALID_OPERATION != size){
                try{

                        fos.write(bAudioData);

                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        try{
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /*
     *存储pcm音频流文件名的格式
     */
    private String audioName(){
        String audioName = new SimpleDateFormat("yyyy-MM-dd hhmmss").format(new Date());
        return audioName = "Record" + audioName + ".pcm";
    }

    /*
     * 停止录音
     */
    public void stopRecord(){
        if(audioRecord != null){
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
            isRecording = false;
        }
    }
}
