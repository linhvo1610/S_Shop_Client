package account.fpoly.s_shop_client.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.API.API_Product;
import account.fpoly.s_shop_client.Modal.CommentModal;
import account.fpoly.s_shop_client.Modal.ReceComment;
import account.fpoly.s_shop_client.Modal.ReceProduct;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface CommentService {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyy").create();

    CommentService apiComment = new Retrofit.Builder()
            .baseUrl(API.api)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CommentService.class);
    //    @FormUrlEncoded

    @GET("comment")
    Call<ReceComment> listcomment();
}

