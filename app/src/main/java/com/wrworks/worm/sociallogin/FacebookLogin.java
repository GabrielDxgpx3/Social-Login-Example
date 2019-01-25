package com.wrworks.worm.sociallogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;

public class FacebookLogin {
    private Activity activity;
    private LoginManager loginManager;
    private CallbackManager callbackManager;
    private Collection<String> permissions = Arrays.asList("public_profile", "email");
    private OnLoginResult onLoginResult;

    public FacebookLogin(Activity activity){
        this.activity = activity;
    }

    public void Connect(){

        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        loginManager.logInWithReadPermissions(this.activity, this.permissions);

        loginManager.registerCallback(this.callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(loginResult != null){
                    callGraph(loginResult.getAccessToken());
                }

            }

            @Override
            public void onCancel() {
                onLoginResult.onCancel();
            }

            @Override
            public void onError(FacebookException error) {
                onLoginResult.onErro(error);
            }
        });

    }

    private void callGraph(AccessToken token){
        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                onLoginResult.onSucesso(object, response);
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, email, first_name, gender, last_name, link,name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    interface OnLoginResult{
        void onSucesso(JSONObject object, GraphResponse response);
        void onErro(FacebookException erro);
        void onCancel();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(callbackManager != null){
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setOnLoginResult(OnLoginResult onLoginResult){
        this.onLoginResult = onLoginResult;
    }

}
