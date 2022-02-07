package com.example.calendarandmapapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.calendarandmapapp.UserRepository;
import com.example.calendarandmapapp.models.User;

public class UserViewModel extends ViewModel {
    UserRepository repository;
    MutableLiveData<User> user = new MutableLiveData<>();
    MutableLiveData<Boolean> emailError = new MutableLiveData<>();
    MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public UserViewModel(){
        errorMessage.setValue("");
        repository = new UserRepository();
        repository.setAuthStateChangedListener(user -> {
            this.user.postValue(user);
        });
    }
    public MutableLiveData<User> getUser() {
        return user;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void signIn(String email, String password) {
        errorMessage.postValue(null);
        if (email == null || email.isEmpty()) {
            errorMessage.postValue("Email cannot be empty");
            return;
        }
        if (password == null || password.isEmpty()) {
            errorMessage.postValue("Password cannot be empty");
            return;
        }

        repository.signIn(
                email,
                password,
                e -> errorMessage.postValue(e.getMessage())
        );
    }

    public void signUp(String email, String emailVerification, String password, String passwordVerification) {
        errorMessage.postValue(null);
        if (email == null || email.isEmpty()) {
            errorMessage.postValue("Email cannot be empty");
            return;
        }
        if(!email.equals(emailVerification)){
            errorMessage.postValue("Email and email verification fields must match");
            return;
        }
        if (password == null || password.isEmpty()) {
            errorMessage.postValue("Password cannot be empty");
            return;
        }
        if(password.length()<8){
            errorMessage.postValue("Password must be at least 8 characters long");
            return;
        }
        if(!password.equals(passwordVerification)){
            errorMessage.postValue("Password and password verification fields must match");
            return;
        }
        repository.signUp(
                email,
                password,
                e -> errorMessage.postValue(e.getMessage())
        );
    }

    public void logout() {
        repository.logout();
        user.setValue(null);
    };


}
