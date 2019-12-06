package student.socialdog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MainFriendslist extends Fragment {

    static ArrayList<FriendAdapter.FriendsObject> friendslist = new ArrayList<>();
    DatabaseReference friendsDB;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.friendslist_main, container, false);

        // Déclaration chemin Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        friendsDB = database.getReference("friends");

        // Listener : on attend les données de Firebase PUIS on créé le recyclerView
        friendsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Récupération des amis dans la database FireBase
                HashMap<String,Map> friendsInDB = (HashMap<String,Map>) dataSnapshot.getValue();
                friendslist = assetLoader.getFriends(getActivity().getApplicationContext(),friendsInDB);

                // Création du recyclerView
                RecyclerView recyclerView;
                try {
                    recyclerView = rootView.findViewById(R.id.RV_friends);
                    recyclerView.setHasFixedSize(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                //Création du linearLayout vertical et association au recyclerView
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                recyclerView = rootView.findViewById(R.id.RV_friends);
                recyclerView.setLayoutManager(layoutManager);
                // Spécification de FriendAdapter pour le recyclerview
                recyclerView.setAdapter(new FriendAdapter());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        Button addFriend = rootView.findViewById(R.id.addFriend);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddFriendActivity();
            }
        });
        super.onCreate(savedInstanceState);
        return rootView;
    }

    private void startAddFriendActivity() {
        Intent intent = new Intent(this.getContext(), AddFriend.class);
        startActivity(intent);
    }
}


