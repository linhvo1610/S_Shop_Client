package account.fpoly.s_shop_client.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import account.fpoly.s_shop_client.API.API_User;
import account.fpoly.s_shop_client.HistoryOrderClient;
import account.fpoly.s_shop_client.InfoUserActivity;
import account.fpoly.s_shop_client.R;

public class SettingsFragment extends Fragment {
    ImageView imginfo, imghistory;
    API_User api_user;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_setting_user, container, false);
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