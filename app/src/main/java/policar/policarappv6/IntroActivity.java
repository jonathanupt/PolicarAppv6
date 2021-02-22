package policar.policarappv6;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class IntroActivity extends AppCompatActivity {

    CountDownTimer countTimerIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  getActionBar().hide();


      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        //ActionBar actionBar = getSupportActionBar();
       // actionBar.setDisplayHomeAsUpEnabled(false);

        countTimerIntro = new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                Intent intentMain = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intentMain);
                finish();

            }
        }.start();


    }

}
