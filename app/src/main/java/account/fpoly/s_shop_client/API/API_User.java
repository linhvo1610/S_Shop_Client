package account.fpoly.s_shop_client.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import account.fpoly.s_shop_client.Modal.UserModal;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface API_User {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyy").create();

    API_User apiUser = new Retrofit.Builder()
            .baseUrl(API.api)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(API_User.class);
    //    @FormUrlEncoded

    @POST("login")
    Call<UserModal> login(@Body UserModal userModal);

    @PUT("updateUser/{id}")
    Call<UserModal> updateUser(@Path("id") String id, @Body UserModal user);

    @POST("register")
    Call<UserModal> postUser (@Body UserModal userModal);
    @POST("logout")
    Call<Void> logout();


    @Multipart
    @PUT("updateUserImage/{id}")
    Call<UserModal> updateImage(@Path("id") String id,
                                @Part("user") UserModal user,
                                @Part MultipartBody.Part image);

}
