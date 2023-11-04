package account.fpoly.s_shop_client.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import account.fpoly.s_shop_client.Modal.Address;
import account.fpoly.s_shop_client.Modal.District;
import account.fpoly.s_shop_client.Modal.Province;
import account.fpoly.s_shop_client.Modal.Ward;
import account.fpoly.s_shop_client.Tools.TOOLS;
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

}
