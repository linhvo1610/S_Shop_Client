package account.fpoly.s_shop_client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import account.fpoly.s_shop_client.API.API;
import account.fpoly.s_shop_client.API.API_Bill;
import account.fpoly.s_shop_client.Modal.Bill;
import account.fpoly.s_shop_client.Modal.BillMore;
import account.fpoly.s_shop_client.Modal.Cart;
import account.fpoly.s_shop_client.Modal.ReceBillMores;
import account.fpoly.s_shop_client.adapter.BillAdapter;
import account.fpoly.s_shop_client.adapter.StatusBillAdapter;
import retrofit2.Call;
import retrofit2.Callback;

public class Xacnhan_Bill extends AppCompatActivity {

    ImageView img_back;
    RecyclerView rcv;
    List<BillMore> list;
    StatusBillAdapter adapter;String iduser;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xacnhan_bill);

        anhxa();
        SharedPreferences preferences = getSharedPreferences("infoUser",MODE_PRIVATE);
        iduser = preferences.getString("iduser", null);
        hienthiHistoty();
//        getListStatus();
//        getnamePro();
    }

    private void anhxa() {
        rcv = findViewById(R.id.rcvHuyBill);
        img_back = findViewById(R.id.img_back);
        title = findViewById(R.id.title);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(getBaseContext());
        rcv.setLayoutManager(manager);
    }
    String idpro;
    private void hienthiHistoty() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.api + "billStatus?id_user=" + iduser + "&status=0", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        BillMore bill = new BillMore();
                        bill.set_id(jsonObject.getString("_id"));
                        bill.setName(jsonObject.getString("name"));
                        bill.setPhone(jsonObject.getString("phone"));
                        bill.setAddress(jsonObject.getString("address"));
                        bill.setTotal(jsonObject.getInt("total"));
                        bill.setStatus(Integer.parseInt(jsonObject.getString("status")));

                        JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                        ArrayList<Cart> cartList = new ArrayList<>();
                        for (int j = 0; j < jsonArrayList.length(); j++) {
                            JSONObject jsonObjectList = jsonArrayList.getJSONObject(j);
                            Cart cart = new Cart();
                            cart.setSize(jsonObjectList.getInt("size"));
                            cart.setQuantity(jsonObjectList.getInt("quantity"));
                            cart.setPrice_product(jsonObjectList.getInt("price_product"));
                            cart.setName_product(jsonObjectList.getString("name_product"));
                            cart.setImage(jsonObjectList.getString("image"));
                            // Populate other fields of the Cart object
                            cartList.add(cart);
                        }
                        bill.setList(cartList);
                        list.add(bill);
                    }
                    adapter = new StatusBillAdapter(getBaseContext(),list);
                    rcv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    private void getListStatus (){
        int status = 0;
        API_Bill.apiBill.getListBillMores(iduser,status).enqueue(new Callback<ReceBillMores>() {
            @Override
            public void onResponse(Call<ReceBillMores> call, retrofit2.Response<ReceBillMores> response) {
                list = response.body().getData();
                adapter = new StatusBillAdapter(getBaseContext(),list);
                rcv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ReceBillMores> call, Throwable t) {

            }
        });
    }
    
}