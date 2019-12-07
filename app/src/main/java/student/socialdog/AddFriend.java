package student.socialdog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AddFriend extends AppCompatActivity {
    DatabaseReference usersDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);

        // Déclaration chemin Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersDB = database.getReference("users");


        Button confirmFriend = findViewById(R.id.confirmFriend);
        Button returnButton = findViewById(R.id.returnButton);

        confirmFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNewFriend();
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToFriendsList();
            }
        });
    }

    private void sendNewFriend(){
        EditText email = findViewById(R.id.emailfield);
        final String newfriend = email.getText().toString();

        usersDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Récupération des utilisateurs dans la database FireBase
                HashMap<String, Map> usersInDB = (HashMap<String,Map>) dataSnapshot.getValue();
                ArrayList<User.UserObject> usersList = new ArrayList<>();
                usersList = assetLoader.getUsers(getApplicationContext(),usersInDB);
                boolean userExists = false;
                String friendID = "";

                for(int i=0;i<usersList.size();i++){
                    String thisEmail = usersList.get(i).email;
                    if(newfriend.equals(thisEmail)){
                        userExists = true;
                        friendID = usersList.get(i).id;
                    }
                    if(userExists){
                        HashMap<String,String> map = new HashMap<>();
                        map.put(friendID,"true");
                        usersDB.child(MainActivity.userKey).child("friends").setValue(map);
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Utilisateur introuvable, peut-être qu'il n'utilise pas encore Social Dog ?",Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void backToFriendsList(){
        finish();
    }
}
