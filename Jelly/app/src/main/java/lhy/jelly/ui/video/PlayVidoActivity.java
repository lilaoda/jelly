package lhy.jelly.ui.video;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;

import com.orhanobut.logger.Logger;

import lhy.jelly.R;
import lhy.jelly.base.BaseActivity;
import lhy.jelly.view.VideoView2;
import lhy.jelly.view.VideoView3;

/**
 * Created by Lihy on 2018/6/4 14:13
 * E-Mail ：liheyu999@163.com
 */
public class PlayVidoActivity extends BaseActivity {


    private VideoView2 videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        android.support.v7.widget.Toolbar viewById = findViewById(R.id.toolbar);
        setSupportActionBar(viewById);
        Logger.d("onCreate");
        initView();

        TextureView textureView = findViewById(R.id.textureView);
//        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
//            @Override
//            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
//                final IMediaPlayer mediaPlayer = new IjkMediaPlayer();
//                mediaPlayer.setSurface(new Surface(surface));
//                try {
//                    mediaPlayer.setDataSource(Uri.parse("/storage/emulated/0/tencent/MicroMsg/bb6b5135d05871ee089533b90d0437ca/video/1338382512170b1319680770.mp4").toString());
//                    mediaPlayer.prepareAsync();
////                    mediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
////                        @Override
////                        public void onPrepared(IMediaPlayer iMediaPlayer) {
////                         //   mediaPlayer.start();
////                        }
////                    });
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
//
//            }
//
//            @Override
//            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
//                return false;
//            }
//
//            @Override
//            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//
//            }
//        });

    }

    //{"duration":34268,"path":"/storage/emulated/0/b8273312cb47a1bc311094e634bf20e6.mp4"}]
    //"duration":70144,"path":"/storage/emulated/0/tencent/MicroMsg/bb6b5135d05871ee089533b90d0437ca/video/1338382512170b1319680770.mp4"
    private void initView() {
        VideoView3 videoView3 = findViewById(R.id.video_view3);
        videoView3.setVideoPath("/storage/emulated/0/tencent/MicroMsg/bb6b5135d05871ee089533b90d0437ca/video/1338382512170b1319680770.mp4");
//        videoView.setActivity(this);
//        videoView.setVideoPath("/storage/emulated/0/tencent/MicroMsg/bb6b5135d05871ee089533b90d0437ca/video/1338382512170b1319680770.mp4");
//        JZVideoPlayerStandard jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
//        jzVideoPlayerStandard.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4",
//        jzVideoPlayerStandard.setUp("/storage/emulated/0/b8273312cb47a1bc311094e634bf20e6.mp4",
//                JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,
//                "饺子闭眼睛");
//        jzVideoPlayerStandard.thumbImageView.setImageResource(R.mipmap.test1);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // videoView.setConfiguration(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_toggle_ratio) {
//            videoView.toggleAspectRatio();
//        }
        return super.onOptionsItemSelected(item);
    }
}
