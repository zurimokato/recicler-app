package job.project.com.recyclego.modelo;

import android.os.Parcel;
import android.os.Parcelable;


public class Taller implements Parcelable{
    private Long id;
    private String nombreTaller;
    private String direccion;
    private double rating;
    private double latd;
    private double longi;
    private String image;

    public Taller() {
    }

    public Taller(Long id, String nombreTaller, String direccion, double rating, double latd, double longi, String image) {
        this.id = id;
        this.nombreTaller = nombreTaller;
        this.direccion = direccion;
        this.rating = rating;
        this.latd = latd;
        this.longi = longi;
        this.image = image;
    }

    protected Taller(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        nombreTaller = in.readString();
        direccion = in.readString();
        rating = in.readDouble();
        latd = in.readDouble();
        longi = in.readDouble();
        image = in.readString();
    }

    public static final Creator<Taller> CREATOR = new Creator<Taller>() {
        @Override
        public Taller createFromParcel(Parcel in) {
            return new Taller(in);
        }

        @Override
        public Taller[] newArray(int size) {
            return new Taller[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreTaller() {
        return nombreTaller;
    }

    public void setNombreTaller(String nombreTaller) {
        this.nombreTaller = nombreTaller;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getLatd() {
        return latd;
    }

    public void setLatd(double latd) {
        this.latd = latd;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
        dest.writeString(nombreTaller);
        dest.writeString(direccion);
        dest.writeDouble(rating);
        dest.writeDouble(latd);
        dest.writeDouble(longi);
        dest.writeString(image);
    }
}
