package de.th.topbottomalert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.LinearLayout;

import de.th.topbottomalerter.Alerter;

public class MainActivity extends AppCompatActivity {

    //LinearLayout topBar;
    Button buttonSuccess,buttonInfo,buttonWarning,buttonError,buttonCustomLayout,buttonCustom;
    Button buttonNoneBottom,buttonFocusableBottom,buttonWarningBottom,buttonErrorBottom,buttonCustomBottom,buttonDefaultBottom;
    Button buttonErrorAutoTop,buttonErrorAutoBottom;
    View rootview;
    Activity activity;
    TextView txtYes, txtNo, txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootview = findViewById(android.R.id.content);
        activity = this;

        buttonSuccess = findViewById(R.id.buttonSuccess);
        buttonInfo = findViewById(R.id.buttonInfo);
        buttonWarning = findViewById(R.id.buttonWarning);
        buttonError = findViewById(R.id.buttonError);
        buttonCustom = findViewById(R.id.buttonCustom);
        buttonCustomLayout = findViewById(R.id.buttonCustomLayout);
        buttonNoneBottom = findViewById(R.id.buttonNoneBottom);
        buttonFocusableBottom = findViewById(R.id.buttonFocusableBottom);
        buttonWarningBottom = findViewById(R.id.buttonWarningBottom);
        buttonErrorBottom = findViewById(R.id.buttonErrorBottom);
        buttonDefaultBottom = findViewById(R.id.buttonDefaultBottom);
        buttonCustomBottom = findViewById(R.id.buttonCustomBottom);
        buttonErrorAutoTop = findViewById(R.id.buttonErrorAutoTop);
        buttonErrorAutoBottom = findViewById(R.id.buttonErrorAutoBottom);

        initlistener();
    }

    public void initlistener(){
        buttonSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(rootview, activity,false, Alerter.ICON_SUCCESS, true, Color.parseColor("#03bc53"),"Success","This is a successful message<br><b>BIG</b>", Alerter.TOP).show();
            }
        });

        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(rootview, activity,false, Alerter.ICON_INFO, false, Color.parseColor("#41c4f4"),"Info","This is an info message", Alerter.TOP).show();
            }
        });

        buttonWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(rootview, activity,false, Alerter.ICON_WARNING, true, Color.parseColor("#f4a941"),"Warning","This is a warning message", Alerter.TOP).show();
            }
        });

        buttonError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(rootview, activity,false, Alerter.ICON_ERROR, true, Color.parseColor("#e21e00"),"ERROR","This is an error message", Alerter.TOP).show();
            }
        });

        buttonCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(rootview, activity,false, Alerter.ICON_CUSTOM,true, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryDark),"Custom","This is an custom <b>ICON</b> message<br>BOTTOM", Alerter.TOP);
                Alerter.getInstance().setAlerterIcon(de.th.topbottomalerter.R.string.fas_gas_pump);
                Alerter.getInstance().show();
            }
        });

        buttonCustomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(rootview, activity, Alerter.TOP, R.layout.custom_alert_layout);
                txtYes = Alerter.getInstance().getAlerterView().findViewById(R.id.txt_yes);
                txtNo = Alerter.getInstance().getAlerterView().findViewById(R.id.txt_no);
                txtMessage = Alerter.getInstance().getAlerterView().findViewById(R.id.txt_message);
                txtMessage.setText("is that cool?");
                //Alerter.getInstance().setAlerterIcon(de.th.topbottomalerter.R.string.fas_gas_pump);
                txtYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getBaseContext(),"Yes that is cool :-)", Toast.LENGTH_LONG).show();
                    }
                });
                txtNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getBaseContext(),"no that's not cool :-(", Toast.LENGTH_LONG).show();
                        Alerter.getInstance().dismissAlerter();
                    }
                });

                Alerter.getInstance().show();
            }
        });

        buttonNoneBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(rootview, activity,false, Alerter.ICON_NONE,false, Color.parseColor("#03bc53"),"Success","This is a successful message", Alerter.BOTTOM).show();
            }
        });

        buttonFocusableBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(rootview, activity,true, Alerter.ICON_INFO,true, Color.parseColor("#41c4f4"),"Info","This is an info message<br><b>focusable</b>", Alerter.BOTTOM).show();
            }
        });

        buttonWarningBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(rootview, activity,false, Alerter.ICON_WARNING,false, Color.parseColor("#f4a941"),"Warning","This is a warning message", Alerter.BOTTOM).show();
            }
        });

        buttonErrorBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(rootview, activity,false, Alerter.ICON_ERROR,true, Color.parseColor("#e21e00"),"Error","This is an error message", Alerter.BOTTOM).show();
            }
        });

        buttonDefaultBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(rootview, activity,false, Alerter.ICON_DEFAULT,true, Color.parseColor("#673AB7"),"Default","This is an default message", Alerter.BOTTOM).show();
            }
        });

        buttonCustomBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(rootview, activity,false, Alerter.ICON_CUSTOM,true, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryDark),"Custom","This is an custom <b>ICON</b> message<br>BOTTOM", Alerter.BOTTOM);
                Alerter.getInstance().setAlerterIcon(de.th.topbottomalerter.R.string.fas_gas_pump);
                Alerter.getInstance().show();
            }
        });

        buttonErrorAutoBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(rootview, activity,false, Alerter.ICON_ERROR,true, Color.parseColor("#e21e00"),"ERROR","This is an error message", Alerter.BOTTOM,4000).show();
            }
        });

        buttonErrorAutoTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(rootview, activity,false, Alerter.ICON_ERROR,true, ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark),"ERROR","This is an error message", Alerter.TOP,4000).show();
            }
        });

    }

    private void callBannerBottom(){
        /*
        Alerter.make(rootview,activity, Alerter.BOTTOM, R.layout.banner);
        textView = Alerter.getInstance().getBannerView().findViewById(R.id.status_text);
        relativeLayout = Alerter.getInstance().getBannerView().findViewById(R.id.rlCancel);
        textView.setText("This is text for the banner");
        bannerClickListener();
        Alerter.getInstance().setCustomAnimationStyle(R.style.NotificationAnimationBottom);
        Alerter.getInstance().show();

         */
    }

}