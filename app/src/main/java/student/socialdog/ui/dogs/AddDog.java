package student.socialdog.ui.dogs;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import student.socialdog.R;

public class AddDog extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_dog);

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
        String sdogage = dogage.toString()+ " ans";
        MainDogslist.dogslist.add(new DogAdapter.DogObject(dogname.toString(),dograce.toString(),sdogage, "Non actualisé",Color.argb(100, 179,107,0),R.drawable.dog1));

        finish();

    }
}
