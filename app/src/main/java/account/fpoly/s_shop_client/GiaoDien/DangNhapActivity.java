package account.fpoly.s_shop_client.GiaoDien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import account.fpoly.s_shop_client.API.API_User;
import account.fpoly.s_shop_client.MainActivity;
import account.fpoly.s_shop_client.Modal.UserModal;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.Service.ServiceUser;
import account.fpoly.s_shop_client.Tab_Giaodien_Activity;
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

//        GetListUser();

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
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
    public void loginUser(){
        String username = txtuser.getText().toString().trim();
        String password = txtpass.getText().toString().trim();

        UserModal userModal = new UserModal(username,password);
        if (username.isEmpty()){
            Toast.makeText(this, "Nhập Username", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()){
            Toast.makeText(this, "Nhập password", Toast.LENGTH_SHORT).show();
            return;
        }
        API_User.apiUser.login(userModal).enqueue(new Callback<UserModal>() {
            @Override
            public void onResponse(Call<UserModal> call, Response<UserModal> response) {
                if (response.isSuccessful()){
                    UserModal userModal1 = response.body();
                    SharedPreferences preferences = getSharedPreferences("infoUser",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("fullname", userModal1.getFullname());
                    editor.putString("username", userModal1.getUsername());
                    editor.putString("email", userModal1.getEmail());
                    editor.putString("phone", userModal1.getPhone());
                    editor.putString("ngaysinh", userModal1.getDob());
                    editor.putString("image", userModal1.getImage());
                    editor.putString("password", userModal1.getPassword());
                    editor.putString("phanquyen", userModal1.getRole());
                    editor.putString("iduser", userModal1.get_id());
                    editor.putString("token", userModal1.getToken());
                    editor.apply();
                    if (userModal1.getRole().equalsIgnoreCase("User")){
                        startActivity(new Intent(getBaseContext(),Tab_Giaodien_Activity.class));
                        Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Toast.makeText(DangNhapActivity.this, "Token:"+userModal1.getToken(), Toast.LENGTH_SHORT).show();
                    }else if (userModal1.getRole().equalsIgnoreCase("Admin")){
                        Toast.makeText(DangNhapActivity.this, "App danh cho User", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(DangNhapActivity.this, "Vui lòng kiếm tra lại tài khoản!!!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserModal> call, Throwable t) {

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
            intent.putExtra("idUser", mUser.get_id());
            intent.putExtra("image", mUser.getImage());
            intent.putExtra("password", mUser.getPassword());
            intent.putExtra("fullname", mUser.getFullname());
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

                Toast.makeText(DangNhapActivity.this, "Lấy dữ liệu thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<UserModal>> call, Throwable t) {
                Toast.makeText(DangNhapActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();

            }
        });
    }

}