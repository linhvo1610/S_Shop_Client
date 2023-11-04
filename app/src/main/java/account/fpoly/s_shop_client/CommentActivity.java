package account.fpoly.s_shop_client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.List;

import account.fpoly.s_shop_client.Modal.CommentModal;

public class CommentActivity extends AppCompatActivity {
    List<CommentModal> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

    }
}