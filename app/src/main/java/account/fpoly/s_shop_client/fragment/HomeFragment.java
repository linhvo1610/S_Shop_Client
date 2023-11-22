package account.fpoly.s_shop_client.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.InvalidParameterException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.API.API_Product;
import account.fpoly.s_shop_client.GiaoDien.ChitietProduct;
import account.fpoly.s_shop_client.GiaoDien.CustomDialog;
import account.fpoly.s_shop_client.Modal.CatModal;
import account.fpoly.s_shop_client.Modal.ProductModal;
import account.fpoly.s_shop_client.Modal.ReceProduct;
import account.fpoly.s_shop_client.Notification;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.Service.IClickItemListener;
import account.fpoly.s_shop_client.adapter.ProductAdapter;
import account.fpoly.s_shop_client.adapter.ProductNewAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    public static final String TAG = HomeFragment.class.getName();
    EditText etd_timkiem;
    ImageView chat_admin,notification,dialog;
    ProductAdapter productAdapter;
    ProductNewAdapter productNewAdapter;
    private List<ProductModal> listproduct;
    TextView chuyen;
    RecyclerView recyclerView,rcv_new;
    private String anhProduct, tenProduct, giaProduct;

    CustomDialog dialog1;
    String minPrice = null;
    String maxPrice = null;
    LinearLayout nameLayout;

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
        dialog=view.findViewById(R.id.dialog);


        recyclerView=view.findViewById(R.id.rcv_product);
        rcv_new=view.findViewById(R.id.rcv_new);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcv_new.setLayoutManager(layoutManager);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        listproduct= new ArrayList<>();


        callApiSeviceListProduct();
        callApiSeviceListProductHot();



        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1=new CustomDialog(getContext());
                ImageView dimiss = dialog1.findViewById(R.id.dimiss);
//                nameLayout = dialog1.findViewById(R.id.nameLayout);
                dimiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                ArrayList<String> spinnerData = new ArrayList<>();
                spinnerData.add("Tìm kiếm sản phẩm");
                spinnerData.add("All sản phẩm");
                spinnerData.add("Dưới 200k");
                spinnerData.add("200k - 500k");
                spinnerData.add("500k - 1triệu");
                spinnerData.add("1triệu - 3triệu");
                spinnerData.add("3triệu - 5triệu");
                spinnerData.add("Trên 5triệu");
                Spinner spinner = dialog1.findViewById(R.id.spinner);

                spinner.setBackgroundColor(Color.LTGRAY);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, spinnerData);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItem = spinnerData.get(position);

                        // Xử lý sự kiện khi một mục được chọn trong Spinner
                        if (selectedItem.equals("All sản phẩm")) {
                            minPrice = null;
                            maxPrice = null;
                            callApiSeviceListProduct();
                            dialog1.dismiss();
                        }
                        else if (selectedItem.equals("Dưới 200k")) {
                            minPrice = "0";
                            maxPrice= "200000";
                        } else if (selectedItem.equals("200k - 500k")) {
                            maxPrice = "500000";
                            minPrice = "200000";
                        } else if (selectedItem.equals("500k - 1triệu")) {
                            maxPrice = "1000000";
                            minPrice = "500000";
                        } else if (selectedItem.equals("1triệu - 3triệu")) {
                            maxPrice = "3000000";
                            minPrice = "1000000";
                        } else if (selectedItem.equals("3triệu - 5triệu")) {
                            maxPrice = "5000000";
                            minPrice = "3000000";
                        } else if (selectedItem.equals("Trên 5triệu")) {
                            minPrice = "5000000";
                            maxPrice = "100000000000";
                        }
                        filterByPrice(minPrice, maxPrice);
                        minPrice = null;
                        maxPrice = null;

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Xử lý sự kiện khi không có mục nào được chọn trong Spinner
                    }
                });
                ghetName();


                dialog1.show();
            }
        });


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
    private void ghetName() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.api + "filterproduct",
                null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    HashSet<String> uniqueNames = new HashSet<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        CatModal nameFilter = new CatModal();
                        try {
                            JSONObject jsonObjectCat = jsonObject.getJSONObject("id_cat");
                            String name = jsonObjectCat.getString("name");
                            String id = jsonObjectCat.getString("_id");

                            GridLayout nameLayout = dialog1.findViewById(R.id.nameLayout);

                            if (!uniqueNames.contains(name)) {
                                TextView textView = new TextView(getContext());
                                textView.setText(name);
                                textView.setLayoutParams(new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f)));
                                textView.setHeight(100);
                                textView.setWidth(170);
                                textView.setTextColor(Color.MAGENTA);
                                textView.setTextSize(16);
                                textView.setGravity(Gravity.CENTER);
                                nameLayout.addView(textView);
                                uniqueNames.add(name);
                                textView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        API_Product.apiProduct.filterName(name).enqueue(new Callback<ReceProduct>() {
                                            @Override
                                            public void onResponse(Call<ReceProduct> call, Response<ReceProduct> response) {
                                                if (response.isSuccessful()) {
                                                    listproduct.clear();
                                                    listproduct.addAll(response.body().getData());
                                                    productAdapter.notifyDataSetChanged();
                                                    dialog1.dismiss();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ReceProduct> call, Throwable t) {
                                                Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                                                Log.d("gggg", "onFailure:" + t);
                                            }
                                        });
                                    }
                                });

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
//                        listFilter.add(nameFilter);
                    }
//                    filterNameAdapter = new FilterNameAdapter(listFilter.stream().filter(nameFilter -> nameFilter.getName() != null && !nameFilter.getName().isEmpty()).collect(Collectors.toList()),getContext());
//                    rcvFilter.setAdapter(filterNameAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    private void filterByPrice(String minPrice, String maxPrice) {
        API_Product.apiProduct.filterProducts(minPrice, maxPrice).enqueue(new Callback<List<ProductModal>>() {
            @Override
            public void onResponse(Call<List<ProductModal>> call, Response<List<ProductModal>> response) {
                if (response.isSuccessful()) {
                    listproduct.clear();
                    listproduct.addAll(response.body());
                    productAdapter.notifyDataSetChanged();
                    dialog1.dismiss();


                }else{
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductModal>> call, Throwable t) {

            }
        });
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
                        String keyword=removeVietnameseDiacritics(etd_timkiem.getText().toString().trim());
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
    private void callApiSeviceListProductHot() {
        API_Product.apiProduct.listProductNew().enqueue(new Callback<ReceProduct>() {
            @Override
            public void onResponse(Call<ReceProduct> call, Response<ReceProduct> response) {
                listproduct = response.body().getData();
                productNewAdapter = new ProductNewAdapter(listproduct, getContext(), new IClickItemListener() {
                    @Override
                    public void onCLickItemProduct(ProductModal productModal) {
                        onClickGoToDetailProduct(productModal);
                    }
                });
                rcv_new.setAdapter(productNewAdapter);
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
        String normalizedKeyword = removeVietnameseDiacritics(keyword);
        for (ProductModal product : listproduct) {
            String normalizedProductName = removeVietnameseDiacritics(product.getName());
            if (normalizedProductName.contains(normalizedKeyword)) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }
    @Override
    public void onResume() {
        super.onResume();
        callApiSeviceListProduct();
        callApiSeviceListProductHot();
    }
    private String removeVietnameseDiacritics(String text) {
        String normalizedText = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalizedText).replaceAll("").toLowerCase();
    }
}