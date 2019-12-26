package com.android.ivorita.granary.data;

import java.util.List;

public class InfoTotal {

    private int total;//信息数量
    private List<GranaryInfo> rows;//信息列表

    public InfoTotal() {
    }

    public InfoTotal(int total, List<GranaryInfo> rows) {
        this.total = total;
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<GranaryInfo> getRows() {
        return rows;
    }

    public void setRows(List<GranaryInfo> rows) {
        this.rows = rows;
    }

}
