package com.zjy.mp3player;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;

/**
 * com.zjy.mp3player
 * Created by 73958 on 2017/5/5.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnMusicChangedListener {
    private TextView musicStatus, musicTime, musicTotal, musicTitle;
    private SeekBar seekBar;

    private ImageButton btnPlayOrPause, btnQuit, btnList, btnPre, btnNext;
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");
    private ImageView albumImage, albumBackground;

    private boolean tag1 = false;
    private boolean tag2 = false;
    private MusicService musicService;

    public static ArrayList<MusicInfo> musicArrayList;

    // 回调onServiceConnected 函数，通过IBinder 获取 Service对象，实现Activity与 Service的绑定
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = ((MusicService.MyBinder) (service)).getService();
            musicService.setOnMusicChangedListener(MainActivity.this);
            musicTotal.setText(time.format(musicService.mediaPlayer.getDuration()));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };

    // 在Activity中调用 bindService 保持与 Service 的通信
    private void bindServiceConnection() {
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        startService(intent);
        bindService(intent, serviceConnection, this.BIND_AUTO_CREATE);
        if(musicArrayList.size() > 0) {
            musicTitle.setText(musicArrayList.get(0).getMusicName());
            // 设置专辑封面
            String albumPath = getAlbumArt(musicArrayList.get(0).getAlbumId());
            Bitmap albumPic = BitmapFactory.decodeFile(albumPath);
            albumImage.setImageBitmap(albumPic);
            Blurry.with(getApplicationContext()).radius(50).color(0x933a3a3a).from(albumPic).into(albumBackground);
        }
    }

    // 通过 Handler 更新 UI 上的组件状态
    public Handler handler = new Handler();
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            musicTime.setText(time.format(musicService.mediaPlayer.getCurrentPosition()));
            seekBar.setProgress(musicService.mediaPlayer.getCurrentPosition());
            seekBar.setMax(musicService.mediaPlayer.getDuration());
            musicTotal.setText(time.format(musicService.mediaPlayer.getDuration()));
            handler.postDelayed(runnable, 200);
        }
    };

    private void findViewById() {
        musicTime = (TextView) findViewById(R.id.MusicTime);
        musicTotal = (TextView) findViewById(R.id.MusicTotal);
        seekBar = (SeekBar) findViewById(R.id.MusicSeekBar);
        musicStatus = (TextView) findViewById(R.id.MusicStatus);
        musicTitle = (TextView) findViewById(R.id.music_title);

        albumImage = (ImageView) findViewById(R.id.Image);
        albumBackground = (ImageView) findViewById(R.id.blur_bg);

        btnPlayOrPause = (ImageButton) findViewById(R.id.BtnPlayorPause);
        btnPre = (ImageButton) findViewById(R.id.Btn_pre);
        btnNext = (ImageButton) findViewById(R.id.Btn_next);
        btnPre.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        btnList = (ImageButton) findViewById(R.id.Btn_list);
        btnQuit = (ImageButton) findViewById(R.id.Btn_exit);
        btnList.setOnClickListener(this);
        btnQuit.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.getBackground().setAlpha(8);
        setSupportActionBar(toolbar);
        findViewById();
        startSearch();
        mediaControl();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true) {
                    musicService.mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void mediaControl(){
        ImageView imageView = (ImageView) findViewById(R.id.Image);
        final ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360.0f);
        animator.setDuration(10000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);

        btnPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicService.mediaPlayer != null) {
                    seekBar.setProgress(musicService.mediaPlayer.getCurrentPosition());
                    seekBar.setMax(musicService.mediaPlayer.getDuration());
                    if (!musicService.mediaPlayer.isPlaying()) {
                        btnPlayOrPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                        musicStatus.setText("Playing");
                        musicService.playOrPause();

                        if (!tag1) {
                            animator.start();
                            tag1 = true;
                        } else {
                            animator.resume();
                        }
                    } else {
                        btnPlayOrPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                        musicStatus.setText("Paused");
                        musicService.playOrPause();
                        animator.pause();
                    }
                    if (!tag2) {
                        handler.post(runnable);
                        tag2 = true;
                    }
                }
            }
        });
    }

    private Handler searchHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(MainActivity.this, "本地音乐搜索完成", Toast.LENGTH_SHORT).show();
                    MusicService.musicInfos = musicArrayList;
                    bindServiceConnection();
                    break;
                default:
                    break;
            }
        }
    };

    private void startSearch(){
        new Thread(new Runnable() {
            public void run() {
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;//-------------
                ContentResolver cr = MainActivity.this.getContentResolver();
                StringBuilder select = new StringBuilder(" 1=1 ");
                // 查询语句：检索出时长大于1分钟，文件大小大于1MB的媒体文件
                select.append(" and " + MediaStore.Audio.Media.SIZE + " > " + MusicUtils.FILTER_SIZE);
                select.append(" and " + MediaStore.Audio.Media.DURATION + " > " + MusicUtils.FILTER_DURATION);
                Log.d("in", select.toString());
                musicArrayList = MusicUtils.getMusicList(cr.query(uri, MusicUtils.proj_music,
                        select.toString(), null,
                        MediaStore.Audio.Media.ARTIST_KEY));
                Message msg = new Message();
                msg.what = 1;
                searchHandler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public void onMusicChanged() {
        if(musicArrayList.size() > 0) {
            musicTitle.setText(musicArrayList.get(musicService.playingIndex).getMusicName());
            // 设置专辑封面
            String albumPath = getAlbumArt(musicArrayList.get(musicService.playingIndex).getAlbumId());
            Bitmap albumPic = BitmapFactory.decodeFile(albumPath);
            albumImage.setImageBitmap(albumPic);
            Blurry.with(getApplicationContext()).radius(50).color(0x933a3a3a).from(albumPic).into(albumBackground);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Btn_list:
                Intent intent = new Intent(MainActivity.this, MusicListActivity.class);
                intent.putExtra("list", musicArrayList);
                startActivityForResult(intent, 0);
                break;
            case R.id.Btn_exit:
                musicService.mediaPlayer.stop();
                handler.removeCallbacks(runnable);
                unbindService(serviceConnection);
                stopService(new Intent(MainActivity.this, MusicService.class));
                try {
                    MainActivity.this.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.Btn_pre:
                musicService.previous();
                btnPlayOrPause.performClick();
                break;
            case R.id.Btn_next:
                musicService.next();
                btnPlayOrPause.performClick();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if(resultCode == 100){
                int playingIndex = data.getIntExtra("index", 0);
                musicService.play(musicArrayList.get(playingIndex).getData(), playingIndex);
                btnPlayOrPause.performClick();
            }
        }
    }

    // 获取专辑图片
    public String getAlbumArt(int album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[] { "album_art" };
        Cursor cur = this.getContentResolver().query(
                Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),
                projection, null, null, null);
        String album_art = null;
        if (cur != null && cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
            cur.close();
        }
        return album_art;
    }

    // 获取并设置返回键的点击事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}



