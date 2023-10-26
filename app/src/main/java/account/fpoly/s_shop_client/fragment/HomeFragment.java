package account.fpoly.s_shop_client.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import account.fpoly.s_shop_client.API.API_Product;
import account.fpoly.s_shop_client.Chat_Admin;
import account.fpoly.s_shop_client.ChitietProduct;
import account.fpoly.s_shop_client.Modal.ProductModal;
import account.fpoly.s_shop_client.Modal.ReceProduct;
import account.fpoly.s_shop_client.Notification;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.adapter.ProductAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    ImageView chat_admin,notification;
    ProductAdapter productAdapter;
    private List<ProductModal> listproduct;

    TextView chuyen;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=view.findViewById(R.id.rcv_product);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        listproduct= new ArrayList<>();
        callApiSeviceListProduct();

//        chat_admin = view.findViewById(R.id.chat_admin);
        notification= view.findViewById(R.id.iconNotification);
        chuyen= view.findViewById(R.id.chuyen);
//        chat_admin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), Chat_Admin.class));
//            }
//        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             startActivity(new Intent(getContext(), Notification.class));
            }
        });

        chuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChitietProduct.class));
            }
        });


        return view;
    }
    private void callApiSeviceListProduct() {
        API_Product.apiProduct.listProduct().enqueue(new Callback<ReceProduct>() {
            @Override
            public void onResponse(Call<ReceProduct> call, Response<ReceProduct> response) {
                listproduct = response.body().getData();
                productAdapter = new ProductAdapter(listproduct,getContext());
                recyclerView.setAdapter(productAdapter);

            }

            @Override
            public void onFailure(Call<ReceProduct> call, Throwable t) {

            }
        });
    }

}