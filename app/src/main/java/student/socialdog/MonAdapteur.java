package student.socialdog;

import android.app.AlertDialog;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class MonAdapteur extends RecyclerView.Adapter<MonAdapteur.MyViewHolder> {

    private final List<Pair<String, String>> characters = Arrays.asList(
            Pair.create("Angela Merkel", "Chancellière d'Allemagne."),
            Pair.create("Shinzo Abe", "Premier Ministre du Japon."),
            Pair.create("Jair Bolsonaro", "Président du Brésil."),
            Pair.create("Justin Trudeau", "Premier ministre du Canada."),
            Pair.create("Boris Johnson", "Premier ministre du Royaume-Uni."),
            Pair.create("Emmanuel Macron", "Président de la France et des français."),
            Pair.create("Vladimir Poutine", "Président de la Russie."),
            Pair.create("Xi Jinping", "Président de la république populaire de chine."),
            Pair.create("Donald Trump", "Président \"POTUS\" des états-unis."),
            Pair.create("Moon Jae-In", "Président de la Corée du Sud.")
    );

    @Override
    public int getItemCount() {
        return characters.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.friendslist, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Pair<String, String> pair = characters.get(position);
        holder.display(pair);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView description;

        private Pair<String, String> currentPair;

        public MyViewHolder(final View itemView) {
            super(itemView);

            name = ((TextView) itemView.findViewById(R.id.name));
            description = ((TextView) itemView.findViewById(R.id.description));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle(currentPair.first)
                            .setMessage(currentPair.second)
                            .show();
                }
            });
        }

        public void display(Pair<String, String> pair) {
            currentPair = pair;
            name.setText(pair.first);
            description.setText(pair.second);
        }
    }

}