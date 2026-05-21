package com.example.cs360finalprojectbyjasonbarry.ui.login;

import android.Manifest;
import android.app.Activity;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cs360finalprojectbyjasonbarry.R;

import com.example.cs360finalprojectbyjasonbarry.data.LoginRepository;
import com.example.cs360finalprojectbyjasonbarry.databinding.ActivityCreateNewAccountBinding;

import java.util.Random;

public class createNewAccount extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityCreateNewAccountBinding binding;
    public static String mCode;
    Button mCreateNewAccountButton;
    public EditText usernameEditText;
    public EditText passwordEditText;
    public EditText phoneNumberEditText;
    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoginRepository loginRE = new LoginRepository(this);

        binding = ActivityCreateNewAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("Create a New Account");

        loginViewModel = new LoginViewModel(loginRE);

        int sizeOfString = 5;
        mCode = getRandomString(sizeOfString);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.createNewAccountLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText usernameEditText = binding.username;
        EditText passwordEditText = binding.password;
        EditText phoneNumberEditText = binding.phoneNumber;

        mCreateNewAccountButton = findViewById(R.id.createNewAccountButton);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
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
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
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
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.createAccount(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(), phoneNumberEditText.getText().toString());
                }
                return false;
            }
        });

        mCreateNewAccountButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            // Transforms Edit Text into Strings for Database Comparison
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String phoneNumber = phoneNumberEditText.getText().toString();


            try{
                // Attempts to Create a new Account with user provided info (if valid)
                if (loginViewModel.createAccount(username, password, phoneNumber)) {
                    // If Permission to send Texts not Granted
                    if (ContextCompat.checkSelfPermission(createNewAccount.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        // Requests Permission
                        ActivityCompat.requestPermissions(createNewAccount.this, new String[]{Manifest.permission.SEND_SMS}, 0);
                    }
                    // If Permission to send Texts is Granted
                    else {
                        // Sends Text
                        sendSMS();

                        // Enters sms_activity Screen
                        Intent EnterSMSIntent = getIntent();
                        EnterSMSIntent = new Intent(createNewAccount.this, sms_activity.class);
                        startActivity(EnterSMSIntent);
                    }
                }
            } catch (Exception e){
                Intent RefreshIntent = getIntent();
                RefreshIntent=new Intent(createNewAccount.this, createNewAccount.class);
                startActivity(RefreshIntent);
            }
        }

        private void sendSMS() {
            String phoneNumber = "+1" + String.valueOf(phoneNumberEditText.getText());
            String message = "This is an SMS sent to number: " + phoneNumber + "\n\nThe Code is: " + mCode;

            try{
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);

                Toast.makeText(createNewAccount.this, "SMS sent to: " + phoneNumber, Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Toast.makeText(createNewAccount.this, "SMS failed to send", Toast.LENGTH_SHORT).show();
            }
        }});
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

    // Ensure SMS Code is Random and Uppercase
    private static String getRandomString (final int sizeOfString){
        final Random random = new Random();
        final StringBuilder stringBuilder = new StringBuilder(sizeOfString);
        for (int i = 0; i < sizeOfString; ++i){
            stringBuilder.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        }
        return stringBuilder.toString().toUpperCase();
    }
}