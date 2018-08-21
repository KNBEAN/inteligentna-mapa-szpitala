package bean.pwr.imskamieskiego;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class AuthorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authors);

        TextView authorsText = (TextView) findViewById(R.id.authorsText);
        TextView translateText = (TextView) findViewById(R.id.translateText);

        authorsText.setText(getString(R.string.karol_data) + "\n"
                +getString(R.string.anna_data) + "\n"
                +getString(R.string.kamil_data) + "\n"
                +getString(R.string.piotr_data)
        );

        translateText.setText(getString(R.string.translations_English) + " - " + getString(R.string.translator_English)+"\n"+
                getString(R.string.translations_Russian) + " - " + getString(R.string.translator_Russian)+"\n"+
                getString(R.string.translations_German) + " - " + getString(R.string.translator_German)
        );
    }
}
