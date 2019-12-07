package student.socialdog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MainActivity extends AppCompatActivity {   // implements NavigationView.OnNavigationItemSelectedListener

    private AppBarConfiguration mAppBarConfiguration;
    DatabaseReference usersDB;
    static String userKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Récupération des informations du compte Google pour les afficher au dessus du menu
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        TextView title = navigationView.getHeaderView(0).findViewById(R.id.title_textView);
        TextView subtitle = navigationView.getHeaderView(0).findViewById(R.id.subtitle_textView);
        ImageView imgv = navigationView.getHeaderView(0).findViewById(R.id.headerImageView);
        final String userEmail = account.getEmail();
        userKey = userEmail.substring(0,userEmail.indexOf("@"));
        userKey = userKey.replaceAll("[.]","");
        final String userDisplayedName = account.getDisplayName();
        final String userPhotoURL = account.getPhotoUrl().toString();
        title.setText(userDisplayedName);
        subtitle.setText(userEmail);
        String imgurl = userPhotoURL;
        Glide.with(this).load(imgurl).into(imgv);

        // On insère l'utilisateur dans la base de données si il n'est pas présent
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersDB = database.getReference("users");
        usersDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Récupération des utilisateurs dans la database FireBase
                HashMap<String, Map> usersInDB = (HashMap<String,Map>) dataSnapshot.getValue();
                ArrayList<User.UserObject> usersList = new ArrayList<>();
                usersList = assetLoader.getUsers(getApplicationContext(),usersInDB);
                boolean userExists = false;

               for(int i=0;i<usersList.size();i++){
                   String thisKey = usersList.get(i).id;
                   if(userKey.equals(thisKey)){
                       userExists = true;
                   }
               }
               if(!userExists){
                   HashMap<String,Object> map = new HashMap<>();
                   map.put("displayedName",userDisplayedName);
                   map.put("email",userEmail);
                   map.put("lastWalk","");
                   map.put("photoURL",userPhotoURL);
                   usersDB.child(userKey).setValue(map);
               }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_friendslist,
                R.id.nav_dogs)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Intent intent = new Intent(getApplicationContext(), NotificationsHandlerService.class);
        getApplicationContext().startService(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
