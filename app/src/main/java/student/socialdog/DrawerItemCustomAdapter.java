package student.socialdog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class DrawerItemCustomAdapter extends ArrayAdapter<DataModel> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<DataModel> data;
    //private DataModel[] data = null;

    DrawerItemCustomAdapter(Context mContext, int layoutResourceId, ArrayList<DataModel> data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);

            ImageView imageViewIcon = convertView.findViewById(R.id.imageViewIcon);
            TextView textViewName = convertView.findViewById(R.id.textViewName);

            DataModel folder = data.get(position);


            imageViewIcon.setImageResource(folder.icon);
            textViewName.setText(folder.name);
        }

        return convertView;
    }
}
