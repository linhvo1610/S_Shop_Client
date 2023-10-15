package account.fpoly.s_shop_client.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import account.fpoly.s_shop_client.Modal.UserModal;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceUser {
    // link API: http://192.168.1.13:3000
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    @GET("/api/users")
    Call<List<UserModal>> getallUser();
    @POST("/api/register")
    Call<UserModal> dangkiUser(@Body UserModal data);
    @PUT("/api/updateuser/{id}")
    Call<UserModal> suaUser(@Path("id") String id, @Body UserModal data);
}
