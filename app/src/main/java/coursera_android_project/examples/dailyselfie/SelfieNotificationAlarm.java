package coursera_android_project.examples.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class SelfieNotificationAlarm extends BroadcastReceiver {

    private static final int MY_NOTIFICATION_ID = 1;

    private Intent mNotificationIntent;
    private PendingIntent mPendingIntent;

    private final Uri soundURI = Uri
            .parse("android.resource://course.examples.Alarms.AlarmCreate/"
                    + R.raw.alarm_rooster);

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");

        mNotificationIntent = new Intent(context, MainActivity.class);
        mPendingIntent = PendingIntent.getActivity(context, 0, mNotificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Build notification
        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setTicker("Time for another selfie")
                .setSmallIcon(R.drawable.ic_menu_camera)
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("Take a Selfie")
                .setContentIntent(mPendingIntent)
                .setSound(soundURI);

        // Get NotificationManager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MY_NOTIFICATION_ID, notificationBuilder.build());
    }
}
