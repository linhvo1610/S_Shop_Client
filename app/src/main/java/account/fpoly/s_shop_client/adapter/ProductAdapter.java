package account.fpoly.s_shop_client.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import account.fpoly.s_shop_client.GiaoDien.ChitietProduct;
import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.Modal.ProductModal;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.Service.IClickItemListener;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHoder> {
    private final List<ProductModal> list;
    List<ProductModal.Size> listSize;
    private final Context context;
    private final IClickItemListener iClickItemListener;
    String iduser;
    int totalQuantityBill;
    public ProductAdapter(List<ProductModal> list, Context context, IClickItemListener iClickItemListener) {
        this.list = list;
        this.context=context;
        this.iClickItemListener = iClickItemListener;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ProductViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iteam_product,parent,false);
        return new ProductViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHoder holder, int position) {
        ProductViewHoder productViewHoder = holder;
        ProductModal productModal = list.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        productViewHoder.NameProduct.setText("" + productModal.getName());
        productViewHoder.PriceProduct.setText("" + productModal.getPrice());
        productViewHoder.Category.setText(""+ productModal.getId_cat().getName());




        int priceFormat = Integer.parseInt(productModal.getPrice());
        String Price = decimalFormat.format(priceFormat);
        productViewHoder.PriceProduct.setText(Price);


        Picasso.get().load(API.api_image + productModal.getImage()).into(holder.ImageProduct);

        int totalQuantity = 0;
        for (ProductModal.Size size : productModal.getSizes()) {
            totalQuantity += size.getQuantity();
        }

        SharedPreferences preferencesbill = context.getSharedPreferences("billPro",context.MODE_PRIVATE);
        String sluongMua = preferencesbill.getString("sluongMua",null);

//        holder.totalQuantity.setText(String.valueOf(totalQuantity));
//        holder.totalQuantity.setText(String.valueOf(sluongMua));

        String id = productModal.getId();
        String sluong = String.valueOf(totalQuantity);
        String description = productModal.getDescription();


        RequestQueue requestQueue = Volley.newRequestQueue(context);
        SharedPreferences preferences = context.getSharedPreferences("infoUser",context.MODE_PRIVATE);
        iduser = preferences.getString("iduser", null);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.api + "billQu?" +"&status=5&id_product=" + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        totalQuantityBill = 0;
                        // Lặp qua từng đối tượng trong JSONArray
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(j);
                            JSONArray jsonArrayPro = jsonObject.getJSONArray("list");
                            for (int k = 0; k < jsonArrayPro.length(); k++) {
//                                JSONObject jsonObjectPro = jsonArrayPro.getJSONObject(k);
//                                int quantity = jsonObjectPro.getInt("quantity");
//                                totalQuantityBill += quantity;
                                JSONObject jsonObjectPro = jsonArrayPro.getJSONObject(k);
                                String idProduct = jsonObjectPro.getString("id_product");
                                if (idProduct.equals(id)) { // Kiểm tra xem id_product có phải là id cần tìm không
                                    int quantity = jsonObjectPro.getInt("quantity");
                                    totalQuantityBill += quantity;
                                }
                            }
                        }
                        SharedPreferences sharedPreferences = context.getSharedPreferences("billPro", context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("sluongMua", String.valueOf(totalQuantityBill));
                        editor.apply();
                        holder.totalQuantity.setText(" "+totalQuantityBill);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Call API Fail", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);


        productViewHoder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

//                Log.d("TAG", "sizeeeee: "+ finalSizes);
                SharedPreferences sharedPreferences = context.getSharedPreferences("product", context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("idProduct", id);
                editor.putString("tenProduct", productModal.getName());
                editor.putString("giaProduct", productModal.getPrice());
                editor.putString("anhProduct", productModal.getImage());
                editor.putString("quantityPro", sluong);
                editor.putString("descriptionPro", description);
                editor.putString("image", productModal.getImage());
                editor.putString("trademark", productModal.getTrademark());
                editor.putString("namecat", productModal.getId_cat().getName());
                editor.putString("importPrice", productModal.getImportPrice());


                editor.apply();
                ProductModal sp = list.get(position);
                if (sp == null){
                    return;
                }
                holder.NameProduct.setText(""+sp.getName());
                holder.PriceProduct.setText(""+sp.getPrice());
                holder.Category.setText(""+sp.getId_cat().getName());

                String urlImage = API.api+sp.getImage();
                Glide.with(holder.itemView).load(urlImage).into(holder.ImageProduct);


                iClickItemListener.onCLickItemProduct(productModal);
            }
        });

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductViewHoder extends RecyclerView.ViewHolder {
        private TextView NameProduct, PriceProduct,totalQuantity,Description, Category;
        private ImageView ImageProduct;

        public ProductViewHoder(@NonNull View itemView) {
            super(itemView);
            NameProduct=itemView.findViewById(R.id.txt_nameproduct);
            PriceProduct=itemView.findViewById(R.id.txt_price_product);
            totalQuantity=itemView.findViewById(R.id.totalQuantity);
            ImageProduct=itemView.findViewById(R.id.img_product);
            Description=itemView.findViewById(R.id.chitiet_description);
            Category=itemView.findViewById(R.id.txt_Catproduct);
        }
    }
}
