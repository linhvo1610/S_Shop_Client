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

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.Modal.Cart;
import account.fpoly.s_shop_client.R;

public class NestedAdapter extends RecyclerView.Adapter<NestedAdapter.NestedViewHolder>{
    private List<Cart> itemList;
    Context context;


    public NestedAdapter(List<Cart> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public NestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iteam_nested,parent,false);

        return new NestedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NestedViewHolder holder, int position) {
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

        Picasso.get().load(API.api_image + cart.getImage()).into(holder.imageBill);



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class NestedViewHolder extends RecyclerView.ViewHolder {
        TextView totalQuantity,totalPrice,statusPro,namePro,sizePro;
        ImageView imageBill;
        public NestedViewHolder(@NonNull View itemView) {
            super(itemView);
            totalQuantity = itemView.findViewById(R.id.totalQuantitys);
            totalPrice = itemView.findViewById(R.id.totalPrices);
            namePro = itemView.findViewById(R.id.nameProbills);
            imageBill = itemView.findViewById(R.id.imageBills);
            sizePro = itemView.findViewById(R.id.sizePros);
        }
    }
}
