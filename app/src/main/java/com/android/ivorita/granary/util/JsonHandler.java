package com.android.ivorita.granary.util;

import android.util.Log;

import com.android.ivorita.granary.data.GranaryInfo;

import com.android.ivorita.granary.data.InfoTotal;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.content.ContentValues.TAG;

public class JsonHandler {

    public static GranaryInfo handleResponse(String response){

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray rows = jsonObject.getJSONArray("rows");
            JSONObject info = rows.getJSONObject(0);
            Log.d(TAG, "handleResponse1: "  + info);
            return new Gson().fromJson(info.toString(), GranaryInfo.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InfoTotal parseRes(String response){

        try {
            JSONObject jsonObject = new JSONObject(response);
            Log.d(TAG, "handleResponse1: "  + jsonObject);
            return new Gson().fromJson(jsonObject.toString(), InfoTotal.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
