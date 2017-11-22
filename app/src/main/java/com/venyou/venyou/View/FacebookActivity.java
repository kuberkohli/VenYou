package com.venyou.venyou.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.venyou.venyou.R;

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
            /* make the API call */
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    userId,
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            try {
                                name = response.getJSONObject().getString("name");
                                Bundle userData = new Bundle();
                                userData.putString("name", name);
                                userData.putString("url", url);
                                userData.putString("userId", userId);
                                Intent intent = new Intent(getApplicationContext(), Home.class);
                                intent.putExtras(userData);
                                startActivity(intent);
                            } catch (JSONException e) {
                                Log.e("MYAPP", "unexpected JSON exception", e);
                                // Do something to recover ... or kill the app.
                            }
                        }
                    }
            ).executeAsync();
        }
        else{
            //login process
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    userId = loginResult.getAccessToken().getUserId();
                    url = "https://graph.facebook.com/" + userId + "/picture?type=large";

                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    Log.v("LoginActivity", response.toString());
                                    try {
                                        name = response.getJSONObject().getString("name");
                                        Bundle userData = new Bundle();
                                        userData.putString("name", name);
                                        userData.putString("url", url);
                                        userData.putString("userId", userId);
                                        Intent intent = new Intent(getApplicationContext(), Home.class);
                                        intent.putExtras(userData);
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender,birthday");
                    request.setParameters(parameters);
                    request.executeAsync();

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
