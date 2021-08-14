package job.project.com.recyclego;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import job.project.com.recyclego.fragments.FragmentHome;
import job.project.com.recyclego.fragments.MapFragment;
import job.project.com.recyclego.fragments.PerfileFragment;

public class PostActivity extends AppCompatActivity {

    private BottomNavigationView bootom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        bootom=findViewById(R.id.bottom_navigation);
        bootom.setOnNavigationItemReselectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmelayout,new FragmentHome()).commit();
    }

    private  BottomNavigationView.OnNavigationItemReselectedListener navListener=new BottomNavigationView.OnNavigationItemReselectedListener() {
        @Override
        public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
            Fragment selected=null;

            switch (menuItem.getItemId()){
                case R.id.home:
                    selected=new FragmentHome();
                    break;
                case R.id.perfil:
                    selected=new PerfileFragment();
                    break;
                case R.id.map:
                    selected=new MapFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragmelayout,selected).commit();
        }
    };
}
