package account.fpoly.s_shop_client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import account.fpoly.s_shop_client.Modal.Notify;
import account.fpoly.s_shop_client.Service.ApiService;
import account.fpoly.s_shop_client.Tools.ACCOUNT;
import account.fpoly.s_shop_client.Tools.TOOLS;
import account.fpoly.s_shop_client.adapter.NotifyAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifyActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private NotifyAdapter adapter;
    private LinearLayout ln_check_notify;
    private Button btn_login_notify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_notify);
        mapping();
        setToolbar();
        showListNotify();
    }
    private void mapping() {
        toolbar = findViewById(R.id.toolbar_notify);
        recyclerView = findViewById(R.id.rcv_notify);
        ln_check_notify = findViewById(R.id.ln_check_notify);
        btn_login_notify = findViewById(R.id.btn_login_notify);
    }
    private void setToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Thông báo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void getListData() {
        Dialog dialog = TOOLS.createDialog(this);
        dialog.show();
        ApiService.apiService.getNotify(ACCOUNT.user.get_id()).enqueue(new Callback<List<Notify>>() {
            @Override
            public void onResponse(@NonNull Call<List<Notify>> call, @NonNull Response<List<Notify>> response) {
                if(response.isSuccessful()&&response.body()!=null){
                    Collections.reverse(response.body());
                    adapter.setData(response.body());
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<List<Notify>> call, @NonNull Throwable t) {
                dialog.dismiss();
                Toast.makeText(NotifyActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showListNotify() {
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new NotifyAdapter(this);
        recyclerView.setAdapter(adapter);
        getListData();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.prev_enter,R.anim.prev_exit);
    }
}