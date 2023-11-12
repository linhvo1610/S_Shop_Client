package account.fpoly.s_shop_client.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import account.fpoly.s_shop_client.Modal.ProductModal;
import account.fpoly.s_shop_client.Modal.ReceProduct;
import account.fpoly.s_shop_client.Modal.UserModal;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API_Product {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyy").create();

    API_Product apiProduct = new Retrofit.Builder()
            .baseUrl(API.api)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(API_Product.class);
    //    @FormUrlEncoded

    @GET("product")
    Call<ReceProduct> listProduct();
    @GET("filterPrice")
    Call<List<ProductModal>> filterProducts(@Query("minPrice") String minPrice, @Query("maxPrice") String maxPrice);


}
