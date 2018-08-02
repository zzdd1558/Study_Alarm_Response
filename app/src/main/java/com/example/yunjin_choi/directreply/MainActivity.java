package com.example.yunjin_choi.directreply;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button sendNotification;
    private static int notificationId = 101;
    private static String KEY_REPLY = "KEY_REPLY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = this.getIntent();

        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);

        if (remoteInput != null){
            TextView myTextView = findViewById(R.id.textView);
            String inputString = remoteInput.getCharSequence(KEY_REPLY).toString();

            myTextView.setText(inputString);

            // 알람에 대한 응답 처리할때 진행하면 되는 코드
            Notification repliedNotification = new Notification.Builder(this)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentText("전송 완료!!")
                    .build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(notificationId , repliedNotification);
            //////////////////
        }

        sendNotification = findViewById(R.id.sendNotification);
        sendNotification.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendNotification:
                sendNotification();
                break;
        }
    }


    public void sendNotification() {
        String replyLabel = "Reply를 입력해보자";

        // android.support.v4.app.RemoteInput이 들어가야 한다.
        // Input Text같은 기능
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_REPLY)
                //Hint같은 기능 Web에서는 PlaceHolder
                .setLabel(replyLabel)
                .build();

        Intent resultIntent = new Intent(this, MainActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        //public Builder(int icon, CharSequence title, PendingIntent intent) {
        // 세번째 Parameter로 PendingIntent를 전달한다. 클릭시 이동할 Activity 및
        // 인텐트를 통해 remoteInput에서 입력한 값을 전달한다.
        // Reply라는 버튼이 나오고 클릭시 연결될 RemoteInput등록
        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(android.R.drawable.ic_dialog_info, "Reply", resultPendingIntent)
                .addRemoteInput(remoteInput)
                .build();

        //Notify 내용.
        Notification newMessageNotification = new NotificationCompat.Builder(this)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Notification Reply Test")
                .setContentText("전달할 메세지를 입력하세요")
                .addAction(replyAction).build();

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId , newMessageNotification);

    }
}
