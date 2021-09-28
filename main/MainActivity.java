package com.example.software_engineering;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText username, password;
    Button btnlogin, btnregistor;
    Login_DBHelper Login_DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText)findViewById(R.id.username1);
        password = (EditText)findViewById(R.id.password1);
        btnlogin = (Button) findViewById(R.id.btnsignin1);
        btnregistor =(Button) findViewById(R.id.btnregistor);

        Login_DB = new Login_DBHelper(this);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                if(user.equals("") || pass.equals("")) //class Login的checkFormat(String username, String password): 檢查輸入格式
                {
                    dialog.setMessage("請輸入帳號密碼");
                    dialog.show();
                }
                else
                {
                    String[] checkuserpass = Login_DB.checkusernamepassword(user, pass); //class LoginDatabase的checkUserPw(String username, String password): 檢查密碼是否正確
                    Log.d("checkuserpass", checkuserpass[0]);
                    if(checkuserpass[0].equals("attendee"))// class Login的public bool checkUserType(): 檢查使用者類別
                    {
                        Toast.makeText(MainActivity.this,"登入成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Attendee.class); // 這裡跳使用者介面
                        Bundle bundle = new Bundle();
                        bundle.putString("info_id", checkuserpass[1]);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else if(checkuserpass[0].equals("host"))
                    {
                        Toast.makeText(MainActivity.this,"登入成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Host.class); // 這裡跳使用者介面
                        Bundle bundle = new Bundle();
                        bundle.putString("info_id", checkuserpass[1]);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else if(checkuserpass[0].equals("manager"))
                    {
                        Toast.makeText(MainActivity.this,"登入成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Manager.class); // 這裡跳使用者介面
                        startActivity(intent);
                    }
                    else
                    {
                        dialog.setMessage("登入失敗，無此帳號密碼");
                        dialog.show();
                    }
                }
            }
        });
        btnregistor.setOnClickListener(new View.OnClickListener() { //class Login的public void register (): 註冊新帳號
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });
    }
}