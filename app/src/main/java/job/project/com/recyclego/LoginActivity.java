package job.project.com.recyclego;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import job.project.com.recyclego.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button login;
    private TextView link_registre;
    private ProgressBar loading;
    private  String URL_LOGIN="http://172.16.10.243/recycle/login.php";
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.emailLog);
        password=findViewById(R.id.passLo);
        login=findViewById(R.id.btn_login);
        link_registre=findViewById(R.id.registre);
        loading=findViewById(R.id.progress);
        sessionManager=new SessionManager(this);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String emailText=email.getText().toString().trim();
                String passText=password.getText().toString();
                System.out.println(emailText);
                if(!emailText.isEmpty() && !passText.isEmpty()){
                    login(emailText,passText);
                }else{
                    email.setError(getString(R.string.emailerror));
                    password.setError(getString(R.string.passerror));
                }
            }
        });

        link_registre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void login(final String email, final String password){

        login.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);


        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("ptppp",response);
                            JSONObject jsonObject=new JSONObject(response);
                            String success=jsonObject.getString("success");
                            JSONArray jsonArray=jsonObject.getJSONArray("login");
                            if(success.equals("1")){
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsObject=jsonArray.getJSONObject(i);
                                    String name=jsObject.getString("name");
                                    String em=jsObject.getString("email");
                                    String id=jsObject.getString("id");
                                    String bio=jsObject.getString("bio");
                                    sessionManager.createSession(name,em,id);
                                    Intent intent= new Intent(getApplicationContext(),PostActivity.class);
                                    intent.putExtra("name",name);
                                    intent.putExtra("email",em);
                                    startActivity(intent);
                                    loading.setVisibility(View.GONE);
                                    login.setVisibility(View.VISIBLE);
                                    finish();

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            login.setVisibility(View.VISIBLE);
                            Log.i("ptppp",response);
                            Toast.makeText(LoginActivity.this, "Error"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        login.setVisibility(View.VISIBLE);
                        Log.i("error",error.toString());
                        Toast.makeText(LoginActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("email",email);
                params.put("password",password);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            hideSistemUI();
        }
    }

    private void hideSistemUI(){

        View decorView=getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE|View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                        |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                        |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        |View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    private void ShowsystemUI(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
