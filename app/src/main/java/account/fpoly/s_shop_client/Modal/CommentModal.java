package account.fpoly.s_shop_client.Modal;

import com.google.gson.annotations.SerializedName;

public class CommentModal {
    @SerializedName("_id")
    String id;
    String id_user;
    String id_product;
    String comment;
    String fullname,name,size,image;

    public CommentModal(String id_user, String id_product, String comment) {
        this.id_user = id_user;
        this.id_product = id_product;
        this.comment = comment;
    }

    public CommentModal() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
