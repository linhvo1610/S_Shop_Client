package account.fpoly.s_shop_client.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import account.fpoly.s_shop_client.Modal.Address;
import account.fpoly.s_shop_client.Modal.BillMore;
import account.fpoly.s_shop_client.Modal.Cart;
import account.fpoly.s_shop_client.Modal.District;
import account.fpoly.s_shop_client.Modal.Notify;
import account.fpoly.s_shop_client.Modal.Province;
import account.fpoly.s_shop_client.Modal.Ward;
import account.fpoly.s_shop_client.Tools.TOOLS;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    String url  = TOOLS.doMainDevice;
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @GET("address/provinces")
    Call<List<Province>> getProvinces();

    @GET("address/districts/{parent_code}")
    Call<List<District>> getDistricts(@Path("parent_code") String parent_code);

    @GET("address/wards/{parent_code}")
    Call<List<Ward>> getWards(@Path("parent_code") String parent_code);

    @POST("address/addNew/{id_user}")
    Call<Address> saveNewAddress(@Path("id_user") String id_user, @Body Address address);

    @GET("address/all/{id_user}")
    Call<List<Address>> getAddress(@Path("id_user") String id_user);

    @POST("address/update")
    Call<Address> updateAddress(@Body Address Address);

    @POST("address/delete/{id_address}")
    Call<Integer> deleteAdsress(@Path("id_address") String id_address);

    @GET("cart/delete/{id_cart}")
    Call<Integer> deleteCart(@Path("id_cart") String id_cart);

    @GET("cart/{id_user}")
    Call<List<Cart>> getCarts(@Path("id_user") String id_user);

    @POST("cart/add")
    Call<Cart> addCart(@Body Cart cart);

    @POST("billmore/add/{token}")
    Call<BillMore> createBill(@Path("token") String token_device, @Body BillMore billMore);
    @POST("billmore/update/{id_billmore}")
    Call<Integer> updateBill(@Path("id_billmore") String id_billmore);

    @POST("billmore/updateBill/{id_billmore}")
    Call<Integer> updateBillHuy(@Path("id_billmore") String id_billmore);

    @POST("billmore/cancel/{id_billmore}")
    Call<Integer> cancelBill(@Path("id_billmore") String id_billmore);
    @GET("notify/{id_user}")
    Call<List<Notify>> getNotify(@Path("id_user") String id_cart);
    @POST("api/tokenNotify/{id_user}")
    Call<Integer> tokenNotify(@Path("id_user") String id_user,@Body RequestBody requestBody);

}
