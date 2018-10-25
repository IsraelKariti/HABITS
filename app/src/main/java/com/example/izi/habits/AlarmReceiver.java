package com.example.izi.habits;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Calendar;

import static com.example.izi.habits.NewDayZeroedHabits.newDayZeroedHabits;

public class AlarmReceiver extends BroadcastReceiver {
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    @Override
    public void onReceive(Context context, Intent intent) {
        // boom notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "myid")
                .setSmallIcon(R.drawable.ic_toys_black_24dp)
                .setContentTitle("MyTitle - ALARM RECEIVER - set all habits to zero value")
                .setContentText("MyContent")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, mBuilder.build());

        newDayZeroedHabits(context);

        // WHAT
        Intent intent2 = new Intent(context, AlarmReceiver.class);
        intent2.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent2, 0);

        //  WHEN
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // combine the when-to-do and what-to-do
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }
}
