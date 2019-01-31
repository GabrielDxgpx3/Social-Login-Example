package com.wrworks.worm.sociallogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.facebook.FacebookException;

import com.facebook.GraphResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;


import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private SignInButton btnLoginGoogle;
    private Button btnLoginFacebook;
    private FacebookLogin fbConnect;
    private GoogleLogin googleLogin;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLoginFacebook  = (Button) findViewById(R.id.btn_login_facebook);
        btnLoginGoogle = findViewById(R.id.btn_login_google);
        fbConnect = new FacebookLogin(MainActivity.this);
        googleLogin = new GoogleLogin(MainActivity.this);

        intent = new Intent(MainActivity.this, HomeActivity.class);

        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleLogin();
            }
        });


        btnLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FacebookLogin();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == googleLogin.RC_SIGN_IN) {
            googleLogin.onActivityResult(requestCode, resultCode, data);
        }else{
            fbConnect.onActivityResult(requestCode, resultCode, data);
        }


    }

    private void GoogleLogin(){


        googleLogin.setOnLoginResult(new GoogleLogin.OnLoginResult() {
            @Override
            public void onSucesso(GoogleSignInAccount account) {
                JSONObject object = new JSONObject();
                try {
                    object.put("name", account.getDisplayName());
                    object.put("email", account.getEmail());
                    object.put("id", account.getId());
                    object.put("imgURL", account.getPhotoUrl());
                    object.put("type", "GOOGLE");

                    intent.putExtra("dados", object.toString());
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onErro(ApiException erro) {

            }
        });

        googleLogin.Conectar();
    }

    private void FacebookLogin(){

        fbConnect.setOnLoginResult(new FacebookLogin.OnLoginResult() {
            @Override
            public void onSucesso(JSONObject object, GraphResponse response) {

                try {
                    object.put("type", "FACEBOOK");;
                    object.put("imgURL", "http://graph.facebook.com/"+ object.getString("id") + "/picture?type=large");

                    intent.putExtra("dados", object.toString());
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErro(FacebookException erro) {
                Log.i("DADOS", erro.toString());
            }

            @Override
            public void onCancel() {
                Log.i("DADOS","acao cancelada");
            }
        });

        fbConnect.Connect();

    }



}
