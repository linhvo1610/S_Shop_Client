package account.fpoly.s_shop_client.adapter;

import static android.app.PendingIntent.getActivity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.API.API_Bill;
import account.fpoly.s_shop_client.AddCommentActivity;
import account.fpoly.s_shop_client.GiaoDien.ChitietProduct;
import account.fpoly.s_shop_client.Modal.Bill;
import account.fpoly.s_shop_client.Modal.ProductModal;
import account.fpoly.s_shop_client.R;
import account.fpoly.s_shop_client.Tab_Giaodien_Activity;
import account.fpoly.s_shop_client.Xacnhan_Bill;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder>{

    Context context;
    List<Bill> list;
    List<String> product;

    public BillAdapter(Context context, List<Bill> list) {
        this.context = context;
        this.list = list;
    }
    String idBill,status,iduser,address,image;
    int quantity,size;
    double price;

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.iteam_trangthai_bill,parent,false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Bill bill = list.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        double priceBill = bill.getTotalPrice();
        int roundedPrice = (int) Math.floor(priceBill);

        int priceFormat = Integer.parseInt(String.valueOf(roundedPrice));
        String Price = decimalFormat.format(priceFormat);
        holder.totalPrice.setText("đ"+Price);
        holder.totalQuantity.setText("x"+bill.getTotalQuantity());
        holder.statusPro.setText(bill.getStatus());
        holder.sizePro.setText("Size "+bill.getSize());
        holder.namePro.setText(bill.getName());

        Glide.with(holder.itemView).load(API.api_image + bill.getImage()).into(holder.imageBill);
        idBill = bill.getId();
        SharedPreferences preferences = context.getSharedPreferences("infoUser",context.MODE_PRIVATE);
        iduser = preferences.getString("iduser",null);

//        Toast.makeText(context, iduser, Toast.LENGTH_SHORT).show();
        address = bill.getId_address();
        price = bill.getTotalPrice();
        quantity = bill.getTotalQuantity();
        image = bill.getImage();
        size = bill.getSize();
        status = "Đã nhận";
        String idpro = bill.get_idPro();

        product = new ArrayList<>();
// Số lượng sản phẩm muốn mua
        int numberOfProducts = 1;
// Vòng lặp để tự động tăng số lượng sản phẩm
        for (int i = 1; i <= numberOfProducts; i++) {
            String idProduct = bill.get_idPro();
            product.add(idProduct);
        }
//        Toast.makeText(context, idpro, Toast.LENGTH_SHORT).show();

        String statusBill = bill.getStatus();
        SharedPreferences sharedPreferences = context.getSharedPreferences("bill", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("idBill", idBill);
        editor.putString("idUser", iduser);
        editor.putString("status", statusBill);
        editor.apply();
        if (statusBill.equals("Chờ xác nhận")){
            holder.huydon.setVisibility(View.VISIBLE);
            holder.huydon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(v.getRootView().getContext());
                    View view = LayoutInflater.from(context).inflate(R.layout.dialog_huydon,null);
                    builder.setView(view);
                    AlertDialog dialog = builder.create();

                    TextView huydonDialog,dong;
                    huydonDialog = view.findViewById(R.id.huydondl);
                    dong = view.findViewById(R.id.dong);

                    huydonDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            API_Bill.apiBill.huyOder(idBill).enqueue(new Callback<Bill>() {
                                @Override
                                public void onResponse(Call<Bill> call, Response<Bill> response) {
                                    Toast.makeText(context, "Huy don thanh cong", Toast.LENGTH_SHORT).show();
                                    list.remove(position);
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<Bill> call, Throwable t) {
                                    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
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
// Tạo hiệu ứng chuông rung
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
        else if (statusBill.equals("Đã giao")){
            holder.xacnhanPro.setVisibility(View.VISIBLE);
            holder.xacnhanPro.setText("Xac nhan");
            holder.xacnhanPro.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddCommentActivity.class);
                    intent.putExtra("id_product",idpro);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);


                    API_Bill.apiBill.updateBill(idBill, new Bill(status,iduser,product,address,quantity,price,size)).enqueue(new Callback<Bill>() {
                        @Override
                        public void onResponse(Call<Bill> call, Response<Bill> response) {

                            Toast.makeText(context, "Cap nhap thanh cong", Toast.LENGTH_SHORT).show();
                            list.set(holder.getAdapterPosition(), new Bill(idBill,status,iduser,product,address,quantity,price,size));
                            holder.xacnhanPro.setVisibility(View.GONE);

                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<Bill> call, Throwable t) {

                        }
                    });

                }
            });
        }
        else{
            holder.huydon.setVisibility(View.GONE);
        }



    }




    private void xacnhan() {
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class BillViewHolder extends ViewHolder {
        TextView totalQuantity,totalPrice,statusPro,namePro,sizePro,huydon,xacnhanPro;
        ImageView imageBill;
        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            totalQuantity = itemView.findViewById(R.id.totalQuantity);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            statusPro = itemView.findViewById(R.id.statusPro);
            namePro = itemView.findViewById(R.id.nameProbill);
            imageBill = itemView.findViewById(R.id.imageBill);
            sizePro = itemView.findViewById(R.id.sizePro);
            huydon = itemView.findViewById(R.id.huydon);
            xacnhanPro = itemView.findViewById(R.id.xacnhanPro);
        }
    }
}
