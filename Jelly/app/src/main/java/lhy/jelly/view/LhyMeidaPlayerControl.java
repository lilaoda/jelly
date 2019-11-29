package lhy.jelly.view;

import android.widget.MediaController;

/**
 * author: liheyu
 * date: 2019-11-19
 * email: liheyu999@163.com
 */
public interface LhyMeidaPlayerControl extends MediaController.MediaPlayerControl {

    void enterFullScreen();

    void exitFullScreen();
}
