package lhy.jelly.ui.music;

import android.app.Application;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import lhy.jelly.base.Resource;
import lhy.jelly.bean.MusicBean;
import lhy.jelly.util.MusicUtils;
import lhy.jelly.util.RxUtils;

/**
 * Created  on 2018/7/25 15:24
 * E-Mail ：liheyu999@163.com
 *
 * @author lihy
 */
public class MusicModel extends AndroidViewModel {

    private MutableLiveData<List<MusicBean>> musicData = new MutableLiveData<>();
    private MutableLiveData<Resource<List<MusicBean>>> musicResource = new MutableLiveData<>();

    public LiveData<List<MusicBean>> getMusicData() {
        return musicData;
    }

    public MutableLiveData<Resource<List<MusicBean>>> getMusicResource() {
        return musicResource;
    }

    @Inject
    public MusicModel(@NonNull Application application) {
        super(application);
    }

    public Observable<List<MusicBean>> getLocalMusic() {
        return RxUtils.wrapRx(Observable.create(e -> {
            e.onNext(MusicUtils.getMp3Infos(getApplication()));
            e.onComplete();
        }));
    }

    public void doRefresh() {
        Observable<List<MusicBean>> observable = Observable.create(e -> {
            SystemClock.sleep(5000);
            e.onNext(MusicUtils.getMp3Infos(getApplication()));
            e.onComplete();
        });
        RxUtils.wrapLiveData(observable, musicResource);
//        RxUtils.wrapRx(observable)
//                .doOnSubscribe(s->musicResource.setValue(Resource.loading()))
//                .subscribe(new HttpObserver<List<MusicBean>>() {
//                    @Override
//                    public void onSuccess(List<MusicBean> value) {
//                        musicResource.setValue(Resource.success(value));
//                    }
//
//                    @Override
//                    public void onFailure(String msg) {
//                        musicResource.setValue(Resource.error(msg));
//                    }
//                });
    }
}
