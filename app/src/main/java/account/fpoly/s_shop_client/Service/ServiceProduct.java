package account.fpoly.s_shop_client.Service;

import account.fpoly.s_shop_client.Modal.ProductModal;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ServiceProduct {
    @GET("/api/getlistbyid/{id}")
    Call<ProductModal> getThongTinProduct(@Path("id") String id);
}
