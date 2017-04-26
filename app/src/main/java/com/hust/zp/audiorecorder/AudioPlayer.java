package com.hust.zp.audiorecorder;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by zhanpeng on 2017/4/26.
 */

public class AudioPlayer {

    private int sampleRateInHz;
    private int bufferSize;
    private AudioTrack audioTrack;
    private String fileName;

    public AudioPlayer(String fileName){
        this.fileName = fileName;
        initParam();
    }


    public void initParam(){

        sampleRateInHz = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);
        bufferSize = AudioTrack.getMinBufferSize(sampleRateInHz,AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,sampleRateInHz,AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT,bufferSize,AudioTrack.MODE_STREAM);
    }

    public void startPlay(){

        //DataInputStream dis ;
        File file = new File(fileName);
        int pcmLength = (int)(file.length()/2);
        short[] pcm = new short[pcmLength];
        try{
            InputStream is = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);

            int i = 0;
            while(dis.available() > 0){
                pcm[i] = dis.readShort();
                i++;
            }

            dis.close();

            audioTrack.play();
            audioTrack.write(pcm,0,pcm.length);
            audioTrack.stop();
        }catch(Throwable t){
            t.printStackTrace();
        }
    }

    public void stop(){
        if (audioTrack != null){
            audioTrack.stop();
            audioTrack.release();
            audioTrack = null;
        }
    }
}
