package services;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBMessagesService {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private CollectionReference bartersRef;


    public DBMessagesService() {
        mAuth = FirebaseAuth.getInstance();                       //auth reference
        db = FirebaseFirestore.getInstance();                    //firestore reference
        bartersRef = db.collection("messages");       //collection reference
        //get current user:
        currentUser = mAuth.getCurrentUser();
    }

    //get all current user messages
    public void getUserMessages(IMessagesCallBack cb) {
        bartersRef.whereEqualTo("user_waiting_for_answer_id", currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Message> messages = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> barterAttributes = document.getData();
                        Message msg = new Message(document.getId(), barterAttributes.get("barter_offer_id").toString(),
                                barterAttributes.get("answer").toString(), barterAttributes.get("status").toString(),
                                barterAttributes.get("user_waiting_for_answer_id").toString(), barterAttributes.get("user_answer_id").toString()); //create message
                        messages.add(msg);          //store message
                    }
                    cb.sendAllMessages(messages);           //return all messages
                } else {
                    cb.sendAllMessages(null);               //return null for unsuccessfully request
                }
            }
        });
    }

    public void getMoreUserMessages(IMessagesCallBack cb) {
        bartersRef.whereEqualTo("user_answer_id", currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Message> messages = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> barterAttributes = document.getData();
                        Message msg = new Message(document.getId(), barterAttributes.get("barter_offer_id").toString(),
                                barterAttributes.get("answer").toString(), barterAttributes.get("status").toString(),
                                barterAttributes.get("user_waiting_for_answer_id").toString(), barterAttributes.get("user_answer_id").toString()); //create message
                        messages.add(msg);          //store message
                    }
                    cb.sendAllMessages(messages);           //return all messages
                } else {
                    cb.sendAllMessages(null);               //return null for unsuccessfully request
                }
            }
        });
    }

    //create message in DB with created status
    public void addMessageToDB(String barterOfferId, String answer, String userWaitingForAnswerId, String userAnswerId) {
        CollectionReference messagesDoc = db.collection("messages");
        Map<String, Object> data1 = new HashMap<>();
        data1.put("barter_offer_id", barterOfferId);
        data1.put("user_waiting_for_answer_id", userWaitingForAnswerId);
        data1.put("user_answer_id", userAnswerId);
        data1.put("answer", answer);
        data1.put("status", "created");
        messagesDoc.document().set(data1);//set data
    }

    //change status of message that received to readed
    public void changeMessageStatus(Message message) {
        CollectionReference messagesDoc = db.collection("messages");
        Map<String, Object> data1 = new HashMap<>();
        data1.put("barter_offer_id", message.getBarterOfferId());
        data1.put("user_waiting_for_answer_id", message.getUserWaitingForAnswerId());
        data1.put("user_answer_id", message.getUserAnswerId());
        data1.put("answer", message.getAnswer());
        data1.put("status", "readed");
        messagesDoc.document(message.getId()).set(data1);//set data
    }
}
