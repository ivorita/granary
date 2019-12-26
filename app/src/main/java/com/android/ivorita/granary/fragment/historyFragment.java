package com.android.ivorita.granary.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ivorita.granary.R;
import com.android.ivorita.granary.data.InfoTotal;
import com.android.ivorita.granary.util.DynamicLineChartManager;
import com.android.ivorita.granary.util.HttpUtil;
import com.android.ivorita.granary.util.JsonHandler;
import com.github.mikephil.charting.charts.LineChart;
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class historyFragment extends Fragment {

    @BindView(R.id.qmui_linear_history)
    QMUILinearLayout qmuiLinearHistory;
    Unbinder unbinder;
    @BindView(R.id.history_chart)
    LineChart historyChart;
    @BindView(R.id.query_btn)
    QMUIRoundButton queryBtn;
    /*@BindView(R.id.start_date)
    TextInputEditText startDate;
    @BindView(R.id.end_date)
    TextInputEditText endDate;*/
    @BindView(R.id.qmui_linear_msg)
    QMUILinearLayout qmuiLinearMsg;

    @BindView(R.id.s_year)
    TextInputEditText sYear;
    @BindView(R.id.s_month)
    TextInputEditText sMonth;
    @BindView(R.id.s_day)
    TextInputEditText sDay;
    @BindView(R.id.e_year)
    TextInputEditText eYear;
    @BindView(R.id.e_month)
    TextInputEditText eMonth;
    @BindView(R.id.e_day)
    TextInputEditText eDay;

    private float mShadowAlpha = 0.6f;
    private int mShadowElevationDp = 14;
    private int mRadius;

    private DynamicLineChartManager dynamicLineChartManager_air;

    private List<Integer> list = new ArrayList<>(); //数据集合
    private List<String> names = new ArrayList<>(); //折线名字集合
    private List<Integer> color = new ArrayList<>();//折线颜色集合

    public static historyFragment newInstance() {
        historyFragment fragment = new historyFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRadius = QMUIDisplayHelper.dp2px(getContext(), 15);
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        unbinder = ButterKnife.bind(this, view);

        initLayout();

        chartAirInit();

        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (!(sYear.getText().toString() == null || sMonth.getText().toString() == null || sDay.getText().toString() == null || eYear.getText().toString() == null || eMonth.getText().toString() == null || eDay.getText().toString() == null))
                requestInfo(sYear.getText().toString(),sMonth.getText().toString(),sDay.getText().toString(),eYear.getText().toString(),eMonth.getText().toString(),eDay.getText().toString());
            }
        });

        return view;
    }

    private void initLayout() {
        qmuiLinearHistory.setShadowColor(0xff0000ff);
        qmuiLinearHistory.setRadiusAndShadow(mRadius,
                QMUIDisplayHelper.dp2px(getContext(), mShadowElevationDp),
                mShadowAlpha);

        qmuiLinearMsg.setShadowColor(0xff0000ff);
        qmuiLinearMsg.setRadiusAndShadow(mRadius,
                QMUIDisplayHelper.dp2px(getContext(), mShadowElevationDp),
                mShadowAlpha);
    }

    /**
     * 初始化折线图
     */
    private void chartAirInit() {
        //折线名字
        names.add("温度");
        names.add("湿度");
        names.add("光照强度");
        names.add("可燃气体");

        //折线颜色
        color.add(getResources().getColor(R.color.air_temp, getActivity().getTheme()));
        color.add(getResources().getColor(R.color.green, getActivity().getTheme()));
        color.add(getResources().getColor(R.color.light, getActivity().getTheme()));
        color.add(getResources().getColor(R.color.co_2, getActivity().getTheme()));

        historyChart.setDrawBorders(true);

        dynamicLineChartManager_air = new DynamicLineChartManager(historyChart, names, color);
        dynamicLineChartManager_air.setDescription();
        dynamicLineChartManager_air.setYAxis(900, 0, 10);
    }

    private void requestInfo(String sy,String sm,String sd,String ey,String em,String ed) {

        String url = "http://192.168.43.88:8080/trio/QueryHistory?start=" + sy + "-" + sm + "-" + sd + "&end=" + ey + "-" + em + "-" + ed;
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
                InfoTotal infoTotal = JsonHandler.parseRes(res);

                for (int i = 0; i < infoTotal.getTotal(); i++) {
                    Log.i(TAG, "i: " + i);
                    try {
                        list.add((int) infoTotal.getRows().get(i).getTemperature());
                        list.add((int) infoTotal.getRows().get(i).getHumidity());
                        list.add((int) infoTotal.getRows().get(i).getIllumination());
                        list.add((int) infoTotal.getRows().get(i).getGas());

                        /*String date = new String(infoTotal.getRows().get(i).getDatetime());
                        date.substring(0)*/
                        dynamicLineChartManager_air.addEntry(list, infoTotal.getRows().get(i).getDatetime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Log.e(TAG, "chart error: ", e);
                    } finally {
                        list.clear();
                    }
                }

                Log.d(TAG, "granaryInfo: " + infoTotal.toString());
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /*@OnClick(R.id.query_btn)
    public void onViewClicked() {
        requestInfo();
    }*/
}
