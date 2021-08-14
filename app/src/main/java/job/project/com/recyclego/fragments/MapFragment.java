package job.project.com.recyclego.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import job.project.com.recyclego.R;
import job.project.com.recyclego.TallerActivity;
import job.project.com.recyclego.modelo.Taller;

public class MapFragment extends Fragment implements OnMapReadyCallback,Response.Listener<JSONObject>,Response.ErrorListener{

    private SupportMapFragment fragmentMap;
    private GoogleMap mMap;
    private ArrayList<Taller>tallers;
    private final String URL_TALLERS="http://172.16.10.243/recycle/talleres.php";

    ProgressDialog progres;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.framgent_map,container,false);
        FragmentManager fragmentManager=getChildFragmentManager();
        fragmentMap=(SupportMapFragment)fragmentManager.findFragmentById(R.id.map);
        tallers=new ArrayList<>();
        requestQueue= Volley.newRequestQueue(getContext());
        cargarWebservice();
        if(fragmentMap!=null){
            setHasOptionsMenu(true);
            fragmentMap=SupportMapFragment.newInstance();
            fragmentMap.getMapAsync(this);
            fragmentMap.setRetainInstance(true);
            fragmentManager.beginTransaction().add(R.id.map,fragmentMap).commit();
        }
        return v;
    }

    private void cargarWebservice() {

        progres=new ProgressDialog(getContext());
        progres.setMessage("Consultando....!");
        progres.show();

        jsonObjectRequest= new JsonObjectRequest(Request.Method.GET,URL_TALLERS,null,this,this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap =googleMap;

        for (Taller t:tallers){
            LatLng punto = new LatLng(t.getLatd(), t.getLongi());
            mMap.addMarker(new MarkerOptions().position(punto).title(t.getNombreTaller()));


        }

        mMap.setMinZoomPreference(10);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(11.234,-74.16893)));

        // Add a marker in Sydney and move the camera



    }

    private void cargarMarket() {

        for (Taller t:tallers){
            LatLng punto = new LatLng(t.getLatd(), t.getLongi());
            mMap.addMarker(new MarkerOptions().position(punto).title(t.getNombreTaller()));


        }

        mMap.setMinZoomPreference(10);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(11.234,-74.16893)));

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
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

            progres.hide();
            cargarMarket();
        }catch (JSONException e) {
            e.printStackTrace();
            progres.hide();
            Toast.makeText(getContext(), "no sé conecto"+e.toString(), Toast.LENGTH_SHORT).show();
            Log.i("No conection",e.getMessage());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the fragment menu items.
        inflater.inflate(R.menu.mpa_menu,menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.taller:
                Intent intent=new Intent(getContext(), TallerActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
