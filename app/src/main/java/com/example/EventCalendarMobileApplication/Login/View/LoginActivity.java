package com.example.EventCalendarMobileApplication.Login.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.EventCalendarMobileApplication.Calendar.View.activity_calendar_daily;
import com.example.EventCalendarMobileApplication.R;
import com.example.EventCalendarMobileApplication.Session;
import com.example.EventCalendarMobileApplication.Calendar.View.activity_calendar_month;
import com.example.EventCalendarMobileApplication.Login.Repository.LoginRepository;
import com.example.EventCalendarMobileApplication.databinding.ActivityLoginBinding;
import com.example.EventCalendarMobileApplication.Login.ViewModel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    Button mCreateNewAccountButton;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoginRepository loginRE = new LoginRepository(this);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("Sign-In");

        loginViewModel = new LoginViewModel(loginRE);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;

        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            loginButton.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.authenticateUser(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
            return false;
        });

        loginButton.setOnClickListener(v -> {

            // Transforms Edit Text into Strings for Database Comparison
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Attempts to login with user provided info (if valid)
            try{
                int userID = loginViewModel.authenticateUser(username, password);
                if (userID != -1) {
                    Session.currentUserId = userID;

                    String sessionID = String.valueOf(Session.currentUserId);

                    Intent EnterMonthlyCalendarIntent = getIntent();
                    EnterMonthlyCalendarIntent = new Intent(LoginActivity.this, activity_calendar_month.class);
                    startActivity(EnterMonthlyCalendarIntent);
                } else {
                    // Outputs a message if the information isn't valid
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                    Intent RefreshIntent = getIntent();
                    RefreshIntent = new Intent(LoginActivity.this, LoginActivity.class);
                    startActivity(RefreshIntent);
            }
        });

        mCreateNewAccountButton = findViewById(R.id.createNewAccount);
        mCreateNewAccountButton.setOnClickListener(view -> {
            Intent EnterNewAccountIntent = getIntent();
            EnterNewAccountIntent = new Intent(LoginActivity.this, createNewAccount.class);
            startActivity(EnterNewAccountIntent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.basic_menu, menu);
        return true;
    }
}