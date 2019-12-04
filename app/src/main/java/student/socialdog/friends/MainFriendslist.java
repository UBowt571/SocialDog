package student.socialdog.friends;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import java.util.ArrayList;

import student.socialdog.R;
import student.socialdog.assetLoader;

public class MainFriendslist extends Fragment {

    static ArrayList<FriendAdapter.FriendsObject> friendslist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friendslist_main, container, false);
        super.onCreate(savedInstanceState);

        // Récupération de la liste d'amis depuis JSON
        friendslist = assetLoader.getFriends(this.getContext());

        // Création du RecyclerView
        RecyclerView recyclerView;
        try {
            recyclerView = rootView.findViewById(R.id.RV_friends);
            recyclerView.setHasFixedSize(true);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }

        //Création du linearLayout vertical et association au recyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        // Spécification de FriendAdapter pour le recyclerview
        recyclerView.setAdapter(new FriendAdapter());

        Button addFriend = rootView.findViewById(R.id.addFriend);

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddFriendActivity();
            }
        });

        return rootView;
    }

    private void startAddFriendActivity(){
        Intent intent = new Intent(this.getContext(), AddFriend.class);
        startActivity(intent);
    }

}
