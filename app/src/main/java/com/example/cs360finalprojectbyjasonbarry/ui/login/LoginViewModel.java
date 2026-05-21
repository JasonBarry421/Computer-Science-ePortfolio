package com.example.cs360finalprojectbyjasonbarry.ui.login;

import android.content.Context;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cs360finalprojectbyjasonbarry.data.LoginRepository;
//import com.example.cs360finalprojectbyjasonbarry.data.Result;
import com.example.cs360finalprojectbyjasonbarry.data.model.LoggedInUser;
import com.example.cs360finalprojectbyjasonbarry.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    // Without Phone Number
    public boolean login(String username, String password) {
        return loginRepository.login(username, password);
    }

    // Without Phone Number
    public boolean createAccount(String username, String password, String phoneNumber) {
        return loginRepository.createAccount(username, password, phoneNumber);
    }

    // Without Phone Number
    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // With Phone Number
    public void loginDataChanged(String username, String password, String phoneNumber) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else if (!isPhoneNumberValid(phoneNumber)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_phoneNumber));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    // A placeholder password validation check
    private boolean isPhoneNumberValid(String phoneNumber){
        return phoneNumber != null && phoneNumber.trim().length() == 10;
    }
}