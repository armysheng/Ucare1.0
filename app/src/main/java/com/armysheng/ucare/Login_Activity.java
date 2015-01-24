package com.armysheng.ucare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginBtn=(Button)findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new loginBtnClickListener());
    }

    private class loginBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(Login_Activity.this, HomePage.class);
            startActivity(intent);
        }
    }
}
