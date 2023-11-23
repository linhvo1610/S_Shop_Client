package account.fpoly.s_shop_client.Modal;

import java.io.Serializable;
import java.util.List;

public class BillMore implements Serializable {
    private String _id;

    private String id_user;
    private String name;
    private String phone;
    private String date;
    private int total;
    private int status;

    private List<Cart> list;

    private String address;
    private int totalImport;

    public BillMore() {
    }

    public BillMore(String _id, String id_user, String name, String phone, String date, int total, int status, List<Cart> list, String address) {
        this._id = _id;
        this.id_user = id_user;
        this.name = name;
        this.phone = phone;
        this.date = date;
        this.total = total;
        this.status = status;
        this.list = list;
        this.address = address;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Cart> getList() {
        return list;
    }

    public void setList(List<Cart> list) {
        this.list = list;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getImportPrice() {
        return totalImport;
    }

    public void setImportPrice(int importPrice) {
        this.totalImport = importPrice;
    }
}
