package com.app.devrah.FireBase_Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.app.devrah.R;
import com.app.devrah.Views.MainActivity;
import com.app.devrah.Views.MyCards.MyCardsActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static int NOTIFY_ME_ID = 0;
   // private LocalBroadcastManager broadcastManager;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
       // broadcastManager = LocalBroadcastManager.getInstance(this);
         showNotification(remoteMessage.getData().get(""), remoteMessage.getData().get("body"),remoteMessage.getData().get("important"));

           //showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),"important");

        // Volley_helper volley_helper = new Volley_helper();
        Intent intent = null;

        if (remoteMessage.getNotification().getTitle().equals("mycards") && MyCardsActivity.isMyCardsActive) {

            String[] id = remoteMessage.getNotification().getBody().split("%");
           /* intent = new Intent(this, MyCardsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //overridePendingTransition(0, 0);
            intent.putExtra("mycards", "mycards");
            intent.putExtra("id", id[1]);
            startActivity(intent);*/
           // Bundle extras = intent.getExtras();
            Intent i = new Intent("broadCastName");
            // Data you need to pass to activity
            i.putExtra("message",id[1]);

            sendBroadcast(i);
           // broadcastManager.sendBroadcast(intent);
           // singletonclass.getInstance().getSingletonModel().textData ="Your data goes here";

        }


        /*    SharedPreferences preferences = getSharedPreferences("Notifications",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if (remoteMessage.getNotification().getTitle().startsWith("Friend Request"))
        {
            editor.putString("friend_request","active");

            editor.apply();

        }
*/

    }

    //This method is only generating push notification
    private void sendNotification(String title, String messageBody, String type) {
        Intent intent = null;

        if (title.startsWith("Friend Request")) {
            //      intent = new Intent(this, Friend_Request.class);

        } else if (title.startsWith("message")) {
            // do message
        }
        intent.putExtra("type", type);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo_devrh)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFY_ME_ID, notificationBuilder.build());
    }

    private void displayNotification() {
        Log.i("Start", "notification");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

   /* Invoking the default notification service */
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setContentTitle("New Message");
        mBuilder.setContentText("You've received new message.");
        mBuilder.setTicker("New Message Alert!");
        // mBuilder.setSmallIcon(R.drawable.woman);

   /* Increase notification number every time a new notification arrives */
        mBuilder.setNumber(++NOTIFY_ME_ID);

   /* Add Big View Specific Configuration */
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        String[] events = new String[6];
        events[0] = new String("This is first line....");
        events[1] = new String("This is second line...");
        events[2] = new String("This is third line...");
        events[3] = new String("This is 4th line...");
        events[4] = new String("This is 5th line...");
        events[5] = new String("This is 6th line...");

        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle("Big Title Details:");

        // Moves events into the big view
        for (int i = 0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }

        mBuilder.setStyle(inboxStyle);

   /* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);

   /* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

   /* notificationID allows you to update the notification later on. */
        notificationManager.notify(NOTIFY_ME_ID, mBuilder.build());
    }

    private void showNotification(String title, String body, String type) {

//        int icon = R.mipmap.ic_launcher;

        int mNotificationId = NOTIFY_ME_ID;
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("type", type);

// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
//
//        Uri path = Uri.parse("android.resource://"+mContext.getPackageName()+"/"+R.raw.one);
//        Uri sound = Uri.parse("file:///android_asset/one.mp3");

        // NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        String tickerText = "New Message! : " + body;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                getApplicationContext());
        Notification notification = null;
        notification = mBuilder.setTicker("New Message!")
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(body)
//                    .setDefaults(Notification.DEFAULT_ALL)
                .setTicker(tickerText)
                .setPriority(Notification.PRIORITY_HIGH)
                .setWhen(System.currentTimeMillis())
                .setStyle(new NotificationCompat.InboxStyle())
                // .setStyle(inboxStyle)
                .setContentIntent(resultPendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                    .setSound(sound)
//                    .setLargeIcon(anImage)
                .setSmallIcon(R.drawable.logo_devrh)
                .build();

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(mNotificationId, notification);


    }


}
