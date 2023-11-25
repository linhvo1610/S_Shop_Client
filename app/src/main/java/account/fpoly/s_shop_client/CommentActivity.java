package account.fpoly.s_shop_client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.Modal.CommentModal;
import account.fpoly.s_shop_client.Modal.ReceComment;
import account.fpoly.s_shop_client.Modal.ReceProduct;
import account.fpoly.s_shop_client.Service.CommentService;
import account.fpoly.s_shop_client.adapter.CommentAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {
   private List<CommentModal> list;
       CommentAdapter commentAdapter;
    private  RecyclerView recyclerView;
    String idProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        recyclerView=findViewById(R.id.rcv_comment);
        ImageView img = findViewById(R.id.img_back_comment);

        SharedPreferences sharedPreferences = getSharedPreferences("product", MODE_PRIVATE);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        idProduct = sharedPreferences.getString("idProduct", null);

        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        list = new ArrayList<>();
//        callListcomment();
        callListVolley();


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });

    }
    public void callListVolley(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest =new JsonObjectRequest(Request.Method.GET, API.api + "comment?id_product=" + idProduct,
                null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CommentModal commentModal = new CommentModal();
                        commentModal.setComment(jsonObject.getString("comment"));

                        JSONObject jsonObjectUser = jsonObject.getJSONObject("id_user");
                        try {
                            commentModal.setFullname(jsonObjectUser.getString("fullname"));
                            commentModal.setImage(jsonObjectUser.getString("image"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        JSONObject jsonObjectPro = jsonObject.getJSONObject("id_product");
                        try {
                            commentModal.setName(jsonObjectPro.getString("name"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        list.add(commentModal);
                    }
                    commentAdapter = new CommentAdapter(list,getBaseContext());
                    recyclerView.setAdapter(commentAdapter);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    public  void  callListcomment(){
        CommentService.apiComment.listcomment().enqueue(new Callback<ReceComment>() {
            @Override
            public void onResponse(Call<ReceComment> call, Response<ReceComment> response) {
                list = response.body().getData();
                commentAdapter = new CommentAdapter(list, getApplicationContext());
                recyclerView.setAdapter(commentAdapter);
                commentAdapter.notifyDataSetChanged();
                Log.d("tuanpa67", "onRespon : list");

            }

            @Override
            public void onFailure(Call<ReceComment> call, Throwable t) {
                Log.e("TAGERRR", "onFailure: ", t);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        callListcomment();
    }
}