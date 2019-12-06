package student.socialdog.ui.friends;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import student.socialdog.R;

public class AddFriend extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);

        Button confirmFriend = findViewById(R.id.confirmFriend);

        confirmFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNewFriend();
            }
        });
    }

    private void sendNewFriend(){
        EditText firstname = findViewById(R.id.firstnamefield);
        EditText lastname = findViewById(R.id.lastnamefield);
        String name = firstname.getText()+" "+lastname.getText();
        MainFriendslist.friendslist.add(new FriendAdapter.FriendsObject(name,"Non actualisé",R.drawable.mgagnon));

        Intent friendadded = new Intent();
        friendadded.setAction("com.example.Broadcast");
        sendBroadcast(friendadded);
        finish();

    }







}
