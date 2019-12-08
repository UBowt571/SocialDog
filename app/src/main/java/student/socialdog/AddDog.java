package student.socialdog;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class AddDog extends AppCompatActivity {
    DatabaseReference dogsDB;
    DatabaseReference usersDB;
    String newID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_dog);

        // Déclaration chemin Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dogsDB = database.getReference("dogs");
        usersDB = database.getReference("users");

        Button confirmDog = findViewById(R.id.confirmDog);

        confirmDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNewDog();
            }
        });
    }

    private void sendNewDog(){
        EditText dogname = findViewById(R.id.dognamefield);
        EditText dograce = findViewById(R.id.dogracefield);
        EditText dogage = findViewById(R.id.dogagefield);
        String sdogage = dogage.getText().toString()+ " ans";

        HashMap<String, Object> map = new HashMap<>();
        map.put("dogname", dogname.getText().toString());
        map.put("dograce", dograce.getText().toString());
        map.put("dogage", sdogage);
        map.put("lastWalk", "Non actualisé");
        map.put("dogowner", MainActivity.userKey);
        map.put("dogcolor", "1677746432");
        map.put("dogpic", "dog2");
        newID = "dog"+MainDogslist.alldogscount;
        dogsDB.child(newID).setValue(map);
        usersDB.child(MainActivity.userKey).child("dogs").child(newID).setValue("true");


        finish();

    }
}
