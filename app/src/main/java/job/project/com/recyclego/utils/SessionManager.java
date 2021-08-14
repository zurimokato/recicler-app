package job.project.com.recyclego.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import job.project.com.recyclego.PostView;
import job.project.com.recyclego.LoginActivity;

public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private  Context context;
    private int PRIVATE_MODE=0;

    private static final String PREF_NAME="LOGIN";
    private static final String LOGIN="IS_LOGIN";
    public  static final String NAME="NAME";
    public  static final String EMAIL="EMAIL";
    public static final String ID="ID";


    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences=context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor=sharedPreferences.edit();
    }

    public void createSession(String name, String email, String id){
        editor.putBoolean(LOGIN,true);
        editor.putString(NAME,name);
        editor.putString(EMAIL,email);
        editor.putString(ID,id);
        editor.apply();
    }

    public boolean isLogin(){
        return sharedPreferences.getBoolean(LOGIN,false);
    }

    public void checkLogin(){
        if(!this.isLogin()){
            Intent intent=new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            ((PostView)context).finish();
        }
    }

    public HashMap<String,String> getUserDetail(){
        HashMap<String,String> user=new HashMap<>();
        user.put(NAME,sharedPreferences.getString(NAME,"noData"));
        user.put(EMAIL,sharedPreferences.getString(EMAIL,"noDAta"));
        user.put(ID,sharedPreferences.getString(ID,"noDota"));
        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent intent=new Intent(context,LoginActivity.class);
        context.startActivity(intent);
        ((PostView)context).finish();
    }


}
