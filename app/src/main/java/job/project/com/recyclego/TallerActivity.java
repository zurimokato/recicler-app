package job.project.com.recyclego;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import job.project.com.recyclego.adapters.TallerAdapter;
import job.project.com.recyclego.modelo.Taller;

public class TallerActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {
    private final String URL_TALLERS="http://172.16.10.243/recycle/talleres.php";

    ProgressDialog progres;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    private ArrayList<Taller>tallers;
    private ListView listatall;
    private TallerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taller);

        tallers=new ArrayList<>();
        requestQueue= Volley.newRequestQueue(getBaseContext());
        listatall=findViewById(R.id.listtaller);
        cargarWebservice();
    }

    private void cargarWebservice() {


        jsonObjectRequest= new JsonObjectRequest(Request.Method.GET,URL_TALLERS,null,this,this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
        progres.hide();
        Log.d("Erro: ",error.getMessage());

    }

    @Override
    public void onResponse(JSONObject response) {
        Taller taller=null;
        JSONArray jsonArray=response.optJSONArray("posts");
        Log.i("Response",response.toString());
        try{
            for(int i=0;i<jsonArray.length();i++){
                taller=new Taller();
                JSONObject jsonObject=null;
                jsonObject=jsonArray.getJSONObject(i);
                taller.setId(jsonObject.optLong("idtaller"));
                taller.setNombreTaller(jsonObject.optString("nombretaller"));
                taller.setDireccion(jsonObject.optString("direccion"));
                taller.setRating(jsonObject.optDouble("rating"));
                taller.setLatd(jsonObject.optDouble("latd"));
                taller.setLongi(jsonObject.optDouble("longi"));
                tallers.add(taller);
            }



            adapter=new TallerAdapter(getApplicationContext(),tallers);
            listatall.setAdapter(adapter);

        }catch (JSONException e) {
            e.printStackTrace();
            progres.hide();
            Toast.makeText(getApplicationContext(), "no sé conecto"+e.toString(), Toast.LENGTH_SHORT).show();
            Log.i("No conection",e.getMessage());
        }

    }
}
