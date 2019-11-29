package lhy.jelly.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lihy on 2018/4/20 14:59
 * E-Mail ：liheyu999@163.com
 */
public class VideoBean implements Parcelable {

    private String path;
    private String title;
    private long duration;
    private String thumbPath;

    public VideoBean() {
    }

    protected VideoBean(Parcel in) {
        path = in.readString();
        title = in.readString();
        duration = in.readLong();
        thumbPath = in.readString();
    }

    public static final Creator<VideoBean> CREATOR = new Creator<VideoBean>() {
        @Override
        public VideoBean createFromParcel(Parcel in) {
            return new VideoBean(in);
        }

        @Override
        public VideoBean[] newArray(int size) {
            return new VideoBean[size];
        }
    };

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    @Override
    public String toString() {
        return "VideoBean{" +
                "path='" + path + '\'' +
                ", title='" + title + '\'' +
                ", duration=" + duration +
                ", thumbPath='" + thumbPath + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(title);
        dest.writeLong(duration);
        dest.writeString(thumbPath);
    }
}
