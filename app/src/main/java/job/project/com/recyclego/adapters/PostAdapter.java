package job.project.com.recyclego.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import job.project.com.recyclego.R;
import job.project.com.recyclego.modelo.Post;

public class PostAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Post>posts;


    public PostAdapter(Context context, ArrayList<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.post,parent,false);
        }

        Post p=(Post)getItem(position);

        TextView nombre=convertView.findViewById(R.id.postTittle);
        TextView fecha=convertView.findViewById(R.id.fecha);
        ImageView imageView=convertView.findViewById(R.id.imagepost);

        nombre.setText(p.getTitulo());
        fecha.setText("Fecha publicacion: "+p.getFechapub());

        if(p.getImagen()==null){
            imageView.setImageResource(R.drawable.centroreciclado);
        }else {
            Picasso.get().load(p.getImagen()).into(imageView);
        }



        return convertView;
    }
}
