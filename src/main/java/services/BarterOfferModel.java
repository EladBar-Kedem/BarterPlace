package services;

import java.io.Serializable;

//barter offer model represent the barter offer in DB just with strings
public class BarterOfferModel implements Serializable {
    String ownerId;
    String offerId;
    String ownerBarterId;
    String offerBarterId;
    String status;
    String barterOfferId;

    public BarterOfferModel(String barterOfferId, String ownerId, String offerId, String ownerBarterId, String offerBarterId, String status) {
        this.ownerId = ownerId;
        this.offerId = offerId;
        this.ownerBarterId = ownerBarterId;
        this.offerBarterId = offerBarterId;
        this.status = status;
        this.barterOfferId = barterOfferId;
    }

    public String getBarterOfferId() {
        return barterOfferId;
    }

    public void setBarterOfferId(String barterOfferId) {
        this.barterOfferId = barterOfferId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getOwnerBarterId() {
        return ownerBarterId;
    }

    public void setOwnerBarterId(String ownerBarterId) {
        this.ownerBarterId = ownerBarterId;
    }

    public String getOfferBarterId() {
        return offerBarterId;
    }

    public void setOfferBarterId(String offerBarterId) {
        this.offerBarterId = offerBarterId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
