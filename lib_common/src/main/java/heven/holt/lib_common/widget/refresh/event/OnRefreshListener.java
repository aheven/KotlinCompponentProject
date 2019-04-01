package heven.holt.lib_common.widget.refresh.event;

public interface OnRefreshListener {
    void onRefreshing();

    void startMoveRefreshing();

    void endMoveRefreshing();
}
