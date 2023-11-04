package account.fpoly.s_shop_client.Modal;

public class CommentModal {
    private  String id;
    private  UserModal id_user;
    private  ProductModal id_product;
    private  String comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserModal getId_user() {
        return id_user;
    }

    public void setId_user(UserModal id_user) {
        this.id_user = id_user;
    }

    public ProductModal getId_product() {
        return id_product;
    }

    public void setId_product(ProductModal id_product) {
        this.id_product = id_product;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
