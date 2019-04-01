package heven.holt.lib_common.widget.refresh;

import android.view.View;

public interface ILoadMoreView {
    /**
     * 重置
     */
    void reset();

    /**
     * 加载中
     */
    void loading();

    /**
     * 加载完成
     */
    void loadComplete();

    void loadFail();

    void loadNothing();

    View getCanClickFailView();


}
