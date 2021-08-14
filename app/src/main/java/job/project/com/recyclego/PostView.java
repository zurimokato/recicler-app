package job.project.com.recyclego;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import job.project.com.recyclego.modelo.Post;

public class PostView extends AppCompatActivity {

    public Button btn;
    private TextView textNombre,textBody,textNombreTaller;
    private ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postview);
        textNombre=findViewById(R.id.postTittle);
        textBody=findViewById(R.id.body);
        btn=findViewById(R.id.contctanos);
        image=findViewById(R.id.imagepost);
        Intent intent=getIntent();
        Post o= (Post) intent.getExtras().get("post");
        if (o!=null){
            textNombre.setText(o.getTitulo());
            textBody.setText(o.getBody());
            Picasso.get().load(o.getImagen()).into(image);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"Contactanos",Toast.LENGTH_SHORT).show();
            }
        });

    }


}
