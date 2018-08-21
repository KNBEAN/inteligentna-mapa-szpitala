package bean.pwr.imskamieskiego;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

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

        authorsText.setText(
                getString(R.string.karol_data) + "\n"
                +getString(R.string.anna_data) + "\n"
                +getString(R.string.kamil_data) + "\n"
                +getString(R.string.piotr_data)
        );

        translateText.setText(
                getString(R.string.translations_English) + " - " + getString(R.string.translator_English)+"\n"+
                getString(R.string.translations_Russian) + " - " + getString(R.string.translator_Russian)+"\n"+
                getString(R.string.translations_German) + " - " + getString(R.string.translator_German)
        );

        specifyAndStartAnimation(appInfo,R.anim.scale_into,2000);

        specifyAndStartAnimation(authorsText,R.anim.slide_in_from_right,1000);
        specifyAndStartAnimation(translateText,R.anim.slide_in_from_right,1500);
        specifyAndStartAnimation(thanksText,R.anim.slide_in_from_right,2000);

        specifyAndStartAnimation(authorsTitle,R.anim.fade_in_anim,2000);
        specifyAndStartAnimation(translateTitle,R.anim.fade_in_anim,2000);
        specifyAndStartAnimation(thanksTitle,R.anim.fade_in_anim,2000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_to_left);
    }

    public void specifyAndStartAnimation(View view,int animationID,long duration){
        Animation slideIn = AnimationUtils.loadAnimation(this,animationID);
        slideIn.setDuration(duration);
        view.setAnimation(slideIn);
        view.animate();
    }

    public void layoutInit(){
        authorsText = (TextView) findViewById(R.id.authorsText);
        translateText = (TextView) findViewById(R.id.translateText);
        thanksText = (TextView) findViewById(R.id.thanksText);
        appInfo = (LinearLayout) findViewById(R.id.appInfo);
        authorsTitle = (TextView) findViewById(R.id.authorsTitle);
        translateTitle = (TextView) findViewById(R.id.translateTitle);
        thanksTitle = (TextView) findViewById(R.id.thanksTitle);
    }
}