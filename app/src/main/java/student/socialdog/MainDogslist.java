package student.socialdog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainDogslist extends Fragment {

    static ArrayList<DogAdapter.DogObject> dogslist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dogslist_main, container, false);
        super.onCreate(savedInstanceState);
        dogslist = new ArrayList<>();
        dogslist.add(new DogAdapter.DogObject("Rex",
                "Shiba Inu","1 an", "Dernière promenade hier.", DogAdapter.DogObject.dogcolor.ORANGE, R.drawable.dog1));
        dogslist.add(new DogAdapter.DogObject("Buzz",
                "Caniche","1 an et demi", "Dernière promenade hier.", DogAdapter.DogObject.dogcolor.BLUE, R.drawable.dog2));
        dogslist.add(new DogAdapter.DogObject("Rantanplan",
                "Corniaud","2 ans", "Dernière promenade il y a 3 jours.", DogAdapter.DogObject.dogcolor.WHITE, R.drawable.dog3));
        dogslist.add(new DogAdapter.DogObject("Saucisse",
                "Teckel","3 ans", "Dernière promenade il y a 3 jours.", DogAdapter.DogObject.dogcolor.WHITE, R.drawable.dog3));

        RecyclerView recyclerView;
        try {
            // Création du RecyclerView
            recyclerView = rootView.findViewById(R.id.RV_dogs);
            recyclerView.setHasFixedSize(true);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }

        //Création du linearLayout vertical et association au recyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Spécification de FriendAdapter pour le recyclerview
        recyclerView.setAdapter(new DogAdapter());

        return rootView;
    }
}
