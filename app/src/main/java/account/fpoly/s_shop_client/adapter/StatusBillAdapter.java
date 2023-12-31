package account.fpoly.s_shop_client.adapter;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.API.API_Bill;
import account.fpoly.s_shop_client.Modal.BillMore;
import account.fpoly.s_shop_client.Modal.Cart;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.Service.ApiService;
import account.fpoly.s_shop_client.Tab_Giaodien_Activity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

        NestedAdapter nestedAdapter = new NestedAdapter(billMore.getList(),context);
        holder.nestedRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.nestedRecyclerView.setAdapter(nestedAdapter);

        String idbill = billMore.get_id();

        holder.huydon.setVisibility(View.GONE);
        if (billMore.getStatus() == 0){
            holder.statusPro.setVisibility(View.GONE);
            holder.huydon.setVisibility(View.VISIBLE);
            holder.huydon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                    View view = LayoutInflater.from(context).inflate(R.layout.dialog_huydon,null);
                    builder.setView(view);
                    AlertDialog dialog = builder.create();

                    TextView huydonDialog,dong;
                    huydonDialog = view.findViewById(R.id.huydondl);
                    dong = view.findViewById(R.id.dong);

                    huydonDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ApiService.apiService.cancelBill(billMore.get_id()).enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                    if (response.isSuccessful()&&response.body()!=null) {
                                        if (response.body() == 1) {
                                            Toast.makeText(context, "Đã hủy đơn hàng!!!", Toast.LENGTH_SHORT).show();
                                            list.remove(billMore);
                                            dialog.cancel();
                                            notifyDataSetChanged();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {

                                }
                            });
                        }
                    });
                    dong.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    ImageView imageView = view.findViewById(R.id.imageView);
                    ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", 0f, -15f, 20f, -15f, 0f);
                    animator.setDuration(1000);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setRepeatCount(ObjectAnimator.INFINITE);
                    animator.start();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    dialog.show();
                }
            });
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
                    ApiService.apiService.updateBill(billMore.get_id()).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if (response.isSuccessful()&&response.body()!=null) {
                                if (response.body() == 1) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                                    View view = LayoutInflater.from(context).inflate(R.layout.dialog_thongbao, null);
                                    builder.setView(view);
                                    AlertDialog dialog = builder.create();

                                    ImageView imageView = view.findViewById(R.id.imageView);
                                    TextView title = view.findViewById(R.id.title);
// Tạo hiệu ứng chuông rung
                                    title.setText("Cảm hơn bạn đã tin tưởng về sản phẩm chúc bạn có trải nghiệm tốt!!!");

                                    ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", 0f, -15f, 20f, -15f, 0f);
                                    animator.setDuration(1000);
                                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                                    animator.setRepeatCount(ObjectAnimator.INFINITE);
                                    animator.start();

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            list.remove(billMore);
                                            notifyDataSetChanged();
                                            dialog.dismiss();
//                                            Intent intent = new Intent(context, Tab_Giaodien_Activity.class);
//
//                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                            context.startActivity(intent);
                                        }
                                    }, 1700);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.show();

                                } 
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {

                        }
                    });

                }
            });;
        }
        // hủy đơn
        else if (billMore.getStatus() == 4){
            holder.statusPro.setVisibility(View.INVISIBLE);
            holder.xacnhanPro.setVisibility(View.INVISIBLE);
            holder.mualai.setVisibility(View.VISIBLE);
            holder.mualai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                    View view = LayoutInflater.from(context).inflate(R.layout.dialog_huydon,null);
                    builder.setView(view);
                    AlertDialog dialog1 = builder.create();

                    TextView huydonDialog,dong,title;
                    huydonDialog = view.findViewById(R.id.huydondl);
                    dong = view.findViewById(R.id.dong);
                    title = view.findViewById(R.id.title);
                    title.setText("Mua lại sản phẩm!!!");
                    huydonDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                    ApiService.apiService.updateBillHuy(billMore.get_id()).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if (response.isSuccessful()&&response.body()!=null) {
                                if (response.body() == 1) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                                    View view = LayoutInflater.from(context).inflate(R.layout.dialog_thongbao, null);
                                    builder.setView(view);
                                    AlertDialog dialog = builder.create();

                                    ImageView imageView = view.findViewById(R.id.imageView);
                                    TextView title = view.findViewById(R.id.title);
// Tạo hiệu ứng chuông rung
                                    title.setText("Đơn hàng của bạn đã được mua lại!!!");

                                    ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", 0f, -15f, 20f, -15f, 0f);
                                    animator.setDuration(1000);
                                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                                    animator.setRepeatCount(ObjectAnimator.INFINITE);
                                    animator.start();

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            list.remove(billMore);
                                            notifyDataSetChanged();
                                            dialog.dismiss();
//                                            Intent intent = new Intent(context, Tab_Giaodien_Activity.class);
//
//                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                            context.startActivity(intent);

                                        }
                                    }, 1700);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.show();

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {

                        }
                    });
                        }
                    });
                    dong.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.cancel();
                        }
                    });
                    ImageView imageView = view.findViewById(R.id.imageView);
                    ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", 0f, -15f, 20f, -15f, 0f);
                    animator.setDuration(1000);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setRepeatCount(ObjectAnimator.INFINITE);
                    animator.start();
                    dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    dialog1.show();
                }
            });

        }
        // lich su dat hang
        else if (billMore.getStatus() == 5){
            holder.statusPro.setVisibility(View.INVISIBLE);
            holder.xacnhanPro.setVisibility(View.INVISIBLE);
            holder.statusPro.setVisibility(View.INVISIBLE);

            NestesCommentAdapter nestesCommentAdapter = new NestesCommentAdapter(billMore.getList(),context);
            holder.nestedRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            holder.nestedRecyclerView.setAdapter(nestesCommentAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class StatusBillViewHolder extends RecyclerView.ViewHolder {
        TextView totalPrice,statusPro,xacnhanPro,huydon;
        TextView maDonhang,tv_name,tv_phonenumber,tv_address,mualai;
        ImageView imageBill;
        RecyclerView nestedRecyclerView;
        public StatusBillViewHolder(@NonNull View itemView) {
            super(itemView);

            maDonhang = itemView.findViewById(R.id.maDonhang);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_phonenumber = itemView.findViewById(R.id.tv_phonenumber);
            tv_address = itemView.findViewById(R.id.tv_address);
            mualai = itemView.findViewById(R.id.mualai);



            xacnhanPro = itemView.findViewById(R.id.xacnhanPros);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            statusPro = itemView.findViewById(R.id.statusPro);
            huydon = itemView.findViewById(R.id.huydon);
            nestedRecyclerView = itemView.findViewById(R.id.nestedRecyclerView);
        }
    }
}
