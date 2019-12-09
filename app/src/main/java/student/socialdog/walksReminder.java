package student.socialdog;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import student.socialdog.util.NotificationsDisplayer;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class walksReminder {
    DatabaseReference walksDB;
    public Date lastWalkDate;
    public Date defaultDate;
    SimpleDateFormat sDF = new SimpleDateFormat("yyyy.MM.dd");

    public walksReminder(final Context applicationContext){
        try {
            defaultDate = sDF.parse("0000.00.00");
            lastWalkDate = defaultDate;
        }catch (Exception ex){
            ex.printStackTrace();
        }


        // Déclaration chemin Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        walksDB = database.getReference("paths");
        // Listener : on attend les données de Firebase PUIS on créé le recyclerView
        walksDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Récupération des amis dans la database FireBase
                HashMap<String, Map> walksInDB = (HashMap<String,Map>) dataSnapshot.getValue();


                for(Map.Entry<String, Map> current : walksInDB.entrySet()) {
                    try{
                        String walkOwner = (String)current.getValue().get("userkey");
                        if(walkOwner.equals(MainActivity.userKey)){
                            Object date = current.getValue().get("date");
                            Date currentWalkDate = sDF.parse((String)date);
                            if (currentWalkDate != null && currentWalkDate.after(lastWalkDate)) {
                                lastWalkDate = currentWalkDate;
                            }
                        }
                    } catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
                //Get date
                Date now = Calendar.getInstance().getTime();
                if(!lastWalkDate.equals(defaultDate)){
                    long diff = now.getTime() - lastWalkDate.getTime();
                    long num_days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    if(num_days > 3){
                        NotificationsDisplayer notificationsDisplayer = new NotificationsDisplayer();
                        notificationsDisplayer.displayNotification(applicationContext,"Rappel de promenade","Hey, ça fait "+num_days+" jours que tu n'as pas fait de promenade !");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }




}
