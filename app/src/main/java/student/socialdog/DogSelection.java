package student.socialdog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DogSelection extends AppCompatActivity {
    static ArrayList<String> myDogsSelectionList = new ArrayList<>();
    DatabaseReference DogsDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dog_selection);

        Button validateBTN = findViewById(R.id.validate_dogs);
        validateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Déclaration chemin Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DogsDB = database.getReference("dogs");

        // Listener : on attend les données de Firebase PUIS on créé le recyclerView
        DogsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Récupération des amis dans la database FireBase
                HashMap<String, Map> dogsInDB = (HashMap<String,Map>) dataSnapshot.getValue();

                for(Map.Entry<String, Map> current : dogsInDB.entrySet()) {
                    try{
                        String dogOwner = (String)current.getValue().get("dogowner");
                        if(dogOwner.equals(MainActivity.userKey)){
                            myDogsSelectionList.add((String)current.getValue().get("dogname"));
                        }
                    } catch(Exception ex){
                        ex.printStackTrace();
                    }
                }

                // Création du recyclerView
                RecyclerView recyclerView;
                try {
                    recyclerView = findViewById(R.id.RV_dogsToSelect);
                    recyclerView.setHasFixedSize(true);
                    //Création du linearLayout vertical et association au recyclerView
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);


                    // Spécification de FriendAdapter pour le recyclerview
                    recyclerView.setAdapter(new DogSelectionAdapter());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });


    }
}