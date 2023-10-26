package account.fpoly.s_shop_client.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import account.fpoly.s_shop_client.API.API_User;
import account.fpoly.s_shop_client.ChitietProduct;
import account.fpoly.s_shop_client.GiaoDien.DangNhapActivity;
import account.fpoly.s_shop_client.HistoryOrderClient;
import account.fpoly.s_shop_client.InfoUserActivity;
import account.fpoly.s_shop_client.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingsFragment extends Fragment {
    ImageView imginfo, imghistory;
    API_User api_user;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_setting_user, container, false);
        imginfo=view.findViewById(R.id.img_info);
        imghistory=view.findViewById(R.id.img_history);
        imghistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), HistoryOrderClient.class));
            }
        });

        imginfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), InfoUserActivity.class));
            }
        });
        return view;
    }

    }