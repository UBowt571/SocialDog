package student.socialdog.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;

import student.socialdog.R;
import student.socialdog.assetLoader;

public class NotificationsDisplayer {

    /**
     * Return the list of saved channels.
     * @return Saved channels
     * @brief To add a new channel, you must use NotificationsDisplayer.createNotificationChannel
     */
    public static ArrayList<customChannel> getChannelsList() {return channelsList;}

    /**
     * Return the list of saved channels.
     * @return Saved channels
     * @brief To add a new channel, you must use NotificationsDisplayer.createNotificationChannel
     */
    public static customChannel getLastChannelsFromList() {return channelsList.get(channelsList.size()-1);}

    private static ArrayList<customChannel> channelsList = new ArrayList<>();

    /**
     * Create channel for notifications
     * @param context Context of the application
     * @param customChannel customChannel to create before containing all needed params
     * @return The channel created
     * @brief The created returned channel does not need to be stored outsite of the class : it is stored directly in 'channelsList' ArrayList.
     */
    public Integer createNotificationChannel(Context context, customChannel customChannel) {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(customChannel.mChannel_STRID, customChannel.mName, customChannel.mImportance);
            channel.setDescription(customChannel.mDescription);
            customChannel.channel = channel;                    /* customChannel is now full */


            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            try {
                notificationManager.createNotificationChannel(customChannel.channel);
            }catch (NullPointerException e){e.printStackTrace();return null;}

            /* Channel successfully added to the system : returns the channel */
            channelsList.add(customChannel);
            return channelsList.size()-1;
        }
        return null;
    }

    public customChannel getCustomChannel(String pName,String pDescription,int pImportance){
        return new customChannel(pName,pDescription,pImportance);
    }

    public class customChannel {
        private String mChannel_STRID = null;
        private int mChannel_INTID = -1;
        private NotificationChannel channel = null;

        public String mName;
        public String mDescription;
        public int mImportance;

        public customChannel(String pName,String pDescription,int pImportance){
            mName = pName;mDescription = pDescription;mImportance = pImportance;
            mChannel_INTID = channelsList.size();
            mChannel_STRID = Integer.toString(mChannel_INTID);
        }
    }


    /**
     * Easy notifications displayer for the app
     * @param applicationContext The context of the application
     * @param customNotification An object to store parameters of a notification
     * @param registered_CustomChannelID An ID retrieved after registering a channel (cf .createNotificationChannel() )
     */
    public static void displayNotification(Context applicationContext,customNotification customNotification,int registered_CustomChannelID){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(applicationContext, channelsList.get(registered_CustomChannelID).mChannel_STRID)
                .setSmallIcon(customNotification.mIconID)
                .setContentTitle(customNotification.mNotificationTitle)
                .setContentText(customNotification.mNotificationText);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(applicationContext);
        notificationManagerCompat.notify(customNotification.mNotificationID, builder.build());
    }


    /**
     * Simpler (only title and content) but needs NotificationDisplayer to be instanciated.
     * @param applicationContext The context of the application
     * @param notificationTitle
     * @param notificationContent
     * @param registered_CustomChannelID An ID retrieved after registering a channel (cf .createNotificationChannel() )
     */
    public void displayNotification(Context applicationContext,String notificationTitle,String notificationContent,int registered_CustomChannelID){
        customNotification customNotification = new customNotification(notificationTitle,notificationContent);
        customNotification.setImageID("chien",applicationContext);

        displayNotification(applicationContext,customNotification,registered_CustomChannelID);
    }

    static private int mLastNotificationID = -1;
    public class customNotification {
        private int mNotificationID = -1;
        private int mIconID = R.drawable.chien;
        public String mNotificationTitle;
        public String mNotificationText;
        public customNotification(String pNotificationTitle, String pNotificationText){
            mNotificationTitle = pNotificationTitle;mNotificationText = pNotificationText;
            mNotificationID = ++mLastNotificationID;  // Caution /!\ : This operation increments mLastNotificationID and THEN set the value of mNotificationID !
        }

        /**
         * Set an image for the notification.
         * @param imageName image name to put in the notification
         * @param context context of the app
         * @return imageID or '-1' if error (if error, default image is left)
         */
        public int setImageID(String imageName,Context context){
            int result = assetLoader.getResIDfromImageName(imageName,context);
            if(result != -1){
                this.mIconID = result;
            }
            return result;
        }
    }
}
