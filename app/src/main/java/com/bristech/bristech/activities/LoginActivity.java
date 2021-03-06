package com.bristech.bristech.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bristech.bristech.R;
import com.bristech.bristech.entities.User;
import com.bristech.bristech.fragments.EventsFragment;
import com.bristech.bristech.utils.EventUtils;
import com.bristech.bristech.utils.LoginUtils;
import com.bristech.bristech.utils.UserUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.Collections;

import static android.provider.ContactsContract.Intents.Insert.EMAIL;


public class LoginActivity extends AppCompatActivity implements LoginUtils.AuthenticationCallback {


    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int GOOGLE_SIGN_IN = 100;

    //views
    private EditText mEmailField;
    private EditText mPasswordField;
    private TextView mCreateAccTextView;
    private Button mGoogleSignInButton;
    private Button mFacebookSignInButton;
    private Button mEmailSignInButton;
    private Button mLogout;

    private CallbackManager mCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build();

        mLogout = findViewById(R.id.btn_logout);
        mEmailField = findViewById(R.id.et_username);
        mPasswordField = findViewById(R.id.et_password);
        mEmailSignInButton = findViewById(R.id.bt_sign_in);
        mGoogleSignInButton = findViewById(R.id.btn_google_sign_in);
        mCreateAccTextView = findViewById(R.id.tv_create_account);
        mFacebookSignInButton = findViewById(R.id.btn_facebook_sign_in);

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mCallbackManager = CallbackManager.Factory.create();

        Toolbar toolbar = findViewById(R.id.login_toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back_black);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loginBackBtnPressed();
                onBackPressed();
            }
        });

        updateButtonListeners();
    }

    private void loginBackBtnPressed() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    private void facebookSignIn() {
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Collections.singletonList(EMAIL));
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        LoginUtils.handleFacebookToken(loginResult.getAccessToken(), LoginActivity.this);
                    }

                    @Override
                    public void onCancel() {
                        //TODO Pass activity to toast
                        // Toast.makeText(this, "Facebook login cancelled", Toast.LENGTH_LONG).show();
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        //TODO Pass activity to toast
                        // Toast.makeText(this, "Error logging in with Facebook", Toast.LENGTH_LONG).show();
                        // App code
                    }
                });
    }

    private void emailSignIn() {
        hideKeyboard();

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
            LoginUtils.signInWithEmail(email, password, this);
        else
            Snackbar
                    .make(findViewById(R.id.login_coordinator),
                            "Please enter email and password"
                            , Snackbar.LENGTH_LONG)
                    .show();
    }

    private void createAccount() {
        Intent createAccountIntent = new Intent(this, createAccount.class);
        startActivity(createAccountIntent);
    }

    private void updateButtonListeners() {
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailSignIn();
            }
        });

        mGoogleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });

        mFacebookSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookSignIn();
            }
        });

        mCreateAccTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // test button
                LoginUtils.signOut();
                Snackbar
                        .make(findViewById(R.id.login_coordinator), "Logout successful", Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GOOGLE_SIGN_IN:
                LoginUtils.signInWithGoogle(data, this);
                break;
            default:
                LoginUtils.signInWithFacebook(mCallbackManager, requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        try {
            AuthResult r = task.getResult(FirebaseException.class);
            showSnackbar("Successful login");
            UserUtils.getUser(new UserUtils.UserCallback<User>() {
                @Override
                public void onComplete(User user) {
                    // TODO This is the callback function for getting the user from our servers, do something here
                    // TODO or use the function below
                    userHasLoggedIn(user);
                }
            });

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 900);   //1 seconds
        } catch (FirebaseAuthUserCollisionException e) {
            showSnackbar("User already exists with another provider");
        } catch (FirebaseAuthInvalidCredentialsException e) {
            showSnackbar("Please enter a valid email");
        } catch (FirebaseAuthInvalidUserException e) {
            showSnackbar("Account does not exist");
        } catch (FirebaseException e) {
            showSnackbar("An error occurred");
            e.printStackTrace();
        }
    }

    private void showSnackbar(String message) {
        Snackbar
                .make(findViewById(R.id.login_coordinator),
                        message
                        , Snackbar.LENGTH_LONG)
                .show();
    }

    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputManager != null && getCurrentFocus() != null)
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void userHasLoggedIn(User user){
        // TODO Do something with the user.. like save it to sharedPreferences
        User.currentUser = user;
    }
}
