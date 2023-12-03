package account.fpoly.s_shop_client.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.AddCommentActivity;
import account.fpoly.s_shop_client.Modal.Cart;
import account.fpoly.s_shop_client.R;

public class NestesCommentAdapter extends RecyclerView.Adapter<NestesCommentAdapter.NestesViewHolder>{
    private List<Cart> itemList;
    Context context;

    public NestesCommentAdapter(List<Cart> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public NestesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iteam_nested_coment,parent,false);

        return new NestesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NestesViewHolder holder, int position) {
        Cart cart = itemList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        if (holder.namePro != null) {
            holder.namePro.setText(cart.getName_product());
        }
        int price = cart.getPrice_product();
        int priceFormat = Integer.parseInt(String.valueOf(price));
        String Price = decimalFormat.format(priceFormat);
        holder.totalQuantity.setText("x"+cart.getQuantity());

        holder.totalPrice.setText("đ"+ Price);
        holder.sizePro.setText("Size:"+ cart.getSize());
        // truyen du lieu sang comment

        Picasso.get().load(API.api_image + cart.getImage()).into(holder.imageBill);

        holder.danhgia.setVisibility(View.VISIBLE);
        holder.danhgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddCommentActivity.class);

                SharedPreferences sharedPreferences = context.getSharedPreferences("id_product", context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("idP", cart.getId_product());
                editor.apply();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                holder.danhgia.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class NestesViewHolder extends RecyclerView.ViewHolder {
        TextView totalQuantity,totalPrice,statusPro,namePro,sizePro;
        LinearLayout danhgia,btn_mualai;
        ImageView imageBill;
        public NestesViewHolder(@NonNull View itemView) {
            super(itemView);
            totalQuantity = itemView.findViewById(R.id.totalQuantitys);
            totalPrice = itemView.findViewById(R.id.totalPrices);
            namePro = itemView.findViewById(R.id.nameProbills);
            imageBill = itemView.findViewById(R.id.imageBills);
            sizePro = itemView.findViewById(R.id.sizePros);
            danhgia=itemView.findViewById(R.id.btn_danhgia);
            btn_mualai=itemView.findViewById(R.id.btn_mualai);
        }
    }
}
