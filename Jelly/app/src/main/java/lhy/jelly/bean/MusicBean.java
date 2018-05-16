package lhy.jelly.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Liheyu on 2017/9/7.
 * Email:liheyu999@163.com
 * 音乐类
 */

public class MusicBean implements Parcelable{

    private long id;
    private String title;
    private String artist;
    private long duration;
    private long size;
    private String url;
    private String album;
    private String albumArt;
    private long album_id;
    private int isMusic;


    public MusicBean() {
    }

    protected MusicBean(Parcel in) {
        id = in.readLong();
        title = in.readString();
        artist = in.readString();
        duration = in.readLong();
        size = in.readLong();
        url = in.readString();
        album = in.readString();
        albumArt = in.readString();
        album_id = in.readLong();
        isMusic = in.readInt();
    }

    public static final Creator<MusicBean> CREATOR = new Creator<MusicBean>() {
        @Override
        public MusicBean createFromParcel(Parcel in) {
            return new MusicBean(in);
        }

        @Override
        public MusicBean[] newArray(int size) {
            return new MusicBean[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(long album_id) {
        this.album_id = album_id;
    }

    public int getIsMusic() {
        return isMusic;
    }

    public void setIsMusic(int isMusic) {
        this.isMusic = isMusic;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    @Override
    public String toString() {
        return "MusicBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", url='" + url + '\'' +
                ", album='" + album + '\'' +
                ", albumArt='" + albumArt + '\'' +
                ", album_id=" + album_id +
                ", isMusic=" + isMusic +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeLong(duration);
        dest.writeLong(size);
        dest.writeString(url);
        dest.writeString(album);
        dest.writeString(albumArt);
        dest.writeLong(album_id);
        dest.writeInt(isMusic);
    }
}
