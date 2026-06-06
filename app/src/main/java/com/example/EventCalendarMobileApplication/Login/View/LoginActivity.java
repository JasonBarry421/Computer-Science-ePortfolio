package com.example.EventCalendarMobileApplication.Login.View;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.EventCalendarMobileApplication.R;
import com.example.EventCalendarMobileApplication.Session;
import com.example.EventCalendarMobileApplication.Calendar.View.activity_calendar_month;
import com.example.EventCalendarMobileApplication.databinding.ActivityLoginBinding;
import com.example.EventCalendarMobileApplication.Login.ViewModel.LoginViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    Button mCreateNewAccountButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("Sign-In");

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

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
                        passwordEditText.getText().toString(),
                        success -> {

                            if (success) {
                                // login success logic
                                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
            return true;
        });

        loginButton.setOnClickListener(v -> {

            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            try {
                loginViewModel.authenticateUser(username, password, success -> {

                    if (success) {

                        // Sets the Current User ID (to ensure Events are specifically associated with that User)
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            Session.currentUserId = user.getUid();
                        }

                        // Displays Success Message
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

                        // Enters to Monthly Calendar Screen
                        startActivity(new Intent(this, activity_calendar_month.class));
                        finish();

                    } else {
                        // Outputs a message if the information isn't valid
                        Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                // Refreshes Login Screen
                Intent RefreshIntent = new Intent(LoginActivity.this, LoginActivity.class);
                startActivity(RefreshIntent);
            }
        });

        mCreateNewAccountButton = findViewById(R.id.createNewAccount);
        mCreateNewAccountButton.setOnClickListener(view -> {
            Intent EnterNewAccountIntent = new Intent(LoginActivity.this, createNewAccount.class);
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