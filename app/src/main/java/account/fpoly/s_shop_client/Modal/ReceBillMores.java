package account.fpoly.s_shop_client.Modal;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReceBillMores {

    @SerializedName("data")
    private ArrayList<BillMore> data;
    private String msg;
    private int status;

    public ArrayList<BillMore> getData() {
        return data;
    }

    public void setData(ArrayList<BillMore> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
