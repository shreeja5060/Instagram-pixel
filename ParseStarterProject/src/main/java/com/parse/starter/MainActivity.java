package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener {
    Boolean signUpModeActive = true;
    TextView loginTextView;
    EditText usernameEditText;
    EditText passwordEditText;
    public void showUserList() {
        Intent intent = new Intent(getApplicationContext(), userListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_DOWN){
            signUpClicked(v);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.loginTextView){
            Button signUpButton = findViewById(R.id.signUpButton);
            if(signUpModeActive){
                signUpModeActive = false;
                signUpButton.setText("Login");
                loginTextView.setText("or,Sign Up");
            }else{
                signUpModeActive = true;
                signUpButton.setText("Sign Up");
                loginTextView.setText("or,Login");
            }
        }else if (v.getId() == R.id.logoImageView || v.getId() == R.id.backgroundLayout){
            InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    public void signUpClicked(View view){

        if(usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")){
            Toast.makeText(this, "A username and password required", Toast.LENGTH_SHORT).show();
        }else{
            if(signUpModeActive) {
                ParseUser user = new ParseUser();
                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());
                user.signUpInBackground(e -> {
                    if (e == null) {
                        Log.i("Sign Up", "Success");
                        showUserList();
                    } else {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
            }else{
                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), (user, e) -> {
                    if(user!=null){
                        Log.i("login","ok");
                        showUserList();
                    }else{
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Instagram");

        loginTextView = findViewById(R.id.loginTextView);
        loginTextView.setOnClickListener(this);
        usernameEditText =findViewById(R.id.usernameEditText);
        passwordEditText =findViewById(R.id.passwordEditText);
        passwordEditText.setOnKeyListener(this);
        ImageView logoImageView =findViewById(R.id.logoImageView);
        RelativeLayout backgroundLayout = findViewById(R.id.backgroundLayout);
        logoImageView.setOnClickListener(this);
        backgroundLayout.setOnClickListener(this);
        if (ParseUser.getCurrentUser() != null) {
            // User is already logged in, start UserListActivity

          showUserList();


        }


        ParseInstallation.getCurrentInstallation().saveInBackground();


    }

}
