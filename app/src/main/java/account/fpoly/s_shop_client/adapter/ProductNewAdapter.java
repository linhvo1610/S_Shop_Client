package account.fpoly.s_shop_client.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.List;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.Modal.ProductModal;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.Service.IClickItemListener;

public class ProductNewAdapter extends RecyclerView.Adapter<ProductNewAdapter.ProductNewViewHolder>{

    private final List<ProductModal> list;
    List<ProductModal.Size> listSize;
    private final Context context;
    String firstImage;
    private final IClickItemListener iClickItemListener;
    String iduser;
    public ProductNewAdapter(List<ProductModal> list, Context context, IClickItemListener iClickItemListener) {
        this.list = list;
        this.context=context;
        this.iClickItemListener = iClickItemListener;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ProductNewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iteam_product_hot,parent,false);
        return new ProductNewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductNewViewHolder holder, int position) {

        ProductNewViewHolder productViewHoder = holder;
        ProductModal productModal = list.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        productViewHoder.NameProduct.setText("" + productModal.getName());
        productViewHoder.PriceProduct.setText("" + productModal.getPrice());
        productViewHoder.Category.setText(""+ productModal.getId_cat().getName());




        int priceFormat = Integer.parseInt(productModal.getPrice());
        String Price = decimalFormat.format(priceFormat);
        productViewHoder.PriceProduct.setText(Price);


//        Picasso.get().load(API.api_image + productModal.getImage()).into(holder.ImageProduct);


        // lay anh
        List<ProductModal.ImageItem> images = productModal.getImages();
        // Hiển thị ảnh đầu tiên từ danh sách hình ảnh (nếu có)
        if (images != null && !images.isEmpty()) {
            firstImage = images.get(0).getImage();
            Glide.with(context).load(API.api_image + firstImage).into(holder.ImageProduct);
        }
//----------


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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.api + "billQu?id_product=" + id + "&status=5", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    int totalQuantityBill = 0;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject billObject = jsonArray.getJSONObject(i);
                        JSONArray productList = billObject.getJSONArray("list");
                        for (int j = 0; j < productList.length(); j++) {
                            JSONObject product = productList.getJSONObject(j);

                            String idProductbill = product.getString("id_product");
                            if (idProductbill.equalsIgnoreCase(productModal.getId())) {
                                int quantity = product.getInt("quantity");
                                totalQuantityBill += quantity;
                            }
                        }
                    }
                    holder.totalQuantity.setText(String.valueOf(totalQuantityBill));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        requestQueue.add(jsonObjectRequest);


        productViewHoder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (images != null && !images.isEmpty()) {
                    firstImage = images.get(0).getImage();
                }
//                Log.d("TAG", "sizeeeee: "+ finalSizes);
                SharedPreferences sharedPreferences = context.getSharedPreferences("product", context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("idProduct", id);
                editor.putString("tenProduct", productModal.getName());
                editor.putString("giaProduct", productModal.getPrice());
                editor.putString("anhProduct", firstImage);
                editor.putString("quantityPro", sluong);
                editor.putString("descriptionPro", description);
                editor.putString("image", firstImage);
                editor.putString("trademark", productModal.getTrademark());
                editor.putString("namecat", productModal.getId_cat().getName());

// gui mang images
                Gson gson = new Gson();
                Type type = new TypeToken<List<ProductModal.ImageItem>>(){}.getType();
                String imagesJson = gson.toJson(productModal.getImages(), type);
                editor.putString("images", imagesJson);


                editor.apply();
                ProductModal sp = list.get(position);
                if (sp == null){
                    return;
                }
                holder.NameProduct.setText(""+sp.getName());
                holder.PriceProduct.setText(""+sp.getPrice());
                holder.Category.setText(""+sp.getId_cat().getName());

                String urlImage = API.api+firstImage;
                Glide.with(holder.itemView).load(urlImage).into(holder.ImageProduct);


                iClickItemListener.onCLickItemProduct(productModal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ProductNewViewHolder extends RecyclerView.ViewHolder {
        private TextView NameProduct, PriceProduct,totalQuantity,Description, Category;
        private ImageView ImageProduct;
        public ProductNewViewHolder(@NonNull View itemView) {
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
