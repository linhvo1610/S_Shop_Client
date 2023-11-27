package account.fpoly.s_shop_client.Modal;



import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserModal implements Serializable {
    @SerializedName("_id")
    private String _id;
    private String username;
    private String password;
    private String fullname;
    private String email;
    private String image;
    private String phone;
    private String dob;
    private String sex;
    private String role;
    private String token;


     String currentPassword;
     String newPassword;
     String confirmPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public UserModal(String currentPassword, String newPassword, String confirmPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public UserModal(String _id, String currentPassword, String newPassword, String confirmPassword) {
        this._id = _id;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserModal() {
    }

    public UserModal(String id, String username, String password, String fullname, String email, String image, String phone, String dob, String sex, String role) {
        this._id = id;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.email = email;
        this.image = image;
        this.phone = phone;
        this.dob = dob;
        this.sex = sex;
        this.role = role;
    }

    public UserModal(String username, String password, String email, String phone, String dob,  String fullname ) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
//        this.sex = sex;
        this.fullname = fullname;
    }

    public UserModal(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
