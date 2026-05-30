package com.example.EventCalendarMobileApplication.Login.ViewModel;

import android.util.Patterns;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.EventCalendarMobileApplication.Login.Repository.LoginRepository;
import com.example.EventCalendarMobileApplication.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private LoginRepository loginRepository;

    public LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    // Authenticate User Information to Log In to their Account
    public int authenticateUser(String username, String password) {
        return loginRepository.authenticateUser(username, password);
    }

    // Without Phone Number (for existing account login)
    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // With Phone Number (for new account login)
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

    // A placeholder Phone Number validation check
    private boolean isPhoneNumberValid(String phoneNumber){
        return phoneNumber != null && phoneNumber.trim().length() == 10;
    }
}