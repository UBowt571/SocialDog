package student.socialdog;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;

public class assetLoader {
    public static JSONObject JSON(Context context,String filename) {
        String jsonString = null;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
            return new JSONObject(jsonString);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public static ArrayList<JSONObject> getJSONArray(JSONObject jsonObject,String arrayName){
        ArrayList<JSONObject> arrayData = new ArrayList<>();
        JSONArray itemsList;
        try{
            itemsList = jsonObject.getJSONArray(arrayName);
            for(int i = 0 ; i < itemsList.length();i++){
                arrayData.add(itemsList.getJSONObject(i));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return arrayData;
    }
}
