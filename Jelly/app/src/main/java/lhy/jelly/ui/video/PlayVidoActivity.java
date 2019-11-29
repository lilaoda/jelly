package lhy.jelly.ui.video;

import lhy.jelly.base.BaseActivity;

/**
 * Created by Lihy on 2018/6/4 14:13
 * E-Mail ：liheyu999@163.com
 */
//@Route(path = Constant.ROUTE_PATH_PLAY_VIDEO_ACTIVITY)
public class PlayVidoActivity extends BaseActivity {

////    @Autowired(name = "video")
//    VideoBean videoBean;
//
//    @BindView(R.id.playView)
//    PlayerView playView;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        ARouter.getInstance().inject(this);
//        setContentView(R.layout.activity_play_video);
//        ButterKnife.bind(this);
//        Toolbar viewById = findViewById(R.id.toolbar);
//        setSupportActionBar(viewById);
//        videoBean  = getIntent().getParcelableExtra("video");
//        initView();
//        Logger.d(new Gson().toJson(videoBean));
//    }
//
//    private void initView() {
//        SimpleExoPlayer simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this);
//        playView.setPlayer(simpleExoPlayer);
//        String userAgent = Util.getUserAgent(this, "jelly");
//        ProgressiveMediaSource.Factory factory = new ProgressiveMediaSource.Factory(
//                new DefaultDataSourceFactory(this, userAgent));
//        ProgressiveMediaSource mediaSource = factory.createMediaSource(Uri.parse(videoBean.getPath()));
//        simpleExoPlayer.prepare(mediaSource);
//        simpleExoPlayer.setPlayWhenReady(true);
////        DownloadService
////        DownloadManager
//    }
}
