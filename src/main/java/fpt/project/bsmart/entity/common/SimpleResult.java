package fpt.project.bsmart.entity.common;

import java.util.ArrayList;

public class SimpleResult {

    private boolean result;
    private ArrayList<Object> dataRes;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public ArrayList<Object> getDataRes() {
        return dataRes;
    }

    public void setDataRes(ArrayList<Object> dataRes) {
        this.dataRes = dataRes;
    }
}
