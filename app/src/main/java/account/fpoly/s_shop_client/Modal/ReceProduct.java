package account.fpoly.s_shop_client.Modal;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReceProduct {
    @SerializedName("data")
    private ArrayList<ProductModal> data;
    private String msg;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<ProductModal> getData() {
        return data;
    }

    public void setData(ArrayList<ProductModal> data) {
        this.data = data;
    }
}
