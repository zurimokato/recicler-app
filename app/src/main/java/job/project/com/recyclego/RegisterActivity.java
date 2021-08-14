package job.project.com.recyclego;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText nombre, correo, pass,c_pass;
    private Button registra;
    private ProgressBar loading;
    private String URL_REGIST="http://172.16.10.243/recycle/registrer.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loading=findViewById(R.id.loading);
        nombre=findViewById(R.id.name);
        correo=findViewById(R.id.email);
        pass=findViewById(R.id.pass);
        c_pass=findViewById(R.id.c_password);
        registra=findViewById(R.id.btn_registra);


        registra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUS();
            }
        });

    }

    private void registrarUS(){
        loading.setVisibility(View.VISIBLE);
        registra.setVisibility(View.GONE);

        final String name=nombre.getText().toString().trim();
        final String email=correo.getText().toString().trim();
        final String password=pass.getText().toString().trim();


        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject=new JSONObject(response);

                            System.out.println("tagconvertstr,["+response+"]");
                            String succes=jsonObject.getString("success");
                            if(succes.equals("1")){
                                Toast.makeText(RegisterActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                                Intent pasar=new Intent(getBaseContext(),PostView.class);
                                startActivity(pasar);
                                finish();
                            }else{
                                Toast.makeText(RegisterActivity.this, "Algo sucedio", Toast.LENGTH_SHORT).show();
                                Intent pasar=new Intent(getBaseContext(),FullscreenActivity.class);
                                startActivity(pasar);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "php error Error"+e.toString(), Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            registra.setVisibility(View.VISIBLE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(RegisterActivity.this, "Volley Error"+error.toString(), Toast.LENGTH_LONG).show();
                        loading.setVisibility(View.GONE);
                        registra.setVisibility(View.VISIBLE);

                    }
                }){
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params=new HashMap<>();

                params.put("name",name);
                params.put("email",email);
                params.put("password",password);

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getBaseContext());
        requestQueue.add(stringRequest);

    }
}
