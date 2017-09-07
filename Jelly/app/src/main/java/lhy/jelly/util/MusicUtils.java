package lhy.jelly.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liheyu on 2017/9/7.
 * Email:liheyu999@163.com
 */

public class MusicUtils {

    public static List<MusicBean> getMp3Infos(@NonNull Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        List<MusicBean> mp3Infos = new ArrayList<>();
        Log.d("searchMusic", cursor.getCount()+"");
        for (int i = 0; i < cursor.getCount(); i++) {
            //新建一个歌曲对象,将从cursor里读出的信息存放进去,直到取完cursor里面的内容为止.
            cursor.moveToNext();
            long id = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));   //音乐id

            String title = cursor.getString((cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE)));//音乐标题

            String artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));//艺术家

            long duration = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));//时长

            long size = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE));  //文件大小

            String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));  //文件路径

            String album = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM)); //唱片图片

            long album_id = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)); //唱片图片ID

            int isMusic = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));//是否为音乐

            MusicBean mp3Info = new MusicBean();
            mp3Info.setId(id);
            mp3Info.setTitle(title);
            mp3Info.setArtist(artist);
            mp3Info.setDuration(duration);
            mp3Info.setSize(size);
            mp3Info.setUrl(url);
            mp3Info.setAlbum(album);
            mp3Info.setAlbum_id(album_id);
            mp3Infos.add(mp3Info);
            Log.d("searchMusic", mp3Info.toString());
        }
        return mp3Infos;
    }
}
