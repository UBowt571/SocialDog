package student.socialdog;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MainFriendslist extends Fragment {

    static ArrayList<FriendAdapter.FriendsObject> friendslist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friendslist_main, container, false);
        super.onCreate(savedInstanceState);
        friendslist = new ArrayList<>();

        friendslist.add(new FriendAdapter.FriendsObject("Marie Gagnon",
                "Actuellement en promenade",R.drawable.mgagnon));
        friendslist.add(new FriendAdapter.FriendsObject("Michel Tremblay",
                "Dernière promenade il y a 5 jours.",R.drawable.mtremblay));
        friendslist.add(new FriendAdapter.FriendsObject("Alice Martel",
                "Dernière promenade il y a plus d'un mois.",R.drawable.amartel));
        friendslist.add(new FriendAdapter.FriendsObject("Maxime Defrances",
                "Dernière promenade il y a deux semaines.",R.drawable.mgagnon));
        friendslist.add(new FriendAdapter.FriendsObject("Thomas Gabriel",
                "Dernière promenade il y a plus d'un mois.",R.drawable.amartel));
        friendslist.add(new FriendAdapter.FriendsObject("Anatole Martin",
                "Dernière promenade il y a plus d'un mois.",R.drawable.mtremblay));
        friendslist.add(new FriendAdapter.FriendsObject("Roger Gagnon",
                "Dernière promenade il y a plus d'un mois.",R.drawable.mtremblay));
        friendslist.add(new FriendAdapter.FriendsObject("Michèle Madre",
                "Dernière promenade il y a plus d'un mois.",R.drawable.amartel));
        friendslist.add(new FriendAdapter.FriendsObject("Yanis Vervet",
                "Dernière promenade il y a plus d'un mois.",R.drawable.mgagnon));

RecyclerView recyclerView;
        try {
            // Création du RecyclerView
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

        return rootView;
    }
}
