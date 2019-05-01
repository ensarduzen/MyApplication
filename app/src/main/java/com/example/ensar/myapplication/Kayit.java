package com.example.ensar.myapplication;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Kayit extends AppCompatActivity {

    private static final String TAG = "Kayit";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private EditText et_Ad,et_Soyad,et_Email,et_Sifre,et_Tsifre,et_Cinsiyet;
    private TextView dogumTarihi;
    private Button uyeOl;
    private static String URL_REGIST = "http://192.168.2.6/loginapp/register.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);

        /////////////////////////////////////////////
        et_Ad = findViewById(R.id.et_Ad);
        et_Soyad = findViewById(R.id.et_Soyad);
        et_Email = findViewById(R.id.et_Email);
        et_Sifre = findViewById(R.id.et_Sifre);
        et_Tsifre = findViewById(R.id.et_Tsifre);
        et_Cinsiyet = findViewById(R.id.et_Cinsiyet);
        dogumTarihi = findViewById(R.id.dogumTarihi);
        uyeOl = findViewById(R.id.uyeOl);

        uyeOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Regist();
            }
        });
        /////////////////////////////////////////////

        mDisplayDate = (TextView) findViewById(R.id.dogumTarihi);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Kayit.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
            mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month = month + 1 ;
                    Log.d(TAG, "onDateSet : dd/mm/yyyy: " + year + "/" + month + "/" + dayOfMonth);
                    String date = year + "/" + month + "/" + dayOfMonth;
                    mDisplayDate.setText(date);
                }
            };
    }

    private void Regist(){
        uyeOl.setVisibility(View.GONE);

        final String et_Ad = this.et_Ad.getText().toString().trim();
        final String et_Soyad = this.et_Soyad.getText().toString().trim();
        final String et_Email = this.et_Email.getText().toString().trim();
        final String et_Sifre = this.et_Sifre.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");

                        if (success.equals("1")){
                            Toast.makeText(Kayit.this, "Register Success!",Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Kayit.this, "Register Error! " + e.toString(),Toast.LENGTH_SHORT).show();
                        uyeOl.setVisibility(View.VISIBLE);
                    }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Kayit.this, "Register Error! " + error.toString(),Toast.LENGTH_SHORT).show();
                        uyeOl.setVisibility(View.VISIBLE);

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ad", et_Ad);
                params.put("soyad", et_Soyad);
                params.put("email", et_Email);
                params.put("sifre", et_Sifre);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
