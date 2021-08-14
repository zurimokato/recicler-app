package job.project.com.recyclego.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import job.project.com.recyclego.R;
import job.project.com.recyclego.modelo.Taller;

public class TallerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Taller>tallers;

    public TallerAdapter(Context context, ArrayList<Taller> tallers) {
        this.context = context;
        this.tallers = tallers;
    }

    @Override
    public int getCount() {
        return tallers.size();
    }

    @Override
    public Object getItem(int position) {
        return tallers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.talleres,parent,false);
        }

        Taller t=(Taller) getItem(position);

        TextView nombre=convertView.findViewById(R.id.tallername);
        TextView dir=convertView.findViewById(R.id.dir);
        RatingBar ratingBar=convertView.findViewById(R.id.ratingtall);
        ImageView imageView=convertView.findViewById(R.id.imagetaller);

        nombre.setText(t.getNombreTaller());
        dir.setText("Direccion: "+t.getDireccion());

        if(t.getImage()==null){
            imageView.setImageResource(R.drawable.centroreciclado);
        }else {
            Picasso.get().load(t.getImage()).into(imageView);
        }



        return convertView;



    }
}
