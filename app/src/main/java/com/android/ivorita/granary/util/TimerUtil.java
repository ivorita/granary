package com.android.ivorita.granary.util;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static android.content.ContentValues.TAG;

public class TimerUtil {

    private Disposable mDisposable;

    /**
     * 每隔milliseconds毫秒后执行指定动作
     */
    public void interval(long milliSeconds, final RxAction rxAction) {
        Observable.interval(milliSeconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        if (rxAction != null) {
                            rxAction.action(number);
                        }
                        Log.d(TAG, "onNext: " + number);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 取消订阅
     */
    public void cancel() {
        //isDisposed() 查询是否解除订阅，true 代表已经解除订阅
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose(); //dispose() 主动解除订阅
            Log.d(TAG, "定时器取消");
        }
    }

    public interface RxAction {
        /**
         * 让调用者指定指定动作
         */
        void action(long number);
    }

}
