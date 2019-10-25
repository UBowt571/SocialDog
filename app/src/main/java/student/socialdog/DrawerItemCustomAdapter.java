package student.socialdog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import org.json.JSONObject;

import java.util.ArrayList;

public class DrawerItemCustomAdapter extends ArrayAdapter<JSONObject> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<JSONObject> data;
    //private DataModel[] data = null;

    DrawerItemCustomAdapter(Context mContext, int layoutResourceId, ArrayList<JSONObject> data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);

            ImageView imageViewIcon = convertView.findViewById(R.id.imageViewIcon);
            TextView textViewName = convertView.findViewById(R.id.textViewName);

            try {
                int imageID = getResIDfromImageName(data.get(position).getString("image"));
                //TODO : find best size for images
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(getBitmapFromResID(imageID).getBitmap(),70,70,true);
                imageViewIcon.setImageBitmap(resizedBitmap);
                textViewName.setText(data.get(position).getString("name"));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return convertView;
    }


    private BitmapDrawable getBitmapFromResID(int resID){
        return  (BitmapDrawable)mContext.getResources().getDrawable(resID);
    }

    private int getImgHeightFromResID(int resID){
        return getBitmapFromResID(resID).getBitmap().getHeight();
    }
    private int getImgWidthFromResID(int resID){
        return getBitmapFromResID(resID).getBitmap().getWidth();
    }
    private int getResIDfromImageName(String imageName){
        return mContext.getResources().getIdentifier(imageName , "drawable", mContext.getPackageName());
    }
}
