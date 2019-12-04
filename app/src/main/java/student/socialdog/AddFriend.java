package student.socialdog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class AddFriend extends AppCompatActivity {
    DatabaseReference friendsDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);

        // Déclaration chemin Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        friendsDB = database.getReference("friends").push();


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

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("lastWalk", "Non actualisé.");
        map.put("ppic", "mgagnon");

        friendsDB.setValue(map);

        Intent friendadded = new Intent();
        friendadded.setAction("com.example.Broadcast");
        sendBroadcast(friendadded);
        finish();

    }







}
