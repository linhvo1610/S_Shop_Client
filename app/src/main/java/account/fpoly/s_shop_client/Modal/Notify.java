package account.fpoly.s_shop_client.Modal;

import java.util.Date;

public class Notify {
    private String _id;
    private String id_user;
    private String content;
    private Date time;
    private int status;

    public Notify() {
    }

    public Notify(String _id, String id_user, String content, Date time, int status) {
        this._id = _id;
        this.id_user = id_user;
        this.content = content;
        this.time = time;
        this.status = status;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String id_billmore) {
        this.content = id_billmore;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
