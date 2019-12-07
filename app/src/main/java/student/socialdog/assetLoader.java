package student.socialdog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import androidx.annotation.MainThread;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class assetLoader {
    private static Context mContext = null;

    /**
     * Returns a orj.json.JSONObject from the app's context and a filename
     * @param context The app's context, most of the times, typing "this" from an Activity is OK
     * @param filename Name of the JSON file to load
     * @return JSONObject Return a JSONObject representation of the JSON file
     */
    public static JSONObject JSON(Context context,String filename) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(filename);
            mContext = context;
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

    /**
     * Returns an ArrayList from a JSONObject
     * @param jsonObject JSONOject containing the desired JSONArray
     * @param arrayName Name of the desired JSONArray in jsonObject
     * @return ArrayList<JSONObject> Return a list of JSONObjects
     */
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

    /**
     * Returns an ArrayList of FriendAdapter.FriendsObject
     * @return ArrayList<FriendAdapter.FriendsObject> Return a list of FriendAdapter.FriendsObject
     */
    public static ArrayList<User.UserObject> getFriends(ArrayList<User.UserObject> usersInDB){
        return getFriends(mContext,usersInDB);
    }

    /**
     * Returns an ArrayList of FriendAdapter.FriendsObject
     * @param pcontext Context to use to load JSON file. Might be usefull if mContext from assetLoader isn't initialized yet
     * @return ArrayList<FriendAdapter.FriendsObject> Return a list of FriendAdapter.FriendsObject
     */
    public static ArrayList<User.UserObject> getFriends(Context pcontext, ArrayList<User.UserObject> usersList) {
        ArrayList<String> myFriendsListStr = new ArrayList<>();
        ArrayList<User.UserObject> myFriendsListObj = new ArrayList<>();
        for(int i=0;i<usersList.size();i++) {
            if(MainActivity.userKey.equals(usersList.get(i).id)){
                myFriendsListStr.addAll(usersList.get(i).friends);
            }
        }
        for(int i=0;i<myFriendsListStr.size();i++) {
            for(int j=0;j<usersList.size();j++){
                if(myFriendsListStr.get(i).equals(usersList.get(j).id)){
                    myFriendsListObj.add(usersList.get(j));
                }
            }
        }


        return myFriendsListObj;
    }

    /**
     * Returns ressource ID for an image as an "int"
     * @param imageName Name of the image ressource to load.
     * @return int Return the ressource ID of image named "imageName" as an "int"
     */
    public static int getResIDfromImageName(String imageName){
        int result = getResIDfromImageName(imageName,mContext);
        if(result == -1){
            Log.e("assetLoader","Context isn't loaded yet, assetLoader.JSON or getResIDfromImageName(String imageName,Context context) to load context !");
        }
        return -1;
    }

    /**
     * Returns ressource ID for an image as an "int"
     * @param imageName Name of the image ressource to load.
     * @param context Context from which to load the ressources
     * @return int Return the ressource ID of image named "imageName" as an "int"
     */
    public static int getResIDfromImageName(String imageName,Context context){
        if(context!=null){
            return context.getResources().getIdentifier(imageName , "drawable", context.getPackageName());
        }
        Log.e("assetLoader","Context parameter isn't correct.");
        return -1;
    }


    /**
     * Returns an ArrayList of DogAdapter.DogObject
     * @return ArrayList<DogAdapter.DogObject> Return a list of DogAdapter.DogObject
     */
    public static ArrayList<DogAdapter.DogObject> getMyDogs(Map<String,Map> dogsInDB){
        return getMyDogs(mContext,dogsInDB);
    }

    /**
     * Returns an ArrayList of DogAdapter.DogObject
     * @param pcontext Context to use to load JSON file. Might be usefull if mContext from assetLoader isn't initialized yet
     * @return ArrayList<DogAdapter.DogObject> Return a list of DogAdapter.DogObject
     */

    public static ArrayList<DogAdapter.DogObject> getMyDogs(Context pcontext,Map<String,Map> dogsInDB) {
        ArrayList<DogAdapter.DogObject> dogslist = new ArrayList<>();
        for(Map.Entry<String, Map> current : dogsInDB.entrySet()) {
            if(current.getValue().get("dogowner").toString().equals(MainActivity.userKey)){
                try{
                    Object dogname = current.getValue().get("dogname");
                    Object dograce = current.getValue().get("dograce");
                    Object dogage = current.getValue().get("dogage");
                    Object lastWalk = current.getValue().get("lastWalk");
                    Object dogcolor = current.getValue().get("dogcolor");
                    int idogcolor = Integer.parseInt(dogcolor.toString());
                    Object dogpic = current.getValue().get("dogpic");
                    int imgResID = getResIDfromImageName(dogpic.toString(),pcontext);
                    dogslist.add(new DogAdapter.DogObject((String) dogname,(String) dograce,(String) dogage,(String) lastWalk,MainActivity.userKey, idogcolor,imgResID));
                } catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }
        return dogslist;
    }

    public static ArrayList<User.UserObject> getUsers(HashMap<String,Map> usersInDB){
        return getUsers(mContext,usersInDB);
    }

    public static ArrayList<User.UserObject> getUsers(Context pcontext, HashMap<String,Map> usersInDB) {
        ArrayList<User.UserObject> usersList = new ArrayList<>();
        for(Map.Entry<String, Map> current : usersInDB.entrySet()) {
            try{

                String id = current.getKey();
                Object displayedName = current.getValue().get("displayedName");
                Object email = current.getValue().get("email");
                Object lastWalk = current.getValue().get("lastWalk");
                Object photoURL = current.getValue().get("photoURL");
                Object friends = current.getValue().get("friends");
                Object dogs = current.getValue().get("dogs");

                ArrayList<String> userFriendsList = new ArrayList<>();
                HashMap<String,String> friends2 = (HashMap<String, String>) friends;
                if(friends!=null){
                    for(Map.Entry<String, String> curFriend : friends2.entrySet()){
                        userFriendsList.add(curFriend.getKey());
                    }
                }
                ArrayList<String> userDogsList = new ArrayList<>();

                usersList.add(new User.UserObject(id,(String) email, (String) displayedName,(String) lastWalk, (String) photoURL,userFriendsList,userDogsList));
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
        return usersList;
    }





    public static ArrayList<DogAdapter.DogObject> getAllDogs(Map<String,Map> dogsInDB){
        return getAllDogs(mContext,dogsInDB);
    }


    public static ArrayList<DogAdapter.DogObject> getAllDogs(Context pcontext,Map<String,Map> dogsInDB) {
        ArrayList<DogAdapter.DogObject> dogslist = new ArrayList<>();
        for(Map.Entry<String, Map> current : dogsInDB.entrySet()) {
            try{
                Object dogname = current.getValue().get("dogname");
                Object dograce = current.getValue().get("dograce");
                Object dogage = current.getValue().get("dogage");
                Object lastWalk = current.getValue().get("lastWalk");
                Object dogcolor = current.getValue().get("dogcolor");
                int idogcolor = Integer.parseInt(dogcolor.toString());
                Object dogpic = current.getValue().get("dogpic");
                int imgResID = getResIDfromImageName(dogpic.toString(),pcontext);
                dogslist.add(new DogAdapter.DogObject((String) dogname,(String) dograce,(String) dogage,(String) lastWalk,MainActivity.userKey, idogcolor,imgResID));
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
        return dogslist;
    }

}
