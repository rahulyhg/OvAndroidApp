package ovenues.com.ovenue.FirbaseContents;



/**
        * Created by Jay on 08/06/2016.
        */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ovenues.com.ovenue.NotificationsScreen;
import ovenues.com.ovenue.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";



    Bitmap bm = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_notification);

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        //Log.d(TAG, "Message: " + remoteMessage.toString());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        sendNotification(remoteMessage.getNotification().getBody().toString()
                ,remoteMessage.getNotification().getTitle().toString());

       /* String text_notification=remoteMessage.getData().get("message");
        String url_notification=remoteMessage.getData().get("url");
        if(url_notification.equalsIgnoreCase("null")){
            url_notification="";
        }
        sendNotification(text_notification+"\n"+url_notification);*/

    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody,String title) {

        final  String GROUP_KEY_EMAILS = "group_key_lotteryinvitation";
        Log.v("mesage",messageBody);
        Intent intent = new Intent(this, NotificationsScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        final NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(MyFirebaseMessagingService.this);





        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            // define sound URI, the sound to be played when there's a notification
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);





            Bitmap bm = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_notification);

            builder
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setTicker("New notification from Ovenues.")
                    .setLargeIcon(bm)
                    .setSound(soundUri)
                    .setAutoCancel(true)
                    .setLights(0xFFFF0000, 500, 500) //setLights (int argb, int onMs, int offMs)
                    //.setContentIntent(pendingIntent)
                    //.addAction(android.R.drawable.ic_menu_close_clear_cancel, coutnt_down, pendingIntent)
                    .setContentIntent(pendingIntent);



            Notification notification = builder.getNotification();

            notificationManager.notify(R.drawable.ic_notification, notification);






        } else {
            // Lollipop specific setColor method goes here.

            // define sound URI, the sound to be played when there's a notification
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



            Bitmap bm = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_notification);

            builder
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setTicker("New notification from Ovenues.")
                    .setLargeIcon(bm)
                    .setColor(Color.parseColor("#F9A059"))
                    .setSound(soundUri)
                    .setAutoCancel(true)
                    .setGroup(GROUP_KEY_EMAILS)
                    .setGroupSummary(true)
                    .setLights(0xFFFF0000, 500, 500) //setLights (int argb, int onMs, int offMs)*/
                    //.setContentIntent(pendingIntent)
                    //.addAction(android.R.drawable.ic_menu_close_clear_cancel, coutnt_down, pendingIntent)
                    /*.addAction (R.drawable.ic_stat_snooze,
                            getString(R.string.snooze), piSnooze);*/
                    .setContentIntent(pendingIntent);




            Notification notification = builder.getNotification();


            notificationManager.notify(R.drawable.ic_notification, notification);


        }



    }


}