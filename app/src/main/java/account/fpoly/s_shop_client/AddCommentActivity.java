package account.fpoly.s_shop_client;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.Modal.CommentModal;
import account.fpoly.s_shop_client.Service.CommentService;
import account.fpoly.s_shop_client.adapter.CommentAdapter;
import account.fpoly.s_shop_client.fragment.HomeFragment;
import account.fpoly.s_shop_client.fragment.LichsuFragment;
import retrofit2.Call;
import retrofit2.Callback;

public class AddCommentActivity extends AppCompatActivity {
    private List<CommentModal> list;
    CommentAdapter commentAdapter;
    private RecyclerView recyclerView;
    String idProduct;
    EditText edt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        recyclerView = findViewById(R.id.Rcv_add_comment);
        edt = findViewById(R.id.ed_commentsp);
        Button btnadd = findViewById(R.id.btn_comment);
        ImageView img = findViewById(R.id.img_back_comment_add);

        SharedPreferences sharedPreferences = getSharedPreferences("product", MODE_PRIVATE);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        idProduct = sharedPreferences.getString("idProduct", null);

        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        list = new ArrayList<>();
//        callListcomment();


        callListVolley();
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment();
            }
        });


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

public  void  addComment(){
        String comment = edt.getText().toString();
    SharedPreferences preferencesbill = getSharedPreferences("infoUser",MODE_PRIVATE);
    String iduser = preferencesbill.getString("iduser",null);
    SharedPreferences preferences = getSharedPreferences("id_product",MODE_PRIVATE);
    String idpr = preferences.getString("idP",null);


    CommentService.apiComment.addComment(new CommentModal( iduser,idpr,comment)).enqueue(new Callback<CommentModal>() {
        @Override
        public void onResponse(Call<CommentModal> call, retrofit2.Response<CommentModal> response) {
            Toast.makeText(AddCommentActivity.this, "Bình Luận Thành Công", Toast.LENGTH_SHORT).show();
            callListVolley();
            commentAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(Call<CommentModal> call, Throwable t) {
            Toast.makeText(AddCommentActivity.this, "Bình Luận Không Thành Công", Toast.LENGTH_SHORT).show();
        }
    });
}

    @Override
    protected void onResume() {
        super.onResume();
        callListVolley();
    }
}