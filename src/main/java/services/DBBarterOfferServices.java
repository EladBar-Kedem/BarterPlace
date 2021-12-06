package services;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBBarterOfferServices {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private CollectionReference bartersRef;

    public DBBarterOfferServices() {
        mAuth = FirebaseAuth.getInstance();                              //auth reference
        db = FirebaseFirestore.getInstance();                           //firestore reference
        bartersRef = db.collection("barters_offer");        //collection reference
        //get current user:
        currentUser = mAuth.getCurrentUser();
    }

    //get all the barters offers by user id that received
    public void getBarterOffersByUserId(String userId, IBarterOffersByUserId cb) {
        bartersRef.whereEqualTo("user_offer_id", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {   //get the relevant barters
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<BarterOfferModel> bartersOffersModels = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> barterOfferAttributes = document.getData();     //get each barter offer in loop
                        BarterOfferModel barterOfferModel = new BarterOfferModel(document.getId(), barterOfferAttributes.get("user_owner_id").toString(),
                                barterOfferAttributes.get("user_offer_id").toString(), barterOfferAttributes.get("barter_id").toString(),
                                barterOfferAttributes.get("barter_received_id").toString(), barterOfferAttributes.get("status").toString());
                        bartersOffersModels.add(barterOfferModel);
                    }
                    cb.sendBarterOffers(bartersOffersModels);       //return all barters offers in DB model (all String)
                } else {
                    cb.sendBarterOffers(null);              //return null for unsuccessfully request
                }
            }
        });
    }

    //get the barter offers that the current user has received
    public void getBarterOffersDoneByUserId(String userId, IBarterOffersByUserId cb) {
        bartersRef.whereEqualTo("user_owner_id", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {   //get the relevant barters
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<BarterOfferModel> bartersOffersModels = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> barterOfferAttributes = document.getData();     //get each barter offer in loop
                        BarterOfferModel barterOfferModel = new BarterOfferModel(document.getId(), barterOfferAttributes.get("user_owner_id").toString(),
                                barterOfferAttributes.get("user_offer_id").toString(), barterOfferAttributes.get("barter_id").toString(),
                                barterOfferAttributes.get("barter_received_id").toString(), barterOfferAttributes.get("status").toString());
                        bartersOffersModels.add(barterOfferModel);
                    }
                    cb.sendBarterOffers(bartersOffersModels);       //return all barters offers in DB model (all String)
                } else {
                    cb.sendBarterOffers(null);              //return null for unsuccessfully request
                }
            }
        });
    }

    //get barter offer by barter offer id that received
    public void getBarterOfferById(String id, IBarterOffersById cb) {
        DocumentReference barterRef = bartersRef.document(id);
        barterRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot result = task.getResult();
                    if (result.exists()) {
                        Map<String, Object> barterAttributes = result.getData();
                        BarterOfferModel bom = new BarterOfferModel(id, barterAttributes.get("user_owner_id").toString(),
                                barterAttributes.get("user_offer_id").toString(), barterAttributes.get("barter_id").toString(),
                                barterAttributes.get("barter_received_id").toString(), barterAttributes.get("status").toString());
                        cb.sendBarterOfferModelById(bom);               //return barter offers in DB model
                    } else {
                        cb.sendBarterOfferModelById(null);         // return null for no exist result
                    }
                } else {
                    cb.sendBarterOfferModelById(null);          //return null for unsuccessfully request
                }
            }
        });
    }

    //get all barter offers that received by another user to the user with the received id
    public void getBarterReceivedByUserId(String userId, IBarterOffersByUserId cb) {
        bartersRef.whereEqualTo("user_owner_id", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<BarterOfferModel> bartersOffersModels = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> barterOfferAttributes = document.getData();         //get each barter offer in loop
                        BarterOfferModel barterOfferModel = new BarterOfferModel(document.getId(), barterOfferAttributes.get("user_owner_id").toString(),
                                barterOfferAttributes.get("user_offer_id").toString(), barterOfferAttributes.get("barter_id").toString(),
                                barterOfferAttributes.get("barter_received_id").toString(), barterOfferAttributes.get("status").toString());
                        bartersOffersModels.add(barterOfferModel);
                    }
                    cb.sendBarterOffers(bartersOffersModels);           //return all barters offers in DB model
                } else {
                    cb.sendBarterOffers(null);          //return null for unsuccessfully request
                }
            }
        });
    }

    //create barter offer in DB with waiting_for_answer status
    public void addBarterOfferToDB(String userOfferId, String barterID, String barterReceivedId, String userReceivedId) {
        CollectionReference bartersDoc = db.collection("barters_offer");
        Map<String, Object> data1 = new HashMap<>();
        data1.put("user_owner_id", userOfferId);
        data1.put("barter_id", barterID);
        data1.put("barter_received_id", barterReceivedId);
        data1.put("user_offer_id", userReceivedId);
        data1.put("status", "waiting_for_answer");
        bartersDoc.document().set(data1);//set data
    }

    //edit barter status in DB by received status (rejected/accepted)
    public void EditBarterOfferStatusToDB(String barterOfferId, String userOfferId, String barterID, String barterReceivedId, String userReceivedId, String status) {
        CollectionReference bartersDoc = db.collection("barters_offer");
        Map<String, Object> data1 = new HashMap<>();
        data1.put("user_owner_id", userReceivedId);
        data1.put("barter_id", barterID);
        data1.put("barter_received_id", barterReceivedId);
        data1.put("user_offer_id", userOfferId);
        data1.put("status", status);
        bartersDoc.document(barterOfferId).set(data1);//set data
    }


}
