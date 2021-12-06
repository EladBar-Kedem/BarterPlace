package services;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Barter implements Serializable {
    private String id;
    private String title;

    private String area;
    private Bitmap imageFileName;
    private String ownerId;
    private String details;
    private String status;
    private Date publishDate;


    public Barter(String id, String title, String area, String userId, String details, String status) {
        this.id = id;
        this.title = title;
        this.area = area;
        this.details = details;
        this.status = status;
        this.ownerId = userId;
        //this.publishDate =new SimpleDateFormat("dd/MM/yyyy").parse(publishDate);

    }

    public Barter(String id, String title, String area, String userId, String details, Bitmap imageFileName, String status) {
        this.id = id;
        this.title = title;
        this.area = area;
        this.details = details;
        this.ownerId = userId;
        this.status = status;
        this.imageFileName = imageFileName;
        //this.publishDate =new SimpleDateFormat("dd/MM/yyyy").parse(publishDate);

    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Bitmap getBarterPic() {
        return imageFileName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setBarterPic(Bitmap imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getUserId() {
        return ownerId;
    }

    public void setUserId(String userId) {
        this.ownerId = userId;
    }
}
