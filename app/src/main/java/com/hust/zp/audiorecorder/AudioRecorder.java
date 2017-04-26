package com.hust.zp.audiorecorder;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhanpeng on 2017/4/24.
 */

public class AudioRecorder {

    //public static final int RECORDER_SAMPLE_RATE = 44100;
    //指定为单声道;选择PCM编码，一个抽样点16bit;
    public static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    public static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private int sampleRateInHz;

    public int bufferSize = 0;
    public  static boolean isRecording;
    private AudioRecord audioRecord;


    /*
     *开始录音
     */
    public void startRecord(){
        //获得手机的抽样率：老一点的手机为44100hz，新一点的手机是48000hz
        sampleRateInHz = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);
        Log.i("Roc", "initParam: "+sampleRateInHz);
        //根据抽样率，声道，编码位数得到最小的缓存区，然后对audioRecord实例化
        bufferSize = AudioRecord.getMinBufferSize(sampleRateInHz,RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,sampleRateInHz,RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING,bufferSize);

        try{
            audioRecord.startRecording();//开始录音
            isRecording = true;
        }catch (IllegalStateException e){
            e.printStackTrace();
        }

        //新建线程将录音写到文件中
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeDataToFile();
            }
        }).start();

    }

    /*
     *将录音的音频存入文件中，以便后面进行播放
     */
    private void writeDataToFile(){
        //录音使用16位的编码方式，对于read方法最好使用short存取buffersize中的数据
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
                //将short[]转成byte[]
                bAudioData [idx++] = (byte) (sAudioData[i] & 0x00ff);
                bAudioData [idx++] = (byte) ((sAudioData[i] & 0xff00) >>> 8);
            }
            if (AudioRecord.ERROR_INVALID_OPERATION != size){
                try{

                        fos.write(bAudioData);//write方法不能写short[]，所以写入byte[]
                    //Log.i(Thread.currentThread().getName(), "writeDataToFile: Success!");

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
        //定义文件名的格式
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
            Log.d(Thread.currentThread().getName(), "stopRecord: Success!");
        }
    }
}
