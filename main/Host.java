package com.example.software_engineering;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Host extends AppCompatActivity {
    int info_id;
    private ScrollView mScrollView;
    Host_DBHelper Host_DB;
    Hosted_event_DBHelper Hosted_event_DB;
    Attended_Event_DBHelper Attended_Event_DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        info_id = Integer.valueOf(bundle.getString("info_id"));

        Button name = (Button)findViewById(R.id.Host_name_btn);
        Button logout = (Button)findViewById(R.id.Host_logout_btn);
        Button new_event = (Button)findViewById(R.id.New_event_btn);

        Host_DB = new Host_DBHelper(this);
        Hosted_event_DB = new Hosted_event_DBHelper(this);
        Attended_Event_DB = new Attended_Event_DBHelper(this);

        String[] personal_data = Host_DB.get_personal_data(info_id);
        name.setText(personal_data[1]);
        String[] hosted_event_list = Hosted_event_DB.hosted_event(info_id);
        Log.d("event_list", hosted_event_list[0]);


        //hosted event
        LinearLayout layout = findViewById(R.id.Host_event_list);
        mScrollView = findViewById(R.id.Host_scrollView);
        float height = getResources().getDimension(R.dimen.chart_width);
        if(hosted_event_list[0].equals("Haven't host any event or error"))
        {
            LinearLayout new_layout = new LinearLayout(this);
            new_layout.setOrientation(LinearLayout.HORIZONTAL);
            new_layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) height));
            TextView event = new TextView(this);
            event.setText("查詢無主辦活動，趕快舉辦一個八");
            event.setTextSize(20);
            event.setWidth(1200);
            event.setGravity(1);
            new_layout.addView(event);
            layout.addView(new_layout);
        }
        else
        {
            for (int i = 0; i < hosted_event_list.length; i++) {
                LinearLayout new_layout = new LinearLayout(this);
                new_layout.setOrientation(LinearLayout.HORIZONTAL);
                new_layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) height));
                TextView event_date = new TextView(this);
                TextView event_place = new TextView(this);
                String event_data[] = Hosted_event_DB.find_event_data(hosted_event_list[i]);
                //String event_data[] = {Info_id, 日期, 地點, 活動名稱, 主辦單位, max ppl, key, is_infected}
                if (event_data[7].equals("True")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Host.this);
                    alertDialog.setTitle("警告!!!!!!!!!");  //活動名稱
                    String alert = "您在" + event_data[1] + "所主辦的" + event_data[3] + "活動，有人確診感染新冠肺炎，若距離活動日期小於14天，請主動自主隔離並通報1922";
                    alertDialog.setMessage(alert + "\n");
                    alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(getBaseContext(),"確定", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
                event_date.setText(event_data[1]);  //活動日期
                event_place.setText(event_data[2]);  //活動地點
                event_date.setTextSize(20);
                event_place.setTextSize(20);
                event_date.setWidth(440);
                event_date.setHeight(75);
                event_place.setWidth(400);
                event_place.setHeight(75);

                Button button = new Button(this);
                button.setTextSize(20);
                button.setWidth(360);
                button.setHeight(75);
                button.setText(event_data[3]);  //活動名稱
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Host.this);
                        alertDialog.setTitle(event_data[3]);  //活動名稱
                        String alert1 = "舉辦單位: " + event_data[4];
                        String alert2 = "活動日期: " + event_data[1];
                        String alert3 = "活動地點: " + event_data[2];
                        String alert4 = "已參加人數/人數上限: " + String.valueOf(Attended_Event_DB.attended_people(event_data[6])) + " / " + event_data[5];
                        String alert5 = "活動Key: " + event_data[6];
                        alertDialog.setMessage(alert1 + "\n" + alert2 + "\n" + alert3 + "\n" + alert4 + "\n" + alert5);
                        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(getBaseContext(),"確定", Toast.LENGTH_SHORT).show();
                            }
                        });
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }
                });
                new_layout.addView(button);
                new_layout.addView(event_date);
                new_layout.addView(event_place);
                layout.addView(new_layout);
            }
        }

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog =
                        new AlertDialog.Builder(Host.this);
                alertDialog.setTitle("個人資料");
                String alert1 = "姓名: " + personal_data[1];
                String alert2 = "生日: " + personal_data[2];
                String alert3 = "手機號碼: " + personal_data[3];
                String alert4 = "身分證字號: " + personal_data[4];
                String alert5 = "獨立KEY: " + personal_data[5];
                alertDialog.setMessage(alert1 + "\n"  + alert2 +"\n" + alert3 + "\n" + alert4 + "\n" + alert5);
                alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getBaseContext(),"確定", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Host.this);
                dialog.setMessage("登出成功");
                dialog.show();
//              setResult(2,intent);
              finish();
            }
        });
        new_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Host.this, New_event_registor.class);
                Bundle bundle = new Bundle();
                bundle.putString("info_id", Integer.toString(info_id));
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });







        }
}