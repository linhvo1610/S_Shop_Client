package account.fpoly.s_shop_client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.API.API_User;
import account.fpoly.s_shop_client.Modal.UserModal;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoUserActivity extends AppCompatActivity {

    TextInputEditText edfullname,edgioitinh,edngaysinh,edemail,edphone;
    ImageView image,back;
    Spinner genderSpinner;
    String curgioitinh,curid,name,email,phone,ngaysinh,curpasswd,curphanquyen,
            curimage,currol;
    LinearLayout update;
    String[] genders = {"Nam", "Nữ", "Khác"};
    ArrayAdapter<String> adapter;
    List<UserModal> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);

        anhxa();

        hienthiInfo();
        hienthiInfomationUser();

    }

    private void anhxa() {
        edfullname = findViewById(R.id.edfullname);
        edgioitinh = findViewById(R.id.edgioitinh);
        edngaysinh = findViewById(R.id.edngaysinh);
        edemail = findViewById(R.id.edemail);
        edphone = findViewById(R.id.edphone);
        genderSpinner = findViewById(R.id.gender_spinner);
        image = findViewById(R.id.image);
        update = findViewById(R.id.update);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //         Tạo một ArrayAdapter để hiển thị dữ liệu trong Spinner
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
// Thiết lập ArrayAdapter cho Spinner
        String selectedGender = curgioitinh; // Giới tính đã chọn
        int position = Arrays.asList(genders).indexOf(selectedGender);
        genderSpinner.setSelection(position);

        genderSpinner.setAdapter(adapter);
        genderSpinner.setGravity(Gravity.CENTER);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });
    }

    private void updateUser() {
        String fullnameuser = edfullname.getText().toString().trim();
        String emailuser = edemail.getText().toString().trim();
        String phoneuser = edphone.getText().toString().trim();
        String gioitinhuser = edgioitinh.getText().toString().trim();
        String ngaysinhuser = edngaysinh.getText().toString().trim();

        String selectedGender = genderSpinner.getSelectedItem().toString();

        UserModal user = new UserModal();
        user.setFullname(fullnameuser);
        user.setEmail(emailuser);
        user.setPhone(phoneuser);
        user.setDob(ngaysinhuser);
        user.setSex(selectedGender);
        user.setRole(curphanquyen);
        user.setPassword(curpasswd);

        API_User.apiUser.updateUser(curid,user).enqueue(new Callback<UserModal>() {
            @Override
            public void onResponse(Call<UserModal> call, Response<UserModal> response) {
                if (response.isSuccessful()){
                    list.clear();
                    UserModal user = response.body();
                    list.add(user);
//                    dapter = new UserDapter(list,getBaseContext());
                    Toast.makeText(getBaseContext(), "Cap nhap thanh cong", Toast.LENGTH_SHORT).show();
//                    dapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(InfoUserActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModal> call, Throwable t) {

            }
        });
    }

    private void hienthiInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("infoUser", MODE_PRIVATE);
        curid = sharedPreferences.getString("iduser", null);
        name = sharedPreferences.getString("fullname", null);
        email = sharedPreferences.getString("email", null);
        ngaysinh = sharedPreferences.getString("ngaysinh", null);
        phone = sharedPreferences.getString("phone", null);
        curimage = sharedPreferences.getString("image", null);
        curpasswd = sharedPreferences.getString("password", null);
        currol = sharedPreferences.getString("phanquyen", null);


        edfullname.setText(name);
        edemail.setText(email);
        edngaysinh.setText(ngaysinh);
        edphone.setText(phone);

        list= new ArrayList<>();
        Glide.with(getBaseContext())
                .load(curimage)
                .into(image);
        String selectedGender = curgioitinh; // Giới tính đã chọn
        int position = Arrays.asList(genders).indexOf(selectedGender);
        genderSpinner.setSelection(position);
    }
    private void hienthiInfomationUser(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.api + "users?_id=" + curid, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        edngaysinh.setText(jsonObject.getString("dob"));
                        edphone.setText(jsonObject.getString("phone"));
                        edemail.setText(jsonObject.getString("email"));
                        edfullname.setText(jsonObject.getString("fullname"));
                        String gioitinhuser = jsonObject.getString("sex");

                        String selectedGender = gioitinhuser; // Giới tính đã chọn
                        int position = Arrays.asList(genders).indexOf(selectedGender);
                        genderSpinner.setSelection(position);
                    }

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
    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        } else {
            super.onBackPressed();
            finish();
        }
    }
}