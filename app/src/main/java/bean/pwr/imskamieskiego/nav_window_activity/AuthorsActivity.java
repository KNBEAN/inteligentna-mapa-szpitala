package bean.pwr.imskamieskiego.nav_window_activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import bean.pwr.imskamieskiego.R;


public class AuthorsActivity extends AppCompatActivity {

    private TextView authorsText;
    private TextView translateText;
    private TextView thanksText;
    private LinearLayout appInfo;
    private TextView authorsTitle;
    private TextView translateTitle;
    private TextView thanksTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authors);
        overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_to_left);

        layoutInit();

        authorsText.setText(getString(R.string.authors));

        translateText.setText(getString(R.string.translations_authors));

        layoutAnimations();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_to_left);
    }

    public void specifyAndStartAnimation(View view, int animationID,long duration){
        Animation animation = AnimationUtils.loadAnimation(this,animationID);
        animation.setDuration(duration);
        view.setAnimation(animation);
        view.animate();
    }

    public void layoutInit(){
        authorsText = findViewById(R.id.authorsText);
        translateText = findViewById(R.id.translateText);
        thanksText = findViewById(R.id.thanksText);
        appInfo = findViewById(R.id.appInfo);
        authorsTitle = findViewById(R.id.authorsTitle);
        translateTitle = findViewById(R.id.translateTitle);
        thanksTitle = findViewById(R.id.thanksTitle);
    }

    public void layoutAnimations(){
        int slideInID = R.anim.slide_in_from_right;
        int fadeInID = R.anim.fade_in_anim;

        specifyAndStartAnimation(appInfo,R.anim.scale_into,2000);

        specifyAndStartAnimation(authorsText,slideInID,1000);
        specifyAndStartAnimation(translateText,slideInID,1500);
        specifyAndStartAnimation(thanksText,slideInID,2000);

        specifyAndStartAnimation(authorsTitle,fadeInID,2000);
        specifyAndStartAnimation(translateTitle,fadeInID,2000);
        specifyAndStartAnimation(thanksTitle,fadeInID,2000);
    }
}
