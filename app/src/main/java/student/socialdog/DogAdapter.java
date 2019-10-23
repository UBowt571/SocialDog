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

    public ArrayList<DogObject> dogslisted = MainDogslist.dogslist;



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

    public class DogViewHolder extends RecyclerView.ViewHolder {

        private final TextView dogname;
        private final TextView dograce;
        private final TextView dogage;
        private final TextView lastWalk;
        private final ImageView profilepic;

        public DogObject currentdog = new DogObject("","","1 an", "", DogObject.dogcolor.WHITE, 0);

        public DogViewHolder(final View itemView) {
            super(itemView);

            dogname = ((TextView) itemView.findViewById(R.id.dogname));
            dograce = ((TextView) itemView.findViewById(R.id.dograce));
            dogage = ((TextView) itemView.findViewById(R.id.dogage));
            lastWalk = ((TextView) itemView.findViewById(R.id.lastWalk));
            profilepic = ((ImageView) itemView.findViewById(R.id.dogpic));

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

        public void display(DogObject dog) {
            currentdog = dog;
            dogname.setText(dog.dogname);
            dograce.setText(dog.dograce);
            dogage.setText(dog.dogage);
            lastWalk.setText(dog.lastWalk);
            profilepic.setImageResource(dog.dogpic);
        }
    }

    public static class DogObject {
        public String dogname;
        public String dograce;
        public String dogage;
        public String lastWalk;
        public dogcolor color;
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
        public int dogpic;


        public DogObject(String pname, String pdograce, String pdogage, String plastWalk, dogcolor pdogcolor, int pdogpic){
            this.dogname = pname;
            this.dograce = pdograce;
            this.dogage = pdogage;
            this.lastWalk = plastWalk;
            this.color = pdogcolor;
            this.dogpic = pdogpic;
        }
    }


}