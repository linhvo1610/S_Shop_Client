package account.fpoly.s_shop_client.GiaoDien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.API.API_User;
import account.fpoly.s_shop_client.Modal.UserModal;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.Service.ServiceUser;
import account.fpoly.s_shop_client.Tools.TOOLS;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DangKyActivity extends AppCompatActivity {
    Button btnDangKy;
    private TextInputEditText dangky_username, dangky_phone, dangky_password, dangky_email;
    private EditText dangky_dob, dangky_sex, dangky_fullname,register_img;
    private Retrofit retrofit;
    private ServiceUser serviceUser;
    private String url = API.api_reg;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        AnhXa();

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DangKy();

            }
        });
    }

    private void AnhXa() {
        dangky_username = findViewById(R.id.register_username);
        dangky_phone = findViewById(R.id.register_phone);
        dangky_password = findViewById(R.id.register_password);
        dangky_email = findViewById(R.id.register_email);

        dangky_dob = findViewById(R.id.register_dob);
        dangky_sex = findViewById(R.id.register_sex);
        dangky_fullname = findViewById(R.id.register_fullname);
        register_img = findViewById(R.id.register_img);

        btnDangKy = findViewById(R.id.btn_DangKy);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void DangKy() {
        retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();

        String username = dangky_username.getText().toString().trim();
        String phone = dangky_phone.getText().toString().trim();
        String password = dangky_password.getText().toString().trim();
        String email = dangky_email.getText().toString().trim();

        String dob = dangky_dob.getText().toString().trim();
        String sex = dangky_sex.getText().toString().trim();
        String fullname = dangky_fullname.getText().toString().trim();
        String image = register_img.getText().toString().trim();
        String token = TOOLS.getToken(DangKyActivity.this);;

        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "Không được để trống Username", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Không được để trống họ tên", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Không được để trống password", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Không được để trống email", Toast.LENGTH_SHORT).show();
        }else {
            serviceUser = retrofit.create(ServiceUser.class);
            Call<UserModal> call = serviceUser.dangkiUser(new UserModal(username, password, email, phone, dob, fullname, token));
            call.enqueue(new Callback<UserModal>() {
                @Override
                public void onResponse(Call<UserModal> call, Response<UserModal> response) {
                    Toast.makeText(DangKyActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
                    startActivity(intent);

                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }

                @Override
                public void onFailure(Call<UserModal> call, Throwable t) {

                }
            });
        }
    }

}