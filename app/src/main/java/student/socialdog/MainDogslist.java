package student.socialdog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MainDogslist extends Fragment {

    static ArrayList<DogAdapter.DogObject> dogslist = new ArrayList<>();
    static int alldogscount = 0;
    DatabaseReference allDB;
    DatabaseReference usersDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        final View rootView = inflater.inflate(R.layout.dogslist_main, container, false);

        // Déclaration chemin Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        allDB = database.getReference();
        usersDB = database.getReference("users");

        // Listener : on attend les données de Firebase PUIS on créé le recyclerView
        allDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,HashMap> allInDB = (HashMap<String,HashMap>) dataSnapshot.getValue();
                HashMap<String,Map> usersInDB = allInDB.get("users");
                HashMap<String,Map> dogsInDB = allInDB.get("dogs");
                ArrayList<User.UserObject> usersList = new ArrayList<>();
                usersList = assetLoader.getUsers(getContext(),usersInDB);

                // Récupération des chiens dans la database FireBase
                ArrayList<DogAdapter.DogObject> alldogs = assetLoader.getAllDogs(getActivity().getApplicationContext(),dogsInDB);
                alldogscount = alldogs.size();
                dogslist = assetLoader.getMyDogs(getActivity().getApplicationContext(),dogsInDB);

                // Création du recyclerView
                RecyclerView recyclerView;
                try {
                    recyclerView = rootView.findViewById(R.id.RV_dogs);
                    recyclerView.setHasFixedSize(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                //Création du linearLayout vertical et association au recyclerView
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                recyclerView = rootView.findViewById(R.id.RV_dogs);
                recyclerView.setLayoutManager(layoutManager);
                // Spécification de DogAdapter pour le recyclerview
                recyclerView.setAdapter(new DogAdapter());

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        Button addDog = rootView.findViewById(R.id.addDog);
        addDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddDogActivity();
            }
        });
        super.onCreate(savedInstanceState);
        return rootView;
    }

    private void startAddDogActivity(){
        Intent intent = new Intent(this.getContext(), AddDog.class);
        startActivity(intent);
    }
}
