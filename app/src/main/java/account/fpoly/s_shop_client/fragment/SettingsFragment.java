package account.fpoly.s_shop_client.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.API.API_User;
import account.fpoly.s_shop_client.ContactUsActivity;
import account.fpoly.s_shop_client.DaGiao_activity;
import account.fpoly.s_shop_client.DangGiao_Activity;
import account.fpoly.s_shop_client.HistoryOrderClient;
import account.fpoly.s_shop_client.HuyBill;
import account.fpoly.s_shop_client.InfoUserActivity;
import account.fpoly.s_shop_client.NotifyActivity;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.SplassActivity;
import account.fpoly.s_shop_client.Tools.ACCOUNT;
import account.fpoly.s_shop_client.Update_PassWord;
import account.fpoly.s_shop_client.Xacnhan_Bill;
import account.fpoly.s_shop_client.thongtinUser;

public class SettingsFragment extends Fragment {
    ImageView imginfo, imghistory;
    String image,curidUser,fullname;
    TextView txtfullname;
    API_User api_user;
    LinearLayout linnerXacnhan,linnerDanggiao,xacnhanPro,huyBill,ln_thongtin,ln_thongbao;
    LinearLayout ln_cart_emty,updatepass,contactus;
    Button btn_buy_cart;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_setting_user, container, false);
        imginfo = view.findViewById(R.id.imginfo);
        txtfullname = view.findViewById(R.id.txtfullname);
        linnerXacnhan = view.findViewById(R.id.linnerXacnhan);
        linnerDanggiao = view.findViewById(R.id.linnerDanggiao);
        xacnhanPro = view.findViewById(R.id.xacnhanPro);
        huyBill = view.findViewById(R.id.huyBill);
        ln_cart_emty=view.findViewById(R.id.ln_cart_emty);
        btn_buy_cart=view.findViewById(R.id.btn_buy_cart);
        ln_thongtin=view.findViewById(R.id.ln_thongtin);
        updatepass=view.findViewById(R.id.updatepass);
        contactus = view.findViewById(R.id.contact_us);
        ln_thongbao= view.findViewById(R.id.ln_thongbao_setting);

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ContactUsActivity.class);
                startActivity(intent);
            }
        });
        ln_thongbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NotifyActivity.class);
                startActivity(intent);
            }
        });

        updatepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Update_PassWord.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        huyBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), HuyBill.class));
            }
        });
        ln_thongtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), thongtinUser.class));
            }
        });
        linnerXacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Xacnhan_Bill.class));
            }
        });
        xacnhanPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), DaGiao_activity.class));
            }
        });
        linnerDanggiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), DangGiao_Activity.class));
            }
        });
        loadInfomation();
//        imghistory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getContext(), HistoryOrderClient.class));
//            }
//        });
        SharedPreferences sharedPreferences= getContext().getSharedPreferences("infoUser", getContext().MODE_PRIVATE);
        image=sharedPreferences.getString("image",null);
        curidUser=sharedPreferences.getString("iduser",null);
        fullname = sharedPreferences.getString("fullname", null);


        if (fullname == null || fullname.equals("")){
            txtfullname.setText("Cập nhập tên bạn!!!");
            txtfullname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), InfoUserActivity.class));
                }
            });
        }else{
            txtfullname.setText(fullname);
        }

        Glide.with(getContext()).load(API.api_image + image).into(imginfo);


        imginfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), InfoUserActivity.class));
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();

        // Thực hiện cập nhật dữ liệu ở đây
        loadInfomation();
    }
    private void loadInfomation() {

        if (ACCOUNT.user == null){
            ln_cart_emty.setVisibility(View.VISIBLE);
            btn_buy_cart.setText("Bạn cần đăng nhập để  sử dụng chức năng này ");
            btn_buy_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), SplassActivity.class));
                }
            });
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.api + "users?_id=" + curidUser, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String image = jsonObject.getString("image");
                        String name = jsonObject.getString("fullname");
                        Glide.with(getContext()).load(API.api_image + image).load(imginfo);

                        if (name.equals("")) {
                            txtfullname.setText("Cập nhập tên bạn!!!");
                            txtfullname.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(getContext(), InfoUserActivity.class));
                                }
                            });
                        }else{
                            txtfullname.setText(name);
                        }


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
}