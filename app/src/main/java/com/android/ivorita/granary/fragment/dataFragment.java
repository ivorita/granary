package com.android.ivorita.granary.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ivorita.granary.R;
import com.android.ivorita.granary.data.GranaryInfo;
import com.android.ivorita.granary.util.HttpUtil;
import com.android.ivorita.granary.util.JsonHandler;
import com.android.ivorita.granary.util.LeakCanaryUtil;
import com.android.ivorita.granary.util.TimerUtil;
import com.android.ivorita.granary.view.MeterView;
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.squareup.leakcanary.RefWatcher;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class dataFragment extends Fragment {

    @BindView(R.id.qmui_linear)
    QMUILinearLayout qmuiLinear;
    Unbinder unbinder;
    @BindView(R.id.TempCtrlView)
    MeterView TempCtrlView;
    @BindView(R.id.humidityView)
    MeterView humidityView;
    @BindView(R.id.illuView)
    MeterView illuView;
    @BindView(R.id.cdView)
    MeterView cdView;

    private float mShadowAlpha = 0.6f;
    private int mShadowElevationDp = 14;
    private int mRadius;

    TimerUtil rxTimer = new TimerUtil();

    public static dataFragment newInstance() {
        dataFragment fragment = new dataFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRadius = QMUIDisplayHelper.dp2px(getContext(), 15);
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        unbinder = ButterKnife.bind(this, view);

        initLayout();
        requestInfo();
        intervalGet();

        return view;
    }

    /**
     * 每隔10秒获取一次数据
     */
    private void intervalGet() {
        rxTimer.interval(5000, new TimerUtil.RxAction() {
            @Override
            public void action(long number) {
                Log.d(TAG, "action execute");
                requestInfo();
            }
        });
    }

    private void requestInfo() {

        String url = "http://192.168.43.88:8080/trio/getjson?total=1";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: ", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: success");
                String res = response.body().string();
                Log.d(TAG, "onResponse: " + res);
                final GranaryInfo granaryInfo = JsonHandler.handleResponse(res);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TempCtrlView.setValue((int) granaryInfo.getTemperature());
                        humidityView.setValue(0, 100, (int) granaryInfo.getHumidity());
                        illuView.setValue(0, 1000, (int) granaryInfo.getIllumination());
                        cdView.setValue(0, 800, (int) granaryInfo.getGas());
                    }
                });
            }
        });

    }

    private void initLayout() {
        qmuiLinear.setShadowColor(0xff0000ff);
        qmuiLinear.setRadiusAndShadow(mRadius,
                QMUIDisplayHelper.dp2px(getContext(), mShadowElevationDp),
                mShadowAlpha);

        //设置最小值、最大值
        TempCtrlView.setValue(0, 40, 0);
        humidityView.setValue(0, 100, 0);
        illuView.setValue(0, 1000, 0);
        cdView.setValue(0, 800, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //内存泄漏检测
        RefWatcher refWatcher = LeakCanaryUtil.getRefWatcher(getActivity());
        refWatcher.watch(this);

        Log.d(TAG, "onDestroy: execute");

        rxTimer.cancel();
    }

    /*@Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }*/
}
