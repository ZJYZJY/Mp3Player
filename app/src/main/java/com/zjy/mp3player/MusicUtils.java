package com.zjy.mp3player;

import java.io.File;
import java.util.ArrayList;

import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

/**
 * com.zjy.mp3player
 * Created by 73958 on 2017/5/5.
 */

public class MusicUtils {

    public static final int FILTER_SIZE = 1 * 1024 * 1024;// 1MB
    public static final int FILTER_DURATION = 1 * 60 * 1000;// 1分钟

    public static String[] proj_music = new String[]{
            MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.DURATION};

    // 音乐检索方法
    public static ArrayList<MusicInfo> getMusicList(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        ArrayList<MusicInfo> musicList = new ArrayList<MusicInfo>();
        while (cursor.moveToNext()) {
            MusicInfo music = new MusicInfo();
            music.songId = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));
            music.albumId = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            music.duration = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));
            Log.i("Name", "" + music.duration);//输出时长
            music.musicName = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE));
            Log.i("Name", music.musicName);//输出名称
            music.artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));

            String filePath = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));
            music.data = filePath;
            Log.i("Name", music.data);//输出路径
            String folderPath = filePath.substring(0,
                    filePath.lastIndexOf(File.separator));
            music.folder = folderPath;
            musicList.add(music);
        }
        cursor.close();
        return musicList;
    }

    static class StringHelper {
        public static String getPingYin() {
            return null;
        }

        private static final int GB_SP_DIFF = 160;

        private static final int[] secPosvalueList = {
                1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787,
                3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027, 4086,
                4390, 4558, 4684, 4925, 5249, 5600};

        private static final char[] firstLetter = {
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
                't', 'w', 'x', 'y', 'z'};

        public static String getFirstLetter(String oriStr) {
            String str = oriStr.toLowerCase();
            StringBuffer buffer = new StringBuffer();
            char ch;
            char[] temp;
            for (int i = 0; i < str.length(); i++) { //依次处理str中每个字符
                ch = str.charAt(i);
                temp = new char[]{ch};
                byte[] uniCode = new String(temp).getBytes();
                if (uniCode[0] < 128 && uniCode[0] > 0) { // 非汉字
                    buffer.append(temp);
                } else {
                    buffer.append(convert(uniCode));
                }
            }
            return buffer.toString();
        }

        private static char convert(byte[] bytes) {

            char result = '-';
            int secPosvalue = 0;
            int i;
            for (i = 0; i < bytes.length; i++) {
                bytes[i] -= GB_SP_DIFF;
            }
            secPosvalue = bytes[0] * 100 + bytes[1];
            for (i = 0; i < 23; i++) {
                if (secPosvalue >= secPosvalueList[i] &&
                        secPosvalue < secPosvalueList[i + 1]) {
                    result = firstLetter[i];
                    break;
                }
            }
            return result;
        }
    }
}
