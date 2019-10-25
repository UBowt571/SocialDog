package student.socialdog;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainDogslist extends Fragment {

    public static ArrayList<DogAdapter.DogObject> dogslist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View rootView = inflater.inflate(R.layout.dogslist_main, container, false);
        super.onCreate(savedInstanceState);
        dogslist = new ArrayList<>();

        int color1 = Color.argb(100, 0,97,0);
        int color2 = Color.argb(100, 97,0,0);
        int color3 = Color.argb(100, 0,0,75);
        int color4 = Color.argb(100, 179,107,0);
        
        dogslist.add(new DogAdapter.DogObject("Rex",
                "Shiba Inu","1 an", "Dernière promenade hier.", color1, R.drawable.dog1));
        dogslist.add(new DogAdapter.DogObject("Buzz",
                "Caniche","1 an et demi", "Dernière promenade hier.", color2, R.drawable.dog2));
        dogslist.add(new DogAdapter.DogObject("Rantanplan",
                "Corniaud","2 ans", "Dernière promenade il y a 3 jours.", color3, R.drawable.dog3));
        dogslist.add(new DogAdapter.DogObject("Saucisse",
                "Teckel","3 ans", "Dernière promenade il y a 3 jours.", color4, R.drawable.dog3));

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

        // Spécification de DogAdapter pour le recyclerview
        recyclerView.setAdapter(new DogAdapter());
        
        return rootView;
    }
}
