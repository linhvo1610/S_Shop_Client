package account.fpoly.s_shop_client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
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

    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);

        anhxa();

        hienthiInfo();
        hienthiInfomationUser();

    }

    private void anhxa() {
        dateButton = findViewById(R.id.datePickerButton);
        initDatePicker();
        dateButton.setText(getTodaysDate());


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
        String ngaysinhDate = dateButton.getText().toString().trim();

        String selectedGender = genderSpinner.getSelectedItem().toString();

        UserModal user = new UserModal();
        user.setFullname(fullnameuser);
        user.setEmail(emailuser);
        user.setPhone(phoneuser);
//        user.setDob(ngaysinhuser);
        user.setSex(selectedGender);
        user.setRole(curphanquyen);
        user.setPassword(curpasswd);

        user.setDob(ngaysinhDate);

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
//        edngaysinh.setText(ngaysinh);
        edphone.setText(phone);

        dateButton.setText(ngaysinh);

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

                        dateButton.setText(jsonObject.getString("dob"));
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
    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }
    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year)
    {
        return   day + "/" + getMonthFormat(month) +  "/" + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "1";
        if(month == 2)
            return "2";
        if(month == 3)
            return "3";
        if(month == 4)
            return "4";
        if(month == 5)
            return "5";
        if(month == 6)
            return "6";
        if(month == 7)
            return "7";
        if(month == 8)
            return "8";
        if(month == 9)
            return "9";
        if(month == 10)
            return "10";
        if(month == 11)
            return "11";
        if(month == 12)
            return "12";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }
}