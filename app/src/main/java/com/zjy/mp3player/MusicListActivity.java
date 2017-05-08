package com.zjy.mp3player;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zjy.mp3player.adapter.SongListAdapter;
import com.zjy.mp3player.model.MusicInfo;

import java.util.ArrayList;

public class MusicListActivity extends AppCompatActivity {

    private ListView songList;
    private ArrayList<MusicInfo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = getIntent().getParcelableArrayListExtra("list");
        songList = (ListView) findViewById(R.id.song_list);
        SongListAdapter adapter = new SongListAdapter(getApplicationContext(), list);
        songList.setAdapter(adapter);

        songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("index", position);
                setResult(100, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
