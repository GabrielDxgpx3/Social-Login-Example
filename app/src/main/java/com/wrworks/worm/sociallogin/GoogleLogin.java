package com.wrworks.worm.sociallogin;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class GoogleLogin {
    private Activity activity;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInAccount Acount;
    public final int RC_SIGN_IN = 568;
    private OnLoginResult onLoginResult;

    public GoogleLogin(Activity activity){
        this.activity = activity;
    }

    public void Conectar(){

        if(onLoginResult != null){

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
            this.googleSignInClient = GoogleSignIn.getClient(this.activity, gso);
            this.Acount = GoogleSignIn.getLastSignedInAccount(this.activity);

            if(this.Acount == null){
                Intent signInIntent = this.googleSignInClient.getSignInIntent();
                this.activity.startActivityForResult(signInIntent, RC_SIGN_IN);
            }else{
                onLoginResult.onSucesso(this.Acount);
            }

        }else{
            Log.i("GOOGLE", "SET onLoginResult before call Conectar Function");
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        this.handleSignInResult(task);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            onLoginResult.onSucesso(account);
        } catch (ApiException e) {
            onLoginResult.onErro(e);
            Log.i("GOOGLE", "signInResult:failed code=" + e.getStatusCode());
        }

    }

    interface OnLoginResult{
        void onSucesso(GoogleSignInAccount account);
        void onErro(ApiException erro);
    }

    public void setOnLoginResult(OnLoginResult onLoginResult){
        this.onLoginResult = onLoginResult;
    }
}
