package com.zjy.mp3player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zjy.mp3player.MainActivity;
import com.zjy.mp3player.R;

/**
 * com.zjy.mp3player.fragment
 * Created by 73958 on 2017/5/8.
 */

public class AlbumPicFragment extends Fragment {

    public ImageView albumImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_pic, container, false);
        albumImage = (ImageView) view.findViewById(R.id.Image);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity aty = (MainActivity)getActivity();
        aty.startSearch();
        aty.mediaControl();
    }
}
