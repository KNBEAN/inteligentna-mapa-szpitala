package bean.pwr.imskamieskiego.GUI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import bean.pwr.imskamieskiego.MapActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);


        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
        finish();
    }
}
