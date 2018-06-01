package lhy.jelly.bean;

/**
 * Created by Lihy on 2018/4/20 14:59
 * E-Mail ：liheyu999@163.com
 */
public class VideoBean {

    private String path;
    private String title;
    private long duration;
    private String thumbPath;

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
}
