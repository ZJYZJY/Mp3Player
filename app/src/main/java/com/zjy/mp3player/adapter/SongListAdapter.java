package com.zjy.mp3player.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjy.mp3player.model.MusicInfo;
import com.zjy.mp3player.R;

import java.util.ArrayList;

/**
 * com.zjy.mp3player
 * Created by 73958 on 2017/5/7.
 */

public class SongListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MusicInfo> songList;

    public SongListAdapter(Context context, ArrayList<MusicInfo> list) {
        this.context = context;
        this.songList = list;
    }

    @Override
    public int getCount() {
        return songList == null ? 0 : songList.size();
    }

    @Override
    public Object getItem(int position) {
        return songList == null ? null : songList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_music, null);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.songName.setText(songList.get(position).getMusicName());
        holder.songInfo.setText(songList.get(position).getArtist());
        return convertView;
    }

    class ViewHolder{

        TextView songName;
        TextView songInfo;

        ViewHolder(View itemView) {
            songName = (TextView)itemView.findViewById(R.id.song_name);
            songInfo = (TextView)itemView.findViewById(R.id.song_info);
        }
    }
}
