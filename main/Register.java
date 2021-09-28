package com.example.software_engineering;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Register extends AppCompatActivity {

    EditText username, password, repassword, email, phone, idnum, addr, birthday;
    Button signup;
    RadioButton host,attendee;
    RadioGroup choose;
    Login_DBHelper Login_DB;
    Host_DBHelper Host_DB;
    Attendee_DBHelper Attendee_DB;
    String type;
    String info_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        repassword = (EditText)findViewById(R.id.repassword);
        birthday = (EditText)findViewById(R.id.birthday);
        email = (EditText)findViewById(R.id.email);
        phone = (EditText)findViewById(R.id.phone);
        idnum = (EditText)findViewById(R.id.idnum);
        addr = (EditText)findViewById(R.id.addr);

        choose = (RadioGroup)findViewById(R.id.choose);

        signup = (Button) findViewById(R.id.btnsignup);

        Login_DB = new Login_DBHelper(this);
        Attendee_DB = new Attendee_DBHelper(this);  // attendee database主辦方資料庫！！！
        Host_DB = new Host_DBHelper(this);  // attendee database主辦方資料庫！！！

        Login_DB.insertData("1" , "admin", "admin", "manager");

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();
                String birth = birthday.getText().toString();
                String mailaddr = email.getText().toString();
                String phonenum = phone.getText().toString();
                String identitynum = idnum.getText().toString();
                String address = addr.getText().toString();
                AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);

                switch (choose.getCheckedRadioButtonId())
                {
                    case R.id.host:
                        type = "host";
                        break;
                    case R.id.attendee:
                        type = "attendee";
                        break;
                }

                if(user.equals("") || pass.equals("") || repass.equals("") || birth.equals("")  //class Register的public bool checkFormat(): 檢查輸入格式
                        || mailaddr.equals("") || phonenum.equals("") || identitynum.equals("") || address.equals("") || type.equals(""))
                {
                    dialog.setMessage("請輸入所有欄位");
                    dialog.show();
                }
                else
                {
                    if(pass.equals(repass) && pass.length() >= 6) //class Register的public bool checkFormat(): 檢查輸入格式
                    {
                        Boolean checkuser = Login_DB.checkusername(mailaddr);//class Register的public bool checkUser(String username): 檢查有無用戶
                        Boolean checkinfo = Attendee_DB.checkinfo(birth,phonenum,identitynum);//class LoginDB的public bool checkUserPw(String username, String password): 檢查密碼是否正確
                        if(checkuser == false && checkinfo == false)
                        {
                            info_id = String.valueOf(Login_DB.getInfo_id()+1);
                            System.out.println(info_id);
                            Boolean insert = Login_DB.insertData(info_id ,mailaddr, pass, type);
                            if (insert == true) //class Register的public bool checkStatus(): 檢查註冊狀態
                            {
                                if(type == "attendee") //class Register的public bool checkUserType(): 檢查使用者類別
                                {
                                    System.out.println("attendee");
                                    Attendee_DB.insertData(info_id, user, birth, phonenum, identitynum);
                                }
                                else if(type == "host")
                                {
                                    System.out.println("host");
                                    Host_DB.insertData(info_id, user, birth, phonenum, identitynum);
                                }
                                dialog.setTitle("註冊成功");
                                dialog.setMessage("請重新登入");
                                dialog.show();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class); //註冊成功重新登入
                                startActivity(intent);
                            }
                            else
                            {
                                dialog.setMessage("註冊失敗");
                                dialog.show();
                            }
                        }
                        else if(checkuser == true)
                        {
                            dialog.setMessage("該用戶已存在"); dialog.show();
                        }
                        else if(checkinfo == true)
                        {
                            dialog.setMessage("資料已註冊過");
                            dialog.show();
                        }
                    }
                    else
                    {
                        dialog.setMessage("密碼輸入錯誤或格式不符合"); dialog.show();
                    }
                }
            }
        });

    }
}