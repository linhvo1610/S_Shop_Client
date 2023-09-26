package account.fpoly.s_shop_client.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import account.fpoly.s_shop_client.Chat_Admin;
import account.fpoly.s_shop_client.ChitietProduct;
import account.fpoly.s_shop_client.R;


public class HomeFragment extends Fragment {

    ImageView chat_admin;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_home, container, false);

        LinearLayout iteam1 = view.findViewById(R.id.iteam1);
        chat_admin = view.findViewById(R.id.chat_admin);
        chat_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Chat_Admin.class));
            }
        });

        iteam1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChitietProduct.class));
            }
        });

        return view;
    }
}