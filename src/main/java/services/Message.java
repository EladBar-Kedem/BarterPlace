package services;

public class Message {
    String id;
    String barterOfferId;
    String answer;
    String status;
    String userAnswerId;
    String userWaitingForAnswerId;


    public Message(String id, String barterOfferId, String answer, String status, String userWaitingForAnswerId, String userAnswerId) {
        this.id = id;
        this.barterOfferId = barterOfferId;
        this.answer = answer;
        this.status = status;
        this.userAnswerId = userAnswerId;
        this.userWaitingForAnswerId = userWaitingForAnswerId;
    }


    public String getUserAnswerId() {
        return userAnswerId;
    }

    public void setUserAnswerId(String userAnswerId) {
        this.userAnswerId = userAnswerId;
    }

    public String getUserWaitingForAnswerId() {
        return userWaitingForAnswerId;
    }

    public void setUserWaitingForAnswerId(String userWaitingForAnswerId) {
        this.userWaitingForAnswerId = userWaitingForAnswerId;
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

    public String getBarterOfferId() {
        return barterOfferId;
    }

    public void setBarterOfferId(String barterOfferId) {
        this.barterOfferId = barterOfferId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
