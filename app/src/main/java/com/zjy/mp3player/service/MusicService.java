package com.zjy.mp3player.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.zjy.mp3player.listener.OnMusicChangedListener;
import com.zjy.mp3player.model.MusicInfo;

import java.util.ArrayList;

public class MusicService extends Service {

    public MediaPlayer mediaPlayer = new MediaPlayer();
    public String initSong;
    public int playingIndex = 0;
    private OnMusicChangedListener listener;

    public static ArrayList<MusicInfo> musicInfos;

    public MusicService() {
        if(musicInfos.size() > 0){
            initSong = musicInfos.get(0).getData();
            play(initSong, playingIndex);
        }
    }

    // 通过 Binder 来保持 Activity 和 Service 的通信
    public MyBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public void play(String song, int index){
        try {
            mediaPlayer.reset(); //重置多媒体
            mediaPlayer.setDataSource(song);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            playingIndex = index;
            if(listener != null){
                listener.onMusicChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play(int index){
        try {
            mediaPlayer.reset(); //重置多媒体
            mediaPlayer.setDataSource(musicInfos.get(index).getData());
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            playingIndex = index;
            listener.onMusicChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playOrPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    public void next(){
        if(musicInfos.size() > playingIndex + 1){
            play(playingIndex + 1);
        }else {
            Toast.makeText(this, "没有下一首了", Toast.LENGTH_SHORT).show();
        }
    }

    public void previous(){
        if(playingIndex - 1 >= 0){
            play(playingIndex - 1);
        }else {
            Toast.makeText(this, "没有上一首了", Toast.LENGTH_SHORT).show();
        }
    }

    public void stop(){
        mediaPlayer.stop();
    }

    public void setOnMusicChangedListener(OnMusicChangedListener listener){
        this.listener = listener;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}

