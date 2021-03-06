package com.zjy.mp3player;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

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
        MusicService getService() {
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
            listener.onMusicChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void play(int index){
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

    public void setOnMusicChangedListener(OnMusicChangedListener listener){
        this.listener = listener;
    }

//    public void stop() {
//        if (mediaPlayer != null) {
//            mediaPlayer.stop();
//            try {
//                mediaPlayer.reset();
//                mediaPlayer.setDataSource("/storage/emulated/0/netease/cloudmusic/Music/李玉刚 - 刚好遇见你.mp3");
//                mediaPlayer.prepare();
//                mediaPlayer.seekTo(0);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}

