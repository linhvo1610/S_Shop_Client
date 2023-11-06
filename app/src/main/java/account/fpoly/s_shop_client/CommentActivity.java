package account.fpoly.s_shop_client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        recyclerView=findViewById(R.id.rcv_comment);
        ImageView img = findViewById(R.id.img_back_comment);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        list = new ArrayList<>();
        callListcomment();


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });

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