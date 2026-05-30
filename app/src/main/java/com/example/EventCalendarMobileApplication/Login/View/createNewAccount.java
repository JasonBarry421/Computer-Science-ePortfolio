package com.example.EventCalendarMobileApplication.Login.View;

import androidx.core.app.NavUtils;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.EventCalendarMobileApplication.R;
import com.example.EventCalendarMobileApplication.Login.Repository.LoginRepository;
import com.example.EventCalendarMobileApplication.databinding.ActivityCreateNewAccountBinding;
import com.example.EventCalendarMobileApplication.Login.ViewModel.LoginViewModel;

public class createNewAccount extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityCreateNewAccountBinding binding;
    Button mCreateNewAccountButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoginRepository loginRE = new LoginRepository(this);

        binding = ActivityCreateNewAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("Create a New Account");

        loginViewModel = new LoginViewModel(loginRE);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.createNewAccountLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText usernameEditText = binding.username;
        EditText passwordEditText = binding.password;
        EditText phoneNumberEditText = binding.phoneNumber;

        mCreateNewAccountButton = findViewById(R.id.createNewAccountButton);

        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            mCreateNewAccountButton.setEnabled(loginFormState.isDataValid());

            if (loginFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
            if (loginFormState.getPhoneNumberError() != null){
                phoneNumberEditText.setError(getString(loginFormState.getPhoneNumberError()));
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
                        passwordEditText.getText().toString(), phoneNumberEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        phoneNumberEditText.addTextChangedListener(afterTextChangedListener);

        mCreateNewAccountButton.setOnClickListener(view -> {
            // Transforms Edit Text into Strings for Database Comparison
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String phoneNumber = phoneNumberEditText.getText().toString();

            try{
                // Attempts to Create a new Account with user provided info (if valid)
                if (loginRE.createAccount(username, password, phoneNumber) != Boolean.FALSE){
                    // Displays Success Message
                    Toast.makeText(createNewAccount.this, "Account Successfully Created", Toast.LENGTH_LONG).show();

                    // Returns to Login Screen
                    Intent EnterMonthlyCalendarIntent = getIntent();
                    EnterMonthlyCalendarIntent = new Intent(createNewAccount.this, LoginActivity.class);
                    startActivity(EnterMonthlyCalendarIntent);
                } else{
                    // Displays Error Message if Account can't be added to the database
                    //          - Occurs if an account using this information already exists
                    Toast.makeText(createNewAccount.this, "Invalid username, password, or phone number", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e){
                Intent RefreshIntent = getIntent();
                RefreshIntent=new Intent(createNewAccount.this, createNewAccount.class);
                startActivity(RefreshIntent);
            }
        });
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.basic_menu, menu);
        return true;
    }
}