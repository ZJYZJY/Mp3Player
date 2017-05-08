package com.zjy.mp3player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zjy.mp3player.MainActivity;
import com.zjy.mp3player.service.MusicService;
import com.zjy.mp3player.R;
import com.zjy.mp3player.adapter.SongListAdapter;

/**
 * com.zjy.mp3player.fragment
 * Created by 73958 on 2017/5/8.
 */

public class MusicListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView musicListView;
    private SongListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);
        musicListView = (ListView) view.findViewById(R.id.music_list);
        update();
        musicListView.setOnItemClickListener(this);
        return view;
    }

    public void update(){
        adapter = new SongListAdapter(getContext(), MusicService.musicInfos);
        musicListView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MainActivity aty = (MainActivity)getActivity();
        aty.musicService.play(aty.musicArrayList.get(position).getData(), position);
        aty.btnPlayOrPause.performClick();
        aty.viewPager.setCurrentItem(1);
    }
}
