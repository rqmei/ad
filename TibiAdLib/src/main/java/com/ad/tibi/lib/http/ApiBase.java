package com.ad.tibi.lib.http;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 请求接口的订阅统一处理
 */
public class ApiBase {
    /**
     * 用于集中管理Observable订阅
     */
    CompositeDisposable mCompositeDisposable;

    /**
     * RXjava注册
     *
     * @param disposable
     */
    public void addDispose(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        // 将所有disposable放入,集中处理
        mCompositeDisposable.add(disposable);
    }

    /**
     * RXjava取消注册，以避免内存泄露
     */
    public void unDispose() {
        if (mCompositeDisposable != null) {
            // 保证activity结束时取消所有正在执行的订阅
            mCompositeDisposable.clear();
        }
    }
}
