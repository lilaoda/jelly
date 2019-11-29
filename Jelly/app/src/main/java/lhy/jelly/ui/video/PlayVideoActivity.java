package lhy.jelly.ui.video;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.lhy.player.LhyVideoView;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.JzvdStd;
import lhy.ijkplayer.media.AndroidMediaController;
import lhy.jelly.R;
import lhy.jelly.base.BaseActivity;
import lhy.jelly.base.Constant;
import lhy.jelly.bean.VideoBean;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by Lihy on 2018/6/4 14:13
 * E-Mail ：liheyu999@163.com
 */
@Route(path = Constant.ROUTE_PATH_PLAY_VIDEO_ACTIVITY)
public class PlayVideoActivity extends BaseActivity {

    //    @Autowired(name = "video")
    VideoBean videoBean;

    @BindView(R.id.videoView)
    LhyVideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ARouter.getInstance().inject(this);
        setContentView(R.layout.activity_play_video);
        ButterKnife.bind(this);
        initToolbar("视频");
        videoBean = getIntent().getParcelableExtra("video");
        initView();
        Logger.d(new Gson().toJson(videoBean));
//        ThumbnailUtils。
        AndroidMediaController controller;
        VideoView v;
        findViewById(R.id.btn_actionbar).setOnClickListener(v2->{
//            if(videoView.isPlaying()){
////                PlayManager.release();
////            }else {
////                videoView.play();
////            }
        });
    }

    private void initView() {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        String url = "http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4";
//        videoView.setVideoPath(videoBean.getPath());
        Uri parse = Uri.parse(videoBean.getPath());
        Logger.d(parse.toString());
//        videoView.setVideoPath(videoBean.getPath());
        videoView.setVideoPath(url);
        videoView.setTitle(videoBean.getTitle());
        videoView.setDuration(videoBean.getDuration());


        JzvdStd jzvdStd = (JzvdStd) findViewById(R.id.jz_video);
        jzvdStd.setUp(videoBean.getPath(),"abc");
//        jzvdStd.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
//                , "饺子闭眼睛");

//        jzvdStd.thumbImageView.setim("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
    }
}
