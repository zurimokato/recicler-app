package job.project.com.recyclego.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import job.project.com.recyclego.PostView;
import job.project.com.recyclego.R;
import job.project.com.recyclego.utils.SessionManager;

public class PerfileFragment extends Fragment {

    private Button butLogaut, btphoto;
    private SessionManager sessionManager;
    private String getId;
    private EditText editNombre, editEmail,editBio;
    private static String URL_READ="http://172.16.10.243/recycle/read_detail.php";
    private static String URL_EDIT="http://172.16.10.243/recycle/edit_detail.php";
    private static String URL_IMAGE="http://172.16.10.243/recycle/image.php";
    private static final String TAG=PostView.class.getSimpleName();
    private Menu action;
    private Bitmap bitmap;
    private CircleImageView porfile_image;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_perfil,container,false);

        sessionManager=new SessionManager(getContext());
        sessionManager.checkLogin();

        editNombre=view.findViewById(R.id.nameInput);
        editEmail=view.findViewById(R.id.emailInput);
        butLogaut=view.findViewById(R.id.logout);
        btphoto=view.findViewById(R.id.bton_photo);
        porfile_image=view.findViewById(R.id.imagen_perfil);
        HashMap<String,String> user=sessionManager.getUserDetail();

        getId=user.get(SessionManager.ID);

        editNombre.setFocusableInTouchMode(false);
        editEmail.setFocusableInTouchMode(false);

        editNombre.setFocusable(false);
        editEmail.setFocusable(false);

        setHasOptionsMenu(true);
        butLogaut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
            }
        });
        btphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });
        return view;
    }


    public void getDetail(){
        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG,response);
                        try {

                            JSONObject jsonObject=new JSONObject(response);
                            String success=jsonObject.getString("success");
                            JSONArray jsonArray=jsonObject.getJSONArray("read");

                            if(success.equals("1")){
                                for(int i=0; i<jsonArray.length();i++) {
                                    JSONObject object=jsonArray.getJSONObject(i);
                                    String strname=object.getString("name");
                                    String stremail=object.getString("email");
                                    String strbio=object.getString("bio");
                                    editNombre.setText(strname);
                                    editEmail.setText(stremail);

                                }
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Log.i(TAG,response);
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Errror Reading Detail", Toast.LENGTH_SHORT).show();
                            Log.i(TAG,e.getMessage());

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.i(TAG,error.getMessage());
                        Toast.makeText(getContext(), "Error sharing Data"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("id",getId);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onResume(){
        super.onResume();
        getDetail();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the fragment menu items.
        inflater.inflate(R.menu.menu,menu);
        action=menu;
        action.findItem(R.id.menu_guardar).setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_edit:
                editNombre.setFocusableInTouchMode(true);
                editEmail.setFocusableInTouchMode(true);

                InputMethodManager im= (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.showSoftInput(editNombre,InputMethodManager.SHOW_IMPLICIT);
                action.findItem(R.id.menu_edit).setVisible(false);
                action.findItem(R.id.menu_guardar).setVisible(true);
                return true;
            case R.id.menu_guardar:
                saveEditDetails();
                action.findItem(R.id.menu_edit).setVisible(true);
                action.findItem(R.id.menu_guardar).setVisible(false);
                editNombre.setFocusableInTouchMode(false);
                editEmail.setFocusableInTouchMode(false);

                editNombre.setFocusable(false);
                editEmail.setFocusable(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void saveEditDetails() {

        final String name=editNombre.getText().toString();
        final String email=editEmail.getText().toString();
        final String id=getId;
        final ProgressDialog progressDialog =new ProgressDialog(getContext());
        progressDialog.setMessage("Actualizando...");
        progressDialog.show();

        Log.i("Nombre",name);
        Log.i("Email",email);
        Log.i("ID",id);

        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL_EDIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG,response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String succes=jsonObject.getString("success");
                            if(succes.equals("1")){
                                Toast.makeText(getContext(), "Finalizo correctamente", Toast.LENGTH_SHORT).show();
                                sessionManager.createSession(name,email,id);

                            }
                        } catch (JSONException e) {
                            Log.i(TAG,e.getMessage());
                            Log.i(TAG,response);
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG,error.getMessage());
                        Log.i(TAG,"Vollley Error");
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> args=new HashMap<>();
                args.put("name",name);
                args.put("email",email);
                args.put("id",id);

                return args;
            }
        };

        RequestQueue requestQueue=Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    private void chooseFile(){
        Intent inten = new Intent();
        inten.setType("image/*");
        inten.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(inten,"Elige una foto"),1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1 &&  data!=null && data.getData()!=null){
            Uri filepath=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(),filepath);
                porfile_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            uploadPicture(getId,getStringImage(bitmap));

        }else{
            Log.i("pop","pop");
        }
    }

    private String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArray= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArray);
        byte[] imageByteArray=byteArray.toByteArray();
        return Base64.encodeToString(imageByteArray,Base64.DEFAULT);
    }

    private void uploadPicture(final String id, final String photo) {
        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_IMAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG,response);
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    String succes=jsonObject.getString("success");
                    if(succes.equals("1")){
                        Toast.makeText(getContext(), "Succes", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Intenta de nuevo"+e.toString(), Toast.LENGTH_SHORT).show();
                    Log.i("JSONEXCEPTION",response);
                    Log.i("JSONEXCEPTION",e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("VOELLYERROR",error.getMessage());
                Toast.makeText(getContext(), "Intenta de nuevo"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("id",id);
                params.put("photo",photo);
                return params;
            }
        };

        RequestQueue queue=Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }
}
