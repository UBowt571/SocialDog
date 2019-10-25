package student.socialdog;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DogAdapter extends RecyclerView.Adapter<DogAdapter.DogViewHolder> {

    private ArrayList<DogObject> dogslisted = MainDogslist.dogslist;



    @Override
    public int getItemCount() {
        ArrayList<DogObject> friendslistedtest = MainDogslist.dogslist;
        return friendslistedtest.size();
    }

    @Override
    public DogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.dogslist, parent, false);
        return new DogViewHolder(view);


    }

    @Override
    public void onBindViewHolder(DogViewHolder holder, int position) {
        DogObject dog = dogslisted.get(position);
        holder.display(dog);
    }

    class DogViewHolder extends RecyclerView.ViewHolder {

        private final TextView dogname;
        private final TextView dograce;
        private final TextView dogage;
        private final TextView lastWalk;
        private final ImageView profilepic;

        DogObject currentdog = new DogObject("","","1 an", "", DogObject.dogcolor.WHITE, 0);

        DogViewHolder(final View itemView) {
            super(itemView);

            dogname = itemView.findViewById(R.id.dogname);
            dograce = itemView.findViewById(R.id.dograce);
            dogage = itemView.findViewById(R.id.dogage);
            lastWalk = itemView.findViewById(R.id.lastWalk);
            profilepic = itemView.findViewById(R.id.dogpic);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle(currentdog.dogname)
                            .setMessage(currentdog.dograce)
                            .setMessage(currentdog.dogage)
                            .setMessage(currentdog.lastWalk)
                            .show();
                }
            });
        }

        void display(DogObject dog) {
            currentdog = dog;
            dogname.setText(dog.dogname);
            dograce.setText(dog.dograce);
            dogage.setText(dog.dogage);
            lastWalk.setText(dog.lastWalk);
            profilepic.setImageResource(dog.dogpic);
        }
    }

    static class DogObject {
        String dogname;
        String dograce;
        String dogage;
        String lastWalk;
        dogcolor color;
        public enum dogcolor {
            BLUE,
            RED,
            GREEN,
            YELLOW,
            BROWN,
            ORANGE,
            WHITE,
            BLACK
        }
        int dogpic;


        DogObject(String pname, String pdograce, String pdogage, String plastWalk, dogcolor pdogcolor, int pdogpic){
            this.dogname = pname;
            this.dograce = pdograce;
            this.dogage = pdogage;
            this.lastWalk = plastWalk;
            this.color = pdogcolor;
            this.dogpic = pdogpic;
        }
    }


}