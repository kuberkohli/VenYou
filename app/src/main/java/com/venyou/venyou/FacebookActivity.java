package com.venyou.venyou;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;
import com.squareup.picasso.Picasso;

public class FacebookActivity extends AppCompatActivity {

    private TextView login_status;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ProfilePictureView profilePictureView;
    private ImageView propic;
    private String userId,name,email,birthday,url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        login_status = (TextView) findViewById(R.id.login_status);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");


        if (AccessToken.getCurrentAccessToken() != null) {
            userId = AccessToken.getCurrentAccessToken().getUserId();
            url = "https://graph.facebook.com/" + userId + "/picture?type=large";
            name = Profile.getCurrentProfile().getName();
            Bundle userData = new Bundle();
            userData.putString("name", name);
            userData.putString("url", url);
            userData.putString("userId", userId);
            Intent intent = new Intent(getApplicationContext(), Home.class);
            intent.putExtras(userData);
            startActivity(intent);
        }
        else{
            //login process
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    login_status.setText("Login Success :" + loginResult.getAccessToken().getUserId());
                    userId = loginResult.getAccessToken().getUserId();
                    url = "https://graph.facebook.com/" + userId + "/picture?type=large";

                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    Log.v("LoginActivity", response.toString());

                                    // Application code
                                    try {
                                        login_status.setText("Welcome ! "+object.getString("name"));
                                        email = object.getString("email");
                                        name = object.getString("name");
                                        birthday = object.getString("birthday"); // 01/31/1980 format
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender,birthday");
                    request.setParameters(parameters);
                    request.executeAsync();

                    Bundle userData = new Bundle();
                    userData.putString("name", name);
                    userData.putString("url", url);
                    userData.putString("userId", userId);
                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    intent.putExtras(userData);
                    startActivity(intent);

                }

                @Override
                public void onCancel() {
                    login_status.setText("Login Cancelled");
                }

                @Override
                public void onError(FacebookException error) {
                    login_status.setText("Login Error :" + error.getMessage());
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
