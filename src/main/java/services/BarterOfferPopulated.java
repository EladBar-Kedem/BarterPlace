package services;

import java.util.Date;

public class BarterOfferPopulated {

    private Barter ownerBarter;
    private Barter barterReceived;
    private String status;
    private Date publishDate;

    public BarterOfferPopulated(Barter ownerBarter, Barter barterReceived) {

        this.ownerBarter = ownerBarter;
        this.barterReceived = barterReceived;
    }
    public BarterOfferPopulated(Barter ownerBarter, Barter barterReceived, String status) {

        this.ownerBarter = ownerBarter;
        this.barterReceived = barterReceived;
        this.status = status;
    }



    public Barter getOwnerBarter() {
        return ownerBarter;
    }

    public void setOwnerBarter(Barter ownerBarter) {
        this.ownerBarter = ownerBarter;
    }

    public Barter getBarterReceived() {
        return barterReceived;
    }

    public void setBarterReceived(Barter barterReceived) {
        this.barterReceived = barterReceived;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }
}
