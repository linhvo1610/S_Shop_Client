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

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

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



        int priceFormat = Integer.parseInt(productModal.getPrice());
        String Price = decimalFormat.format(priceFormat);
        productViewHoder.PriceProduct.setText(Price);


        Picasso.get().load(API.api_image + productModal.getImage()).into(holder.ImageProduct);

        int totalQuantity = 0;
        for (ProductModal.Size size : productModal.getSizes()) {
            totalQuantity += size.getQuantity();
        }

        holder.totalQuantity.setText(String.valueOf(totalQuantity));

        String id = productModal.getId();
        String sluong = String.valueOf(totalQuantity);
        String description = productModal.getDescription();

        productViewHoder.itemView.setOnClickListener(new View.OnClickListener() {
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


                editor.apply();
        ProductModal sp = list.get(position);
        if (sp == null){
                    return;
        }
        holder.NameProduct.setText(""+sp.getName());
        holder.PriceProduct.setText(""+sp.getPrice());
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
        private TextView NameProduct, PriceProduct,totalQuantity,Description;
        private ImageView ImageProduct;

        public ProductViewHoder(@NonNull View itemView) {
            super(itemView);
            NameProduct=itemView.findViewById(R.id.txt_nameproduct);
            PriceProduct=itemView.findViewById(R.id.txt_price_product);
            totalQuantity=itemView.findViewById(R.id.totalQuantity);
            ImageProduct=itemView.findViewById(R.id.img_product);
            Description=itemView.findViewById(R.id.chitiet_description);
        }
    }
}
