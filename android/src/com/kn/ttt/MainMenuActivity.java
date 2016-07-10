package com.kn.ttt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.view.View;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        final Context appContext = getApplicationContext();

        final Button button = (Button) findViewById(R.id.buttonStartGame);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(appContext, AndroidLauncher.class);
                startActivity(intent);
            }
        });
    }



}
