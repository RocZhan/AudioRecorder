package com.hust.zp.audiorecorder;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by zhanpeng on 2017/4/26.
 */

public class AudioPlayer {

    private int sampleRateInHz;
    private int bufferSize;
    //private short[] audioData;
    private AudioTrack audioTrack;
    private String fileName;

    public AudioPlayer(){
        //initParam();
    }

    public AudioPlayer(String fileName){
        this.fileName = fileName;
        //initParam();
    }




    public void startPlay(){

        try{
        sampleRateInHz = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);
        bufferSize = AudioTrack.getMinBufferSize(sampleRateInHz,AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,sampleRateInHz,AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT,bufferSize,AudioTrack.MODE_STREAM);
        //audioData = new short[AudioRecorder.sAudioData.length];
        int size;
        audioTrack.play();
        size = audioTrack.write(AudioRecorder.sAudioData,0,AudioRecorder.sAudioData.length);
        Log.d(TAG, "startPlay: success!" + size);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
/*
    public void startPlay(){

        //DataInputStream dis ;
        File file = new File(fileName);
        int pcmLength = (int)(file.length()/2);
        short[] pcm = new short[pcmLength];
        byte[] generatedSnd = new byte[2 * pcmLength];
        try{
            InputStream is = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);

            int j = 0;
            while(dis.available() > 0){
                pcm[j] = dis.readShort();
                j++;
            }

            dis.close();

            /*
            int i;
            int idx = 0;

            int ramp = pcmLength / 20;

            for (i = 0; i < ramp; i++) { // Ramp amplitude up (to avoid clicks)

                final short val = (short) (pcm[i] * 32767 * i / ramp);
                generatedSnd[idx++] = (byte) (val & 0x00ff);
                generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

            }

            for (i = ramp; i < pcmLength - ramp; i++) {   // Max amplitude for most of the samples

                final short val = (short) (pcm[i] * 32767);
                generatedSnd[idx++] = (byte) (val & 0x00ff);
                generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

            }

            for (i = pcmLength - ramp;i < pcmLength;i++){

                final short val = (short) (pcm[i] * 32767 * (pcmLength - i) / ramp);
                generatedSnd[idx++] = (byte) (val & 0x00ff);
                generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

            }

            audioTrack.play();
            audioTrack.write(pcm,0,pcm.length);
        }catch(Throwable t){
            t.printStackTrace();
        }
    }
*/
    public void stop(){
        if (audioTrack != null){
            audioTrack.stop();
            audioTrack.release();
            audioTrack = null;
        }
    }
}
