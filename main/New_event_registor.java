package com.example.software_engineering;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class New_event_registor extends AppCompatActivity {
    EditText eventname,eventdate,eventplace,hostname,total;
    Button insert,dataview;
    Hosted_event_DBHelper Hosted_event_DB;
    int info_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_registor);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        info_id = Integer.valueOf(bundle.getString("info_id"));
        Hosted_event_DB = new Hosted_event_DBHelper(this);

        eventname = (EditText)findViewById(R.id.eventname);
        eventdate = (EditText)findViewById(R.id.eventdate);
        eventplace = (EditText)findViewById(R.id.eventplace);
        hostname = (EditText)findViewById(R.id.hostname);
        total = (EditText)findViewById(R.id.total);
        insert = (Button)findViewById(R.id.add_new_event);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date=eventdate.getText().toString();
                String place=eventplace.getText().toString();
                String ename=eventname.getText().toString();
                String hname=hostname.getText().toString();
                String num=total.getText().toString();

                if(ename.equals("")||date.equals("")||place.equals("")||hname.equals("")||num.equals(""))
                {
                    Toast.makeText(New_event_registor.this,"請全部輸入",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Boolean is_exist =  Hosted_event_DB.checkinfo(ename, hname, num);
                    if(is_exist == true)
                    {
                        Toast.makeText(New_event_registor.this,"該活動已經舉辦過，請重新輸入",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Boolean checkinsertdata = Hosted_event_DB.insertData(String.valueOf(info_id), date, place, ename, hname, num, "False");
                        if(checkinsertdata == true)
                        {
                            Toast.makeText(New_event_registor.this,"成功輸入",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(New_event_registor.this, Host.class); // 這裡跳使用者介面
                            Bundle bundle = new Bundle();
                            bundle.putString("info_id", String.valueOf(info_id));
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(New_event_registor.this,"輸入資料庫失敗",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}