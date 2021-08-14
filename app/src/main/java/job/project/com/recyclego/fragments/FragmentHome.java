package job.project.com.recyclego.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import job.project.com.recyclego.PostView;
import job.project.com.recyclego.R;
import job.project.com.recyclego.adapters.PostAdapter;
import job.project.com.recyclego.modelo.Post;
import job.project.com.recyclego.utils.SessionManager;

public class FragmentHome extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {

    private ListView list;
    private EditText textoTitle;
    private  EditText textoBody;
    private Button btnPublicar;
    private PostAdapter apater;
    private ArrayList<Post> posts;
    private SessionManager manager;
    private String getId;
    private final String URL_POST="http://172.16.10.243/recycle/pots.php";

    ProgressDialog progres;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home,container,false);
        list=view.findViewById(R.id.listaPost);
        textoTitle=view.findViewById(R.id.title);
        textoBody=view.findViewById(R.id.body);
        btnPublicar=view.findViewById(R.id.btnpu);
        manager=new SessionManager(getContext());
        manager.checkLogin();
        posts=new ArrayList<>();
        requestQueue= Volley.newRequestQueue(getContext());
        cargarWebservice();

        HashMap<String,String> user=manager.getUserDetail();

        getId=user.get(SessionManager.ID);
        btnPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicar(getId);
            }
        });

        return view;
    }

    private void publicar(final String getId) {
        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        final String titulo=textoTitle.getText().toString().trim();
        final String body=textoBody.getText().toString().trim();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("tagconvertstr", "["+response+"]");
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String succes=jsonObject.getString("success");
                            if(succes.equals("1")){
                                Toast.makeText(getContext(), R.string.success, Toast.LENGTH_SHORT).show();
                                getActivity().recreate();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Json error"+e.toString(), Toast.LENGTH_LONG).show();
                            Log.i("error", "["+e.toString()+"]");
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getContext(), "Volley Error"+error.toString(), Toast.LENGTH_LONG).show();
                        Log.i("tagconvertstr", "["+error.toString()+"]");

                    }
                }){
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params=new HashMap<>();

                params.put("titulo",titulo);
                params.put("post",body);
                params.put("user",getId);

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void cargarWebservice() {
        progres=new ProgressDialog(getContext());
        progres.setMessage("Consultando....!");
        progres.show();


        final String URL_POSTS="http://172.16.10.243/recycle/data.php";
        jsonObjectRequest= new JsonObjectRequest(Request.Method.GET,URL_POSTS,null,this,this);
        requestQueue.add(jsonObjectRequest);


    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
        progres.hide();
        Log.d("Erro: ",error.getMessage());

    }

    @Override
    public void onResponse(JSONObject response) {
        Post post= null;
        JSONArray jsonArray=response.optJSONArray("posts");
        Log.i("Response",response.toString());
        try{
            for(int i=0;i<jsonArray.length();i++){
                post=new Post();
                JSONObject jsonObject=null;
                jsonObject=jsonArray.getJSONObject(i);
                post.setId(jsonObject.optLong("idposts"));
                post.setTitulo(jsonObject.optString("titulo"));
                post.setBody(jsonObject.optString("post"));
                post.setImagen(jsonObject.optString("imgen"));
                post.setFechapub(jsonObject.optString(" fechapub"));
                post.setIduser(jsonObject.optLong("users_id_user"));
                post.setIdtaller(jsonObject.optLong("id_taller"));
                posts.add(post);
            }
            progres.hide();
            apater=new PostAdapter(getContext(),posts);
            list.setAdapter(apater);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Post p=(Post) apater.getItem(position);
                    Intent intent=new Intent(getContext(), PostView.class);
                    intent.putExtra("post",p);
                    getContext().startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            progres.hide();
            Toast.makeText(getContext(), "no sé conecto"+e.toString(), Toast.LENGTH_SHORT).show();
            Log.i("No conection",e.getMessage());
        }

    }
}
