package account.fpoly.s_shop_client.Modal;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReceComment {
    @SerializedName("data")
    private ArrayList<CommentModal> data;
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

    public ArrayList<CommentModal> getData() {
        return data;
    }

    public void setData(ArrayList<CommentModal> data) {
        this.data = data;
    }
}

