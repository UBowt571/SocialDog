package student.socialdog;

import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class DogSelectionAdapter extends RecyclerView.Adapter<DogSelectionAdapter.DogSelectionViewHolder> {

    private ArrayList<String> dogsSelectionlisted = DogSelection.myDogsSelectionList;
    private ArrayList<String> dogsSelected = MapsActivity.selectedDogs;

    @Override
    public int getItemCount() {
        ArrayList<String> dogsSelectionList = DogSelection.myDogsSelectionList;
        return dogsSelectionList.size();
    }

    @NonNull
    @Override
    public DogSelectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.dogselection_element, parent, false);
        return new DogSelectionViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final DogSelectionViewHolder holder, int position) {
        String dogNameSTR = dogsSelectionlisted.get(position);
        holder.display(dogNameSTR);
    }

    class DogSelectionViewHolder extends RecyclerView.ViewHolder {

        private final TextView dogName;
        private final CheckBox checkbox;
        private final View itemView;

        DogSelectionViewHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            dogName = itemView.findViewById(R.id.dogSelectionName);

            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(checkbox.isChecked()){
                        dogsSelected.add(dogName.getText().toString());
                    }else{
                        dogsSelected.remove(dogName.getText().toString());
                    }
                }
            });
        }

        void display(String dogNameSTR) {
            dogName.setText(dogNameSTR);
        }
    }
}
