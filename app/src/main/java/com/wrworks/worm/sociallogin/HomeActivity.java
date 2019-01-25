package com.wrworks.worm.sociallogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {
    private TextView txtId;
    private TextView txtNome;
    private TextView txtEmail;
    private ImageView imgLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtId = findViewById(R.id.txt_id);
        txtNome = findViewById(R.id.txt_nome);
        txtEmail = findViewById(R.id.txt_email);
        imgLogin = findViewById(R.id.img_login);

        Intent intent = getIntent();
        if(intent == null){
            finish();
        }

        String json = intent.getStringExtra("dados");
        try {
            JSONObject obj = new JSONObject(json);
            txtId.setText(obj.getString("id"));
            txtNome.setText(obj.getString("name"));
            txtEmail.setText(obj.getString("email"));

            Picasso.get().load(obj.getString("imgURL")).into(imgLogin);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        //getActionBar().setDisplayShowHomeEnabled(true);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
