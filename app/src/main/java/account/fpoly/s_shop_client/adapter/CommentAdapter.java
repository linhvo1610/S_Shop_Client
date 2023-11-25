package account.fpoly.s_shop_client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.Modal.CommentModal;
import account.fpoly.s_shop_client.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHoder> {
 List<CommentModal> list;
Context context;
    public CommentAdapter(List<CommentModal> list, Context context) {
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public CommentViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
            return  new CommentViewHoder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHoder holder, int position) {
        CommentModal commentModal =list.get(position);
        holder.tv_Username.setText(commentModal.getFullname());
        holder.tv_comment.setText(commentModal.getComment());
        holder.tv_productname_comment.setText(commentModal.getName());

        Glide.with(holder.itemView).load(API.api_image + commentModal.getImage()).into(holder.imageUser);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  static class CommentViewHoder extends RecyclerView.ViewHolder {
    TextView tv_Username, tv_comment, tv_productName,tv_productname_comment,phanloai;
    ImageView imageUser;

        public CommentViewHoder(@NonNull View itemView) {
            super(itemView);
            tv_Username= itemView.findViewById(R.id.tv_username_comment);
//            tv_productName= itemView.findViewById(R.id.tv_username_comment);
            tv_comment= itemView.findViewById(R.id.tv_decription_comment);
            imageUser= itemView.findViewById(R.id.img_user_comment);
            phanloai= itemView.findViewById(R.id.phanloai);
            tv_productname_comment= itemView.findViewById(R.id.tv_productname_comment);
        }
    }
}
