package account.fpoly.s_shop_client.GiaoDien;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import account.fpoly.s_shop_client.API.API_User;
import account.fpoly.s_shop_client.MainActivity;
import account.fpoly.s_shop_client.Modal.Address;
import account.fpoly.s_shop_client.Modal.UserModal;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.Service.ApiService;
import account.fpoly.s_shop_client.Service.ServiceUser;
import account.fpoly.s_shop_client.Tab_Giaodien_Activity;
import account.fpoly.s_shop_client.Tools.ACCOUNT;
import account.fpoly.s_shop_client.Tools.LIST;
import account.fpoly.s_shop_client.Tools.TOOLS;
import okhttp3.MediaType;
import okhttp3.RequestBody;
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
    private String url = "http://192.168.1.13:3000";
    CheckBox savepass;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        tvDangKy = findViewById(R.id.tv_dangky);
        btnDangNhap = findViewById(R.id.btn_DangNhap);

        txtuser = findViewById(R.id.txt_username);
        txtpass = findViewById(R.id.txt_password);
        savepass=findViewById(R.id.savePasswordCheckBox);
        savepass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Lưu mật khẩu
//                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("password", txtpass.getText().toString());
//                    editor.putString("username",txtuser.getText().toString());
//                    editor.putBoolean("savePassword", true);
//                    editor.apply();

                    String password = txtpass.getText().toString();
                    String username = txtuser.getText().toString();
                    if (!username.isEmpty() && !password.isEmpty()) {
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("password", password);
                        editor.putString("username", username);
                        editor.putBoolean("savePassword", true);
                        editor.apply();
                    } else {
                        Toast.makeText(getBaseContext(), "Hãy nhập Username và Password trước khi lưu!!!", Toast.LENGTH_SHORT).show();
                        savepass.setChecked(false); // Không lưu nếu mật khẩu chưa được nhập
                    }

                } else {
                    // Xóa mật khẩu đã lưu
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("password");
                    editor.remove("username");
                    editor.putBoolean("savePassword", false);
                    editor.apply();
                }
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isPasswordSaved = sharedPreferences.getBoolean("savePassword", false);
        if (isPasswordSaved) {
            String savedUsername = sharedPreferences.getString("username", "");
            String savedPassword = sharedPreferences.getString("password", "");
//            txtpass.setText(savedPassword);
//            txtuser.setText(savedUsername);
//            savepass.setChecked(true);

            if (!savedPassword.isEmpty()) {
                txtpass.setText(savedPassword);
                txtuser.setText(savedUsername);
                savepass.setChecked(true);
            } else {
                Toast.makeText(this, "Nhập Username, Password trước khi lưu!!!", Toast.LENGTH_SHORT).show();
                savepass.setChecked(false); // Không check nếu mật khẩu chưa được lưu
            }
        } else {
            txtpass.setText(null);
            txtuser.setText(null);
            savepass.setChecked(false);
        }
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
                    TOOLS.saveUser(DangNhapActivity.this, userModal1);
                    ACCOUNT.user = userModal1;
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
                        String token = TOOLS.getToken(DangNhapActivity.this);
                        JSONObject postData = new JSONObject();
                        try {
                            postData.put("tokenNotify", token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String jsonString = postData.toString();

                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonString);
                        ApiService.apiService.tokenNotify(ACCOUNT.user.get_id(), requestBody).enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                                Log.e( "login: ", requestBody+"");
                                if (response.isSuccessful()) {


                                    userModal1.setToken(token);
                                    Log.e( "onResponse: ", token);
                                    TOOLS.saveUser(DangNhapActivity.this, userModal1);
                                    ACCOUNT.user = userModal1;
                                    startActivity(new Intent(getBaseContext(),Tab_Giaodien_Activity.class));
                                    Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                                Log.e( "login fail : ",  t.getMessage());
                            }
                        });
                    }else if (userModal1.getRole().equalsIgnoreCase("Admin")){
                        Toast.makeText(DangNhapActivity.this, "App dành  cho Khách Hàng ", Toast.LENGTH_SHORT).show();
                    }

                    ApiService.apiService.getAddress(userModal1.get_id()).enqueue(new Callback<List<Address>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<Address>> call, @NonNull Response<List<Address>> response) {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    LIST.listAddress = response.body();
                                }
                            }
//                            gotoSettings();
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<Address>> call, @NonNull Throwable t) {
//                            gotoSettings();
                        }
                    });

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


            }
        });
    }

}