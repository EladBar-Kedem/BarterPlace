package services;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import androidx.annotation.NonNull;


import com.example.barterplaceui.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firestore.v1.WriteResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBBartersServices {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private CollectionReference bartersRef;
    int noBarterImage = R.drawable.ic_help;

    public DBBartersServices() {
        mAuth = FirebaseAuth.getInstance();                          //auth reference
        db = FirebaseFirestore.getInstance();                        //firestore reference
        bartersRef = db.collection("Barters");            //collection reference
        //get current user:
        currentUser = mAuth.getCurrentUser();
    }

    //get all the barters of the current user
    public void getUserBarters(IBarterCallback cb) {
        bartersRef.whereEqualTo("user_id", currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Barter[] barters = new Barter[5];       //Barter Limit
                    int i = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> barterAttributes = document.getData();      //get each barter in loop
                        if (barterAttributes.get("status").toString().equals("open")) {      //check barter status
                            Barter barter = new Barter(document.getId(), barterAttributes.get("title").toString(),
                                    barterAttributes.get("area").toString(), currentUser.getUid(), barterAttributes.get("details").toString(),
                                    barterAttributes.get("status").toString()); //create barter
                            barters[i++] = barter;     //store barter
                        }
                    }
                    cb.sendBarters(barters);        //return barters while finish
                } else {
                    cb.sendBarters(null);           //return null for unsuccessfully request
                }
            }
        });
    }

    //get barter by barter id that received
    public void getBarterById(String barterId, IbarterByIdCallBack cb) {
        DocumentReference barterRef = bartersRef.document(barterId);//barter by id reference in firestore
        barterRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot result = task.getResult();
                    if (result.exists()) {
                        Map<String, Object> barterAttributes = result.getData();        //get the barter
                        Barter barter = new Barter(barterId, barterAttributes.get("title").toString(),
                                barterAttributes.get("area").toString(), barterAttributes.get("user_id").toString(),
                                barterAttributes.get("details").toString(), barterAttributes.get("status").toString());
                        cb.sendBarter(barter);
                    } else {
                        cb.sendBarter(null);
                    }
                } else {
                    cb.sendBarter(null);
                }
            }
        });
    }

    //get barter's picture by the barter id
    public void getBarterPicById(String barterId, FirebaseStorage storage, IBarterPicByIdCallBack cb) {
        StorageReference imgRef = storage.getReference().child("barters_images/" + barterId);     //reference to barter's pic
        try {
            File localFile = File.createTempFile(barterId, "jpeg");
            imgRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());          //get the picture
                            cb.sendBarterPic(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    cb.sendBarterPic(null);                 //on fail send null
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //get all open barters to show in main app screen (Barters)
    public void getAllBarters(IbartersCallBack cb) {
        bartersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Barter> barters = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> barterAttributes = document.getData();          //get each barter in loop
                        if (barterAttributes.get("status").toString().equals("open")) {
                            Barter barter = new Barter(document.getId(), barterAttributes.get("title").toString(),
                                    barterAttributes.get("area").toString(), barterAttributes.get("user_id").toString(),
                                    barterAttributes.get("details").toString(), barterAttributes.get("status").toString());   //create barter
                            barters.add(barter);           //store each barter
                        }
                    }
                    cb.sendAllBarters(barters);             //return all barters
                } else {
                    cb.sendAllBarters(null);                //return null for unsuccessfully request
                }
            }
        });
    }

    public void getAllRelevantBarters(String userInput, IbartersCallBack cb) {
        bartersRef.whereEqualTo("title", userInput).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Barter> barters = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> barterAttributes = document.getData();      //get each barter in loop
                        if (barterAttributes.get("status").toString().equals("open")) {      //check barter status
                            Barter barter = new Barter(document.getId(), barterAttributes.get("title").toString(),
                                    barterAttributes.get("area").toString(), currentUser.getUid(), barterAttributes.get("details").toString(),
                                    barterAttributes.get("status").toString()); //create barter
                            barters.add(barter);     //store barter
                        }
                    }
                    cb.sendAllBarters(barters);        //return barters while finish
                } else {
                    cb.sendAllBarters(null);           //return null for unsuccessfully request
                }
            }
        });
    }

    //create barter in DB
    public void addBarterToDB(String barterID, String stTitle, String stArea, String stDetails) {
        CollectionReference bartersDoc = db.collection("Barters"); //receive collection reference
        Map<String, Object> data1 = new HashMap<>(); //make document object to fill
        data1.put("image_file_name", barterID);
        data1.put("title", stTitle);
        data1.put("area", stArea);
        data1.put("details", stDetails);
        data1.put("user_id", currentUser.getUid());
        data1.put("status", "open");
        bartersDoc.document(barterID).set(data1);//set data
    }

    //close barter in DB by change status to close
    public void closeBarterInDB(String barterID, String stTitle, String stArea, String stDetails) {
        CollectionReference bartersDoc = db.collection("Barters"); //receive collection reference
        Map<String, Object> data1 = new HashMap<>(); //make document object to fill
        data1.put("image_file_name", barterID);
        data1.put("title", stTitle);
        data1.put("area", stArea);
        data1.put("details", stDetails);
        data1.put("user_id", currentUser.getUid());
        data1.put("status", "close");
        bartersDoc.document(barterID).set(data1);//set data
    }


}
