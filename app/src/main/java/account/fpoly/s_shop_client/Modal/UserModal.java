package account.fpoly.s_shop_client.Modal;



import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserModal implements Serializable {
    @SerializedName("_id")
    private String id;
    private String username;
    private String hoten;
    private String email;
    private String anhdaidien;
    private String password;
    private String phone;

    public UserModal(String id, String username, String hoten, String email, String anhdaidien, String password, String phone) {
        this.id = id;
        this.username = username;
        this.hoten = hoten;
        this.email = email;
        this.anhdaidien = anhdaidien;
        this.password = password;
        this.phone = phone;
    }

    public UserModal(String username, String hoten, String email, String anhdaidien, String password, String phone) {
        this.username = username;
        this.hoten = hoten;
        this.email = email;
        this.anhdaidien = anhdaidien;
        this.password = password;
        this.phone = phone;
    }

    public UserModal(String username, String password, String phone) {
        this.username = username;
        this.password = password;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAnhdaidien() {
        return anhdaidien;
    }

    public void setAnhdaidien(String anhdaidien) {
        this.anhdaidien = anhdaidien;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
