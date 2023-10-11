package account.fpoly.s_shop_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import account.fpoly.s_shop_client.Modal.UserModal;
import account.fpoly.s_shop_client.Service.ServiceUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DangNhapActivity extends AppCompatActivity {
    TextView tvDangKy;
    Button btnDangNhap;
    private TextInputEditText txtuser, txtpass;
    private List<UserModal> listUser = new ArrayList<>();
    private UserModal mUser;
    private Retrofit retrofit;
    private ServiceUser serviceUser;
    private String url = "http://192.168.1.7:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        tvDangKy = findViewById(R.id.tv_dangky);
        btnDangNhap = findViewById(R.id.btn_DangNhap);

        txtuser = findViewById(R.id.txt_username);
        txtpass = findViewById(R.id.txt_password);

        GetListUser();

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DangNhapActivity.this, Tab_Giaodien_Activity.class);
                startActivity(intent);

                DangNhap();

                Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
            }
        });
        tvDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DangNhapActivity.this, DangKyActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void DangNhap() {
        String username = txtuser.getText().toString().trim();
        String password = txtpass.getText().toString().trim();

        boolean isHasUser = false;

        if (listUser == null || listUser.isEmpty()) {
            return;
        }

        for (UserModal user : listUser) {
            if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                isHasUser = true;
                mUser = user;
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                break;
            } else if (username.equals("") && password.equals("")) {
                Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show();
                break;
            } else {

            }
        }

        if (isHasUser) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("username", mUser.getUsername());
            intent.putExtra("email", mUser.getEmail());
            intent.putExtra("idUser", mUser.getId());
            intent.putExtra("anhdaidien", mUser.getAnhdaidien());
            intent.putExtra("password", mUser.getPassword());
            intent.putExtra("hoten", mUser.getHoten());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
        }
    }

    private void GetListUser(){
        retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        serviceUser = retrofit.create(ServiceUser.class);
        Call<List<UserModal>> call = serviceUser.getallUser();
        call.enqueue(new Callback<List<UserModal>>() {
            @Override
            public void onResponse(Call<List<UserModal>> call, Response<List<UserModal>> response) {
                listUser = response.body();
            }

            @Override
            public void onFailure(Call<List<UserModal>> call, Throwable t) {
                Toast.makeText(DangNhapActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();

            }
        });
    }

}