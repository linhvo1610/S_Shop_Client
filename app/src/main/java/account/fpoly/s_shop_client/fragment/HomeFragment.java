package account.fpoly.s_shop_client.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.API.API_Product;
import account.fpoly.s_shop_client.GiaoDien.ChitietProduct;
import account.fpoly.s_shop_client.Modal.ProductModal;
import account.fpoly.s_shop_client.Modal.ReceProduct;
import account.fpoly.s_shop_client.Notification;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.Service.IClickItemListener;
import account.fpoly.s_shop_client.adapter.ProductAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    public static final String TAG = HomeFragment.class.getName();
    EditText etd_timkiem;
    ImageView chat_admin,notification;
    ProductAdapter productAdapter;
    private List<ProductModal> listproduct;
    TextView chuyen;
    RecyclerView recyclerView;
    private String anhProduct, tenProduct, giaProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_home, container, false);

        // Truyền dữ liệu vào Chi Tiết Activity
        Bundle bundle = getArguments();
        if (bundle != null){
            tenProduct = bundle.getString("tenProduct");
            giaProduct = bundle.getString("giaProduct");
            anhProduct = bundle.getString("anhProduct");
        }
        //=====================================
        etd_timkiem=view.findViewById(R.id.edt_timkiem);
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

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             startActivity(new Intent(getContext(), Notification.class));
            }
        });



        return view;
    }
    private void callApiSeviceListProduct() {
        API_Product.apiProduct.listProduct().enqueue(new Callback<ReceProduct>() {
            @Override
            public void onResponse(Call<ReceProduct> call, Response<ReceProduct> response) {
                listproduct = response.body().getData();
                productAdapter = new ProductAdapter(listproduct, getContext(), new IClickItemListener() {
                    @Override
                    public void onCLickItemProduct(ProductModal productModal) {
                        onClickGoToDetailProduct(productModal);
                    }
                });
                recyclerView.setAdapter(productAdapter);
                etd_timkiem.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        String keyword=etd_timkiem.getText().toString().toLowerCase();
                        List<ProductModal> serarchResult=searchProducts(keyword);
                        productAdapter= new ProductAdapter(serarchResult, getContext(), new IClickItemListener() {
                            @Override
                            public void onCLickItemProduct(ProductModal productModal) {
                                onClickGoToDetailProduct(productModal);
                            }
                        });
                        recyclerView.setAdapter(productAdapter);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

            }

            @Override
            public void onFailure(Call<ReceProduct> call, Throwable t) {
                try {
                    throw t;
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void onClickGoToDetailProduct(ProductModal productModal){
        Intent intent = new Intent(getContext(), ChitietProduct.class);
        intent.putExtra("id", productModal.getId());
        intent.putExtra("tenProduct", tenProduct);
        intent.putExtra("giaProduct", giaProduct);
        intent.putExtra("anhProduct", anhProduct);
        startActivity(intent);
    }
    private List<ProductModal> searchProducts(String keyword) {
        List<ProductModal> filteredProducts = new ArrayList<>();
        for (ProductModal product : listproduct) {
            if (product.getName().contains(keyword)) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }
    @Override
    public void onResume() {
        super.onResume();
        callApiSeviceListProduct();
    }
}