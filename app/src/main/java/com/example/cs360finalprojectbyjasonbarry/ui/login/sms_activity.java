package com.example.cs360finalprojectbyjasonbarry.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cs360finalprojectbyjasonbarry.R;
import com.example.cs360finalprojectbyjasonbarry.activity_calendar_month;
import com.example.cs360finalprojectbyjasonbarry.databinding.ActivitySmsBinding;

public class sms_activity extends AppCompatActivity {

    private ActivitySmsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivitySmsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final EditText mSmsCodeText = binding.smsCode;

        setTitle("Enter SMS Code");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.smsScreen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Sets Enter Button for Reference
        Button mEnterButton = findViewById(R.id.smsEnterCodeButton);

        // Listens for Button Press
        mEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mTextAsString = mSmsCodeText.getText().toString();
                String myCode = createNewAccount.mCode;

                if (mTextAsString.equals(myCode)) {
                    Intent EnterCalendarIntent = getIntent();
                    EnterCalendarIntent = new Intent(sms_activity.this, activity_calendar_month.class);
                    startActivity(EnterCalendarIntent);
                }
                else{
                    Toast.makeText(sms_activity.this, "Entered Code is Incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.basic_menu, menu);
        return true;
    }
}