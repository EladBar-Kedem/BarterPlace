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

import java.util.HashMap;
import java.util.Map;

public class DBUsersServices {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private CollectionReference usersRef;

    public DBUsersServices() {
        mAuth = FirebaseAuth.getInstance();             //auth reference
        db = FirebaseFirestore.getInstance();           //firestore reference
        usersRef = db.collection("users");  //collection reference
        //get current user:
        currentUser = mAuth.getCurrentUser();
    }

    public void getUser(IUserCallback cb) {
        DocumentReference userRef = usersRef.document(currentUser.getUid());//user reference in firestore
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot result = task.getResult();
                    if (result.exists()) {
                        Map<String, Object> userAttributes = result.getData();      //get user
                        User user2 = new User(userAttributes.get("first").toString(), userAttributes.get("last").toString(),
                                userAttributes.get("area").toString(), userAttributes.get("phone").toString());         //create user
                        cb.sendUser(user2);             //return user
                    } else {
                        cb.sendUser(null);              //return ull for no exist user
                    }
                } else {
                    cb.sendUser(null);              //return null for unsuccessfully request
                }
            }
        });
    }

    public void getUserByID(String id,IUserCallback cb) {
        DocumentReference userRef = usersRef.document(id);//user reference in firestore
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot result = task.getResult();
                    if (result.exists()) {
                        Map<String, Object> userAttributes = result.getData();      //get user
                        User user2 = new User(userAttributes.get("first").toString(), userAttributes.get("last").toString(),
                                userAttributes.get("area").toString(), userAttributes.get("phone").toString());         //create user
                        cb.sendUser(user2);             //return user
                    } else {
                        cb.sendUser(null);              //return ull for no exist user
                    }
                } else {
                    cb.sendUser(null);              //return null for unsuccessfully request
                }
            }
        });
    }

    //create user in DB
    public void addUserDetailsToDB(String stName, String stLastName, String stArea, String stPhone) {
        CollectionReference usersDoc = db.collection("users"); //receive collection reference
        Map<String, Object> data1 = new HashMap<>(); //make document object to fill
        data1.put("first", stName);
        data1.put("last", stLastName);
        data1.put("area", stArea);
        data1.put("phone", stPhone);
        usersDoc.document(currentUser.getUid()).set(data1);//set data
    }
}
