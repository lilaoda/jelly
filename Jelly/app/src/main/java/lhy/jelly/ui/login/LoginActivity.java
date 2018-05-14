package lhy.jelly.ui.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lhy.jelly.R;
import lhy.jelly.ui.main.MainActivity;
import lhy.lhylibrary.base.LhyActivity;
import lhy.lhylibrary.http.exception.ApiException;
import lhy.zxinglibrary.zxing.decode.QRCodeDecoder;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import top.zibin.luban.Luban;

/**
 * Created by Liheyu on 2017/9/7.
 * Email:liheyu999@163.com
 */

public class LoginActivity extends LhyActivity {

    private static final int CODE_REQUEST_IMG = 10;
    @BindView(R.id.edit_account)
    EditText editAccount;
    @BindView(R.id.edit_password)
    EditText editPassword;
    private ArrayList<String> mImgList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        startActivity(new Intent(this, MainActivity.class));
//        DownloadManager downloadManager = new DownloadManager(this);
//        downloadManager.updateAPP("http://192.168.8.62:8080/imageLoad/pda.apk", FileUtils.getSDCardPath()+"/jelly/");
//
//    openAlbum();
    }

    private void openAlbum(){
        MultiImageSelector.create().count(1)
                .showCamera(true)
                .origin(mImgList)
                .start(LoginActivity.this, CODE_REQUEST_IMG);
//        TranslateAnimation
//        Transition transition = TransitionInflater.from(this).inflateTransition(1);
//        getWindow().setEnterTransition(transition);
//        ActivityOptions.makeSceneTransitionAnimation()
//        TransitionManager.go();
//        ViewAnimationUtils.createCircularReveal(editAccount,)
    }
    private void testQRcode(){
        Disposable subscribe = Flowable.just(mImgList)
                .map(new Function<List<String>, String>() {
                    @Override
                    public String apply(List<String> list) throws Exception {
                        Logger.d(list.get(0));
                        String s3 = QRCodeDecoder.decodeQRCodeSync(QRCodeDecoder.drawableToBitmap(BitmapFactory.decodeFile(mImgList.get(0))));
                        Logger.d("压缩前："+s3);
                        String imgDir = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"test"+File.separator;
                        File file1 = new File(imgDir);
                        if(!file1.exists())file1.mkdirs();
                        List<File> files = Luban.with(LoginActivity.this).load(list.get(0)).setTargetDir(imgDir).get();
                        if (files.size() == 0|| files.get(0).length()==0) throw new ApiException("图片压缩失败");
                        File file = files.get(0);
                        File savedFile = new File(file.getParentFile(),System.currentTimeMillis()+"2.jpg");
                        boolean b = file.renameTo(savedFile);
                        Logger.d("savedFile:"+savedFile.length());
                        Logger.d("savedFile:"+savedFile.getAbsolutePath());
                        Bitmap bitmap = BitmapFactory.decodeFile(savedFile.getAbsolutePath());
                        String s = QRCodeDecoder.decodeQRCodeSync(QRCodeDecoder.drawableToBitmap(bitmap));
                        Logger.d("压缩后:填充白色底："+s);
                        String s2 = QRCodeDecoder.decodeQRCodeSync(bitmap);
                        Logger.d("压缩后:无填充白色底："+s2);
                        if (TextUtils.isEmpty(s)) throw new ApiException("条形码解析失败");
                        return s;
                    }
                }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        editAccount.setText(s);
                        Logger.d(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        editAccount.setText(throwable.getMessage());
                        Logger.d(throwable.getMessage());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CODE_REQUEST_IMG) {
                List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                mImgList.clear();
                mImgList.addAll(paths);
                testQRcode();
            }
        }
    }
}
