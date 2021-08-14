package job.project.com.recyclego.modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable{
    private Long id;
    private String titulo;
    private String body;
    private String imagen;
    private String fechapub;
    private Long iduser;
    private Long idtaller;

    public Post(Long id, String titulo, String body, String imagen, String fechapub, Long iduser, Long idtaller) {
        this.id = id;
        this.titulo = titulo;
        this.body = body;
        this.imagen = imagen;
        this.fechapub = fechapub;
        this.iduser = iduser;
        this.idtaller = idtaller;
    }

    public Post() {
    }

    protected Post(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        titulo = in.readString();
        body = in.readString();
        imagen = in.readString();
        fechapub = in.readString();
        if (in.readByte() == 0) {
            iduser = null;
        } else {
            iduser = in.readLong();
        }
        if (in.readByte() == 0) {
            idtaller = null;
        } else {
            idtaller = in.readLong();
        }
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getFechapub() {
        return fechapub;
    }

    public void setFechapub(String fechapub) {
        this.fechapub = fechapub;
    }

    public Long getIduser() {
        return iduser;
    }

    public void setIduser(Long iduser) {
        this.iduser = iduser;
    }

    public Long getIdtaller() {
        return idtaller;
    }

    public void setIdtaller(Long idtaller) {
        this.idtaller = idtaller;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(titulo);
        dest.writeString(body);
        dest.writeString(imagen);
        dest.writeString(fechapub);
        if (iduser == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(iduser);
        }
        if (idtaller == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(idtaller);
        }
    }
}
