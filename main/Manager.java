package com.example.software_engineering;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Manager extends AppCompatActivity {

    EditText username, birthday, phone, idnum;
    String infected_username, infected_birthday, infected_phone, infected_idnum;
    Button new_infected;
    Attendee_DBHelper Attendee_DB;
    Attended_Event_DBHelper Attended_Event_DB;
    Hosted_event_DBHelper Hosted_event_DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        username = (EditText)findViewById(R.id.infected_name);
        birthday = (EditText)findViewById(R.id.infected_birthdate);
        phone = (EditText)findViewById(R.id.infected_phone);
        idnum = (EditText)findViewById(R.id.infected_id);
        new_infected = (Button)findViewById(R.id.add_new_infected);

        Attendee_DB = new Attendee_DBHelper(this);
        Attended_Event_DB = new Attended_Event_DBHelper(this);
        Hosted_event_DB = new Hosted_event_DBHelper(this);

        new_infected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                infected_username = username.getText().toString();
                infected_birthday = birthday.getText().toString();
                infected_phone = phone.getText().toString();
                infected_idnum = idnum.getText().toString();

                boolean check_is_infected = Attendee_DB.checkinfo(infected_birthday, infected_phone, infected_idnum);
                if (check_is_infected == true) { //class Attendee_DB的public bool checkInfo(): 檢查個人資訊
                    String p_key = Attendee_DB.get_key(infected_idnum); //class Attendee_DB的public String getKey(String infected_idnum): 得到感染者的資料
                    String[] infected_attended_event = Attended_Event_DB.attended_event_list(p_key);
                    int num = 0;
                    for (int i = 0; i < infected_attended_event.length; i++) {
                        boolean success = Hosted_event_DB.labelinfected(infected_attended_event[i]); //Class hostevent_DB的public bool labelInfected(String p_key): 標記為感染者
                        if (success == true) {
                            num += 1;
                        }
                    }
                    if (num == infected_attended_event.length) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Manager.this);
                        alertDialog.setTitle("警告!!!!!!!!!");
                        String alert = "已經將" + String.valueOf(num) + "個活動新增感染通知";
                        alertDialog.setMessage(alert + "\n");
                        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(getBaseContext(),"確定", Toast.LENGTH_SHORT).show();
                            }
                        });
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Manager.this);
                        alertDialog.setTitle("警告!!!!!!!!!");
                        String alert = "已經將" + String.valueOf(infected_attended_event.length) + "之中的" + String.valueOf(num) + "個活動新增感染通知";
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
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Manager.this);
                    alertDialog.setTitle("警告!!!!!!!!!");
                    String alert = "資料庫中沒有此感染者資料";
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
            }
        });
    }
}