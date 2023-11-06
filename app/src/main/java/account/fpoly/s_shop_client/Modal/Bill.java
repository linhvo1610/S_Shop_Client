package account.fpoly.s_shop_client.Modal;

import java.util.List;

public class Bill {
    private String _id;
    private String date;
    private String status;
    private String id_user;
    private List<String> product;
    private String id_address;
    private int totalQuantity;
    private double totalPrice;
    private int size;

    public Bill() {
    }

    public String getId_address() {
        return id_address;
    }

    public void setId_address(String id_address) {
        this.id_address = id_address;
    }

    public Bill(String status, String id_user, List<String> product, String id_address, int totalQuantity, double totalPrice, int size) {
        this.status = status;
        this.id_user = id_user;
        this.product = product;
        this.id_address = id_address;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.size = size;
    }

    public static class Product {
        private String id_product;
        private String _id;

        public Product() {
        }

        public String getId_product() {
            return id_product;
        }

        public void setId_product(String id_product) {
            this.id_product = id_product;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public List<String> getProduct() {
        return product;
    }

    public void setProduct(List<String> product) {
        this.product = product;
    }
}
