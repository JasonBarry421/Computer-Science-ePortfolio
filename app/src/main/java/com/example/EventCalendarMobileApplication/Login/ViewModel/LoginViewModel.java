package com.example.EventCalendarMobileApplication.Login.ViewModel;

import android.util.Patterns;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.EventCalendarMobileApplication.Login.Repository.LoginRepository;
import com.example.EventCalendarMobileApplication.R;

public class LoginViewModel extends ViewModel {

    private final LoginRepository repository = new LoginRepository();

    private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LoginViewModel() {
    }

    // ---------------------------
    // LOGIN
    // ---------------------------
    public void authenticateUser(String username, String password, LoginRepository.AuthCallback callback) {
        repository.authenticateUser(username, password, callback);
    }

    // ---------------------------
    // CREATE ACCOUNT
    // ---------------------------
    public void createAccount(String username, String password, String phone, LoginRepository.AuthCallback callback) {
        repository.createAccount(username, password, phone, callback);
    }

    // ---------------------------
    // FORM VALIDATION (LOGIN)
    // ---------------------------
    public void loginDataChanged(String username, String password) {

        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        }
        else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        }
        else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // ---------------------------
    // FORM VALIDATION (SIGN UP)
    // ---------------------------
    public void signupDataChanged(String username, String password, String phoneNumber) {

        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        }
        else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        }
        else if (!isPhoneNumberValid(phoneNumber)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_phoneNumber));
        }
        else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // ---------------------------
    // VALIDATION HELPERS
    // ---------------------------

    // A placeholder username validation check
    private boolean isUserNameValid(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    // A placeholder Phone Number validation check
    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber != null && phoneNumber.trim().length() == 10;
    }
}