package student.socialdog;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import student.socialdog.util.NotificationsDisplayer;

public class NotificationsHandlerService extends Service {
    final static String TAG = "NotificationsHandler";
    public NotificationsHandlerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
//        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationsDisplayer notificationsDisplayer = new NotificationsDisplayer();
        NotificationsDisplayer.customChannel customChannel = notificationsDisplayer.getCustomChannel("My first channel","This is a random description for a random channel", NotificationManager.IMPORTANCE_HIGH);
        int channel_ID = notificationsDisplayer.createNotificationChannel(getApplicationContext(),customChannel);

        NotificationsDisplayer.customNotification customNotification = notificationsDisplayer.new customNotification("My first Notification","This is random test text");
        NotificationsDisplayer.displayNotification(getApplicationContext(),customNotification,channel_ID);
       /* Log.d("onStartCommand", "run: CERVISE started");
        new Handler(). postDelayed(new Runnable() {
            @Override
            public void run() {
                String CHANNEL_ID = "0";
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "testing notification", NotificationManager.IMPORTANCE_HIGH);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(notificationChannel);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.chien)
                        .setContentTitle("test notif")
                        .setContentText("Ceci est un exemple de texte à mettre dans une notification. Ce n'est ni utile ni intéressant mais ça permet de tester les limites du contenu d'une notification !");

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getBaseContext());

                notificationManagerCompat.notify(0, builder.build());
            }
        },10000);
        */

//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        Thread.sleep(5000);
//                        Toast.makeText(getApplicationContext(),"thread task",Toast.LENGTH_SHORT).show();
//                    } catch (InterruptedException IntEX) {
//                        IntEX.printStackTrace();
//                    }
//                }
//            }
//        };
//
//        new Thread(runnable).run();
//        final Context currentContext = getApplicationContext();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(5000);
//                    Looper.prepare();
//                    Toast.makeText(getApplicationContext(),"thread task",Toast.LENGTH_SHORT).show();
//                    Log.d(TAG, "run: ");
//                } catch (InterruptedException IntEX) {
//                    IntEX.printStackTrace();
//                }
//            }
//        }).start();
        return super.onStartCommand(intent, flags, startId);
    }
}
