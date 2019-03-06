package com.project.bulletjournalapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LogInActivity extends AppCompatActivity {
    EditText email,password;
    TextView register,forgotPassword,VerifyMail;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        email=findViewById(R.id.LogInEmailET);
        password=findViewById(R.id.LogInPasswordET);
        register=findViewById(R.id.registerNewUserTV);
        forgotPassword=findViewById(R.id.ForgotPasswordTV);
        VerifyMail=findViewById(R.id.VerifyUserTV);
        login=findViewById(R.id.LogInButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogInActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogInActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });
    }
}
