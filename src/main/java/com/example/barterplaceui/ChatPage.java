package com.example.barterplaceui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import services.DBMessagesService;
import services.IMessagesCallBack;
import services.Message;

//display messages answers to barter offers that the current user sent
public class ChatPage extends AppCompatActivity {
    ArrayList<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        putMessages();
    }

    //get all  relevant messages
    private void putMessages() {
        DBMessagesService service = new DBMessagesService();
        service.getUserMessages(new IMessagesCallBack() {
            @Override
            public void sendAllMessages(ArrayList<Message> messages) {
                saveMessages(messages);
            }
        });
        service.getMoreUserMessages(new IMessagesCallBack(){
            @Override
            public void sendAllMessages(ArrayList<Message> messages) {
                concatMessages(messages);
            }
        });

    }

    private void concatMessages(ArrayList<Message> messages) {
        if(messages != null){
            this.messages.addAll(messages);
        }
        insertToFields(this.messages);
    }

    private void saveMessages(ArrayList<Message> messages) {
        this.messages = messages;       //store messages
    }

    //display messages
    private void insertToFields(ArrayList<Message> messages) {
        if (messages != null && messages.size() != 0) {
            ListView lv = (ListView)findViewById(R.id.listView);
            CustomAdapter adapter = new CustomAdapter(this, messages);
            lv.setAdapter(adapter);        //set adapter
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    editMessageStatus(position);        //edit message to readed
                    messageClicked(position);           //open message clicked
                }
            });
        } else {
            Toast.makeText(getBaseContext(), "There are no Messages yet", Toast.LENGTH_SHORT).show();        //no messages case
        }
    }

    //change message status to readed
    private void editMessageStatus(int position) {
        DBMessagesService service = new DBMessagesService();
        service.changeMessageStatus(messages.get(position));
    }

    //send barter offer id to open
    private void messageClicked(int position) {
        Intent intent = new Intent(this, AnswerBarterOfferPage.class);
        String id = messages.get(position).getBarterOfferId();
        intent.putExtra("barter_offer_id_to_open",id);
        startActivity(intent);
        finish();
    }


    public void moveToSettingsPage(View view) {
        //send to SettingsPage
        Intent intent = new Intent(getBaseContext(),SettingsPage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();
    }

    public void moveToHomePage(View view) {
        //send to HomePage
        Intent intent = new Intent(getBaseContext(),HomePage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();

    }

    public void moveToBartersPage(View view) {
        //send to BartersPage
        Intent intent = new Intent(getBaseContext(),BartersPage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();

    }

    public void moveToProfilePage(View view) {
        //send to ProfilePage
        Intent intent = new Intent(getBaseContext(),ProfilePage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();
    }
}