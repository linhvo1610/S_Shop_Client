package account.fpoly.s_shop_client.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import account.fpoly.s_shop_client.Modal.Bill;
import account.fpoly.s_shop_client.Modal.ReceBillMores;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API_Bill {


    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyy").create();

    API_Bill apiBill = new Retrofit.Builder()
            .baseUrl(API.api)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(API_Bill.class);
    //    @FormUrlEncoded

    @POST("bill")
    Call<Bill> addBill(@Body Bill bill);

    @DELETE("bill/delete/{id}")
    Call<Bill> huyOder(@Path("id") String id);

    @PUT("bill/update/{id}")
    Call<Bill> updateBill(@Path("id") String id, @Body Bill bill);

    @GET("billStatus")
    Call<ReceBillMores> getListBillMores(@Query("id_user") String id_user, @Query("status") int status);
}
