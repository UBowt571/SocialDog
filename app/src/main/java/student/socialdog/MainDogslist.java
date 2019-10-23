package student.socialdog;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainDogslist extends AppCompatActivity {

    public static ArrayList<DogAdapter.DogObject> dogslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dogslist_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dogslist = new ArrayList<DogAdapter.DogObject>();
        dogslist.add(new DogAdapter.DogObject("Rex",
                "Shiba Inu","1 an", "Dernière promenade hier.", DogAdapter.DogObject.dogcolor.ORANGE, R.drawable.dog1));
        dogslist.add(new DogAdapter.DogObject("Buzz",
                "Caniche","1 an et demi", "Dernière promenade hier.", DogAdapter.DogObject.dogcolor.BLUE, R.drawable.dog2));
        dogslist.add(new DogAdapter.DogObject("Rantanplan",
                "Corniaud","2 ans", "Dernière promenade il y a 3 jours.", DogAdapter.DogObject.dogcolor.WHITE, R.drawable.dog3));
        dogslist.add(new DogAdapter.DogObject("Saucisse",
                "Teckel","3 ans", "Dernière promenade il y a 3 jours.", DogAdapter.DogObject.dogcolor.WHITE, R.drawable.dog3));






        // Création du RecyclerView
        RecyclerView recyclerView = findViewById(R.id.RV_dogs);
        recyclerView.setHasFixedSize(true);

        //Création du linearLayout vertical et association au recyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Spécification de DogAdapter pour le recyclerview
        recyclerView.setAdapter(new DogAdapter());
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
