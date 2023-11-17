package account.fpoly.s_shop_client.Modal;

import java.io.Serializable;

public class Cart implements Serializable {
    private String _id;
    private String id_user;
    private String id_product;
    private String name_product;
    private Integer price_product;
    private String image;
    private Integer quantity;

    private Integer size;

    public Cart() {
    }

    public Cart(String _id, String id_user, String id_product, String name_product, Integer price_product, String image, Integer quantity, Integer size) {
        this._id = _id;
        this.id_user = id_user;
        this.id_product = id_product;
        this.name_product = name_product;
        this.price_product = price_product;
        this.image = image;
        this.quantity = quantity;
        this.size = size;
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

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public Integer getPrice_product() {
        return price_product;
    }

    public void setPrice_product(Integer price_product) {
        this.price_product = price_product;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "_id='" + _id + '\'' +
                ", id_user='" + id_user + '\'' +
                ", id_product='" + id_product + '\'' +
                ", name_product='" + name_product + '\'' +
                ", price_product=" + price_product +
                ", image='" + image + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
