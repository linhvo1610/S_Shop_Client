package account.fpoly.s_shop_client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.Modal.BillMore;
import account.fpoly.s_shop_client.Modal.Cart;
import account.fpoly.s_shop_client.R;


public class StatusBillAdapter extends RecyclerView.Adapter<StatusBillAdapter.StatusBillViewHolder>{
    Context context;
    List<BillMore> list;

    public StatusBillAdapter(Context context, List<BillMore> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public StatusBillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.iteam_status_product,parent,false);
        return new StatusBillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusBillViewHolder holder, int position) {
        BillMore billMore = list.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        if (holder.tv_name != null) {
            holder.tv_name.setText(billMore.getName());
        }
        holder.maDonhang.setText(billMore.get_id());
        holder.tv_phonenumber.setText(billMore.getPhone());
        holder.tv_address.setText(billMore.getAddress());

        int  price = billMore.getTotal();
        int priceFormat = Integer.parseInt(String.valueOf(price));
        String Price = decimalFormat.format(priceFormat);
        holder.totalPrice.setText("Tổng tiền: đ"+ Price);




        NestedAdapter nestedAdapter = new NestedAdapter(billMore.getList());
        holder.nestedRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.nestedRecyclerView.setAdapter(nestedAdapter);


        holder.huydon.setVisibility(View.GONE);
        if (billMore.getStatus() == 0){
            holder.statusPro.setText("Chờ xác nhận");
            holder.huydon.setVisibility(View.VISIBLE);
//            holder.huydon.setText("Huy don");
        }
        // đang giao
        else if (billMore.getStatus() == 2){
            holder.statusPro.setVisibility(View.INVISIBLE);
            holder.huydon.setVisibility(View.INVISIBLE);
        }
        // Đã nhận hàng
        else if (billMore.getStatus() == 3){
            holder.statusPro.setVisibility(View.INVISIBLE);
            holder.huydon.setVisibility(View.INVISIBLE);
            holder.xacnhanPro.setVisibility(View.VISIBLE);
            holder.xacnhanPro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });;
        }
        // hủy đơn
        else if (billMore.getStatus() == 4){
            holder.statusPro.setVisibility(View.INVISIBLE);
            holder.xacnhanPro.setVisibility(View.INVISIBLE);

        }
        // lich su dat hang
        else if (billMore.getStatus() == 5){
            holder.statusPro.setVisibility(View.INVISIBLE);
            holder.xacnhanPro.setVisibility(View.INVISIBLE);
            holder.statusPro.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class StatusBillViewHolder extends RecyclerView.ViewHolder {
        TextView totalPrice,statusPro,xacnhanPro,huydon;
        TextView maDonhang,tv_name,tv_phonenumber,tv_address;
        ImageView imageBill;
        RecyclerView nestedRecyclerView;
        public StatusBillViewHolder(@NonNull View itemView) {
            super(itemView);

            maDonhang = itemView.findViewById(R.id.maDonhang);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_phonenumber = itemView.findViewById(R.id.tv_phonenumber);
            tv_address = itemView.findViewById(R.id.tv_address);



            xacnhanPro = itemView.findViewById(R.id.xacnhanPros);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            statusPro = itemView.findViewById(R.id.statusPro);
            huydon = itemView.findViewById(R.id.huydon);
            nestedRecyclerView = itemView.findViewById(R.id.nestedRecyclerView);
        }
    }
}
