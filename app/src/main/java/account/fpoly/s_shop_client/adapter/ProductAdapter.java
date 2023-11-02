package account.fpoly.s_shop_client.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

        productViewHoder.NameProduct.setText("" + productModal.getName());
        productViewHoder.PriceProduct.setText("" + productModal.getPrice());


        Picasso.get().load(API.api_reg + productModal.getImage()).into(holder.ImageProduct);

        int totalQuantity = 0;
        for (ProductModal.Size size : productModal.getSizes()) {
            totalQuantity += size.getQuantity();
        }

//        StringBuilder sizesBuilder = new StringBuilder();
//        String sizeLog = null;
//        for (ProductModal.Size size : productModal.getSizes()) {
//            sizesBuilder.append(size.getSize()).append(", ");
//            sizeLog = String.valueOf(size.getSize());
//        }
//        String sizes = sizesBuilder.toString();
//        if (sizes.length() > 0) {
//            sizes = sizes.substring(0, sizes.length() - 2); // Loại bỏ dấu phẩy cuối cùng
//        }
//-------------
        StringBuilder sizesBuilder = new StringBuilder();
        for (ProductModal.Size size : productModal.getSizes()) {
            sizesBuilder.append(size.getSize()).append(", ");
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(String.valueOf(size.getSize()));

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // Kích thước đã được chọn
                        String selectedSize = buttonView.getText().toString();
                        Toast.makeText(context, "Đã chọn kích thước: " + selectedSize, Toast.LENGTH_SHORT).show();
                    } else {
                        // Kích thước đã bị bỏ chọn
                        String deselectedSize = buttonView.getText().toString();
                        Toast.makeText(context, "Đã bỏ chọn kích thước: " + deselectedSize, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.checkboxLayout.addView(checkBox);
        }
        String sizes = sizesBuilder.toString();
        if (sizes.length() > 0) {
            sizes = sizes.substring(0, sizes.length() - 2); // Loại bỏ dấu phẩy cuối cùng
        }
//--    -------

        holder.sizePro.setText(String.valueOf(sizes));

        holder.totalQuantity.setText(String.valueOf(totalQuantity));

        String id = productModal.getId();
        String sluong = String.valueOf(totalQuantity);
        String description = productModal.getDescription();
        String sizePro = String.valueOf(sizes);

        Log.d("TAG", "sizeeeee: "+ sizePro);
        String finalSizes = sizes;
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
                editor.putString("sizeProPro", sizePro);


                for (ProductModal.Size size : productModal.getSizes()) {
                    String sizeKey = "size_" + size.getSize();
                    editor.putBoolean(sizeKey, true);
                    Log.d("TAG", "sizeeeee: "+ sizeKey);
//                    editor.putString ("size_", sizeKey);

                }


                editor.apply();
        ProductModal sp = list.get(position);
        if (sp == null){
                    return;
        }
        holder.NameProduct.setText(""+sp.getName());
        holder.PriceProduct.setText(""+sp.getPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChitietProduct.class);
                ArrayList<String> selectedSizes = new ArrayList<>();

                for (ProductModal.Size size : productModal.getSizes()) {
                    String sizeKey = "size_" + size.getSize();
                    editor.putBoolean(sizeKey, true);
                    Log.d("TAG", "sizeeeee: "+ sizeKey);
                    selectedSizes.add(sizeKey);
                    Log.d("TAG", "sele[]"+ selectedSizes );

                }
                intent.putStringArrayListExtra("selectedSizes",  selectedSizes);

                context.startActivity(intent);

            }
        });

                iClickItemListener.onCLickItemProduct(productModal);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductViewHoder extends RecyclerView.ViewHolder {
        private TextView NameProduct, PriceProduct,totalQuantity,sizePro,Description;
        private ImageView ImageProduct;
        LinearLayout checkboxLayout;

        public ProductViewHoder(@NonNull View itemView) {
            super(itemView);
            NameProduct=itemView.findViewById(R.id.txt_nameproduct);
            PriceProduct=itemView.findViewById(R.id.txt_price_product);
            totalQuantity=itemView.findViewById(R.id.totalQuantity);
            ImageProduct=itemView.findViewById(R.id.img_product);
            sizePro=itemView.findViewById(R.id.sizePro);
            Description=itemView.findViewById(R.id.chitiet_description);
            checkboxLayout=itemView.findViewById(R.id.checkboxLayout);
        }
    }
}
