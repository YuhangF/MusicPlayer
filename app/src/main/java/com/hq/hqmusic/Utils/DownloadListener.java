package com.hq.hqmusic.Utils;

public interface DownloadListener {
    //定义一个回调接口

    void onProgress(int progress);
    void onSuccess();
    void onFailed();
    void onPaused();
    void onCanceled();

}