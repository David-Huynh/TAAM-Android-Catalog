package com.b07project2024.group1;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class Presenter {
    private LoginFragment view;
    public AuthManager model;

    public Presenter(LoginFragment view, AuthManager model) {
        this.view = view;
        this.model = model;
    }

    public void checkInput(User user) {
        if (user.getUser().isEmpty() || user.getPass().isEmpty()) {
            view.displayAlert("Please fill in all fields");
        } else {
            model.checkDB(user, this);
        }
    }

    public void signIn(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            model.login();
            model.getLoginStatus();
            view.displayAlert("Login Successful");
            view.closeFragmentOnLogin();
        } else {
            view.displayAlert("Login Failed");
        }
    }
}