package account.fpoly.s_shop_client.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import account.fpoly.s_shop_client.Modal.Bill;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

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
}
