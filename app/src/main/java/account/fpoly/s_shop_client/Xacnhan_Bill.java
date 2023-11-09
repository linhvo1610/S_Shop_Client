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
import account.fpoly.s_shop_client.Modal.Bill;
import account.fpoly.s_shop_client.adapter.BillAdapter;

public class Xacnhan_Bill extends AppCompatActivity {

    ImageView img_back;
    RecyclerView rcv;
    List<Bill> list;
    BillAdapter adapter;String iduser;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xacnhan_bill);

        anhxa();
        SharedPreferences preferences = getSharedPreferences("infoUser",MODE_PRIVATE);
        iduser = preferences.getString("iduser", null);
        hienthiHistoty();
//        getnamePro();
    }

    private void anhxa() {
        rcv = findViewById(R.id.rcv);
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.api + "billCho?id_user=" + iduser + "&status=Chờ xác nhận", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Bill bill = new Bill();
                        bill.setTotalPrice(jsonObject.getDouble("totalPrice"));
                        bill.setTotalQuantity(jsonObject.getInt("totalQuantity"));
                        bill.setStatus(jsonObject.getString("status"));
                        bill.setSize(jsonObject.getInt("size"));

                        JSONArray jsonArrayPro = jsonObject.getJSONArray("product");
                        for (int j = 0; j < jsonArrayPro.length(); j++) {
                            JSONObject jsonObjectPro = jsonArrayPro.getJSONObject(j);
                            try {
                                JSONObject idProductObject  = jsonObjectPro.getJSONObject("id_product");
                                bill.setName(idProductObject .getString("name"));
                                bill.setImage(idProductObject .getString("image"));

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        list.add(bill);
                    }
                    adapter = new BillAdapter(getBaseContext(),list);
                    rcv.setAdapter(adapter);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "Call API Fail", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void getnamePro() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.api + "product?_id=" + idpro,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        Toast.makeText(Xacnhan_Bill.this, "name: "+ name, Toast.LENGTH_SHORT).show();
                        title.setText(name);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Xacnhan_Bill.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}