package student.socialdog;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainFriendslist extends AppCompatActivity {

    public static ArrayList<FriendAdapter.FriendsObject> friendslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendslist_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        friendslist = new ArrayList<FriendAdapter.FriendsObject>();
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




        // Création du RecyclerView
        RecyclerView recyclerView = findViewById(R.id.RV_friends);
        recyclerView.setHasFixedSize(true);

        //Création du linearLayout vertical et association au recyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Spécification de FriendAdapter pour le recyclerview
        recyclerView.setAdapter(new FriendAdapter());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
