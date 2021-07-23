package de.th.topbottomalert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;

import android.content.Context;
import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.th.topbottomalerter.Alerter;
import de.th.topbottomalerter.AlerterDialog;

public class MainActivity extends AppCompatActivity {

    Button buttonSuccess,buttonInfo,buttonWarning,buttonError,buttonCustomLayout,buttonCustom;
    Button buttonNoneBottom,buttonFocusableBottom,buttonWarningBottom,buttonErrorBottom,buttonCustomBottom,buttonDefaultBottom;
    Button buttonErrorAutoTop,buttonErrorAutoBottom;
    Button buttonAlerterDialogBottom,buttonAlerterDialogCenter,buttonAlerterDialogTop;
    View rootview;
    Context context;
    AlerterDialog alerterDialog;
    TextView txtYes, txtNo, txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootview = findViewById(android.R.id.content);
        context = this;

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
        buttonAlerterDialogBottom = findViewById(R.id.buttonAlerterDialogBottom);
        buttonAlerterDialogCenter = findViewById(R.id.buttonAlerterDialogCenter);
        buttonAlerterDialogTop = findViewById(R.id.buttonAlerterDialogTop);

        initlistener();
    }

    public void initlistener(){
        buttonSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(context,false, Alerter.ICON_SUCCESS, true, ContextCompat.getColor(getApplicationContext(),R.color.colorSuccess),"Success","This is a successful message<br><b>BIG</b>", Alerter.TOP).show();
            }
        });

        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(context,false, Alerter.ICON_INFO, false, ContextCompat.getColor(getApplicationContext(),R.color.colorInfo),"Info","This is an info message", Alerter.TOP).show();
            }
        });

        buttonWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(context,false, Alerter.ICON_WARNING, true, ContextCompat.getColor(getApplicationContext(),R.color.colorWarning),"Warning","This is a warning message", Alerter.TOP).show();
            }
        });

        buttonError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(context,false, Alerter.ICON_ERROR, true, ContextCompat.getColor(getApplicationContext(),R.color.colorError),"ERROR","This is an error message", Alerter.TOP).show();
            }
        });

        buttonCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(context,false, Alerter.ICON_CUSTOM,true, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryDark),"Custom","This is an custom <b>ICON</b> message<br>BOTTOM", Alerter.TOP);
                Alerter.getInstance().setAlerterIcon(de.th.topbottomalerter.R.string.fas_gas_pump);
                Alerter.getInstance().show();
            }
        });

        buttonCustomLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                Alerter.make(context, Alerter.TOP, R.layout.custom_alert_layout);
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
                Alerter.make(context,false, Alerter.ICON_NONE,false, ContextCompat.getColor(getApplicationContext(),R.color.colorSuccess),"Success","This is a successful message", Alerter.BOTTOM).show();
            }
        });

        buttonFocusableBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(context,true, Alerter.ICON_INFO,true, ContextCompat.getColor(getApplicationContext(),R.color.colorInfo),"Info","This is an info message<br><b>focusable</b>", Alerter.BOTTOM).show();
            }
        });

        buttonWarningBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(context,false, Alerter.ICON_WARNING,false, ContextCompat.getColor(getApplicationContext(),R.color.colorWarning),"Warning","This is a warning message", Alerter.BOTTOM).show();
            }
        });

        buttonErrorBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(context,false, Alerter.ICON_ERROR,true, ContextCompat.getColor(getApplicationContext(),R.color.colorError),"Error","This is an error message", Alerter.BOTTOM).show();
            }
        });

        buttonDefaultBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(context,false, Alerter.ICON_DEFAULT,true, Color.parseColor("#673AB7"),"Default","This is an default message", Alerter.BOTTOM).show();
            }
        });

        buttonCustomBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(context,false, Alerter.ICON_CUSTOM,true, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryDark),"Custom","This is an custom <b>ICON</b> message<br>BOTTOM", Alerter.BOTTOM);
                Alerter.getInstance().setAlerterIcon(de.th.topbottomalerter.R.string.fas_gas_pump);
                Alerter.getInstance().show();
            }
        });

        buttonErrorAutoBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(context,false, Alerter.ICON_ERROR,true, ContextCompat.getColor(getApplicationContext(),R.color.colorError),"ERROR","This is an error message", Alerter.BOTTOM,4000).show();
            }
        });

        buttonErrorAutoTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerter.make(context,false, Alerter.ICON_ERROR,true, ContextCompat.getColor(getApplicationContext(),R.color.colorError),"ERROR","This is an error message", Alerter.TOP,4000).show();
            }
        });

        buttonAlerterDialogBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAlerterDialogBottom();
            }
        });

        buttonAlerterDialogCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAlerterDialogCenter();
            }
        });

        buttonAlerterDialogTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAlerterDialogTop();
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void callAlerterDialogBottom(){

        alerterDialog = new AlerterDialog(context)
				.setDialogPosition(AlerterDialog.Position.BOTTOM)
                //.setDialogCustomView(customView)
				.setDialogCancelable(false)
                .setDialogAnimation(true)
				.setDialogRadius(40)
				.setDialogClickDismiss(false)
				.setDialogCloseDuration(0)
                //.setDialogTypeFace(Typeface.createFromAsset(getAssets(), "bsans.ttf"))
				//.setDialogBackgroundColor(getResources().getColor(R.color.alert_green))
				//setDialogTextColor(getResources().getColor(R.color.alert_red)) // nur wenn kein customView
				.setDialogTitel("Buttom Dialog") // nur wenn kein customView
				.setDialogMessage("Message<br><b>Buttom Dialog</b>") // nur wenn kein customView
                
                //.setHeaderDrawableIcon(getResources().getDrawable(R.drawable.luncher)) // muss gleichseitig sein!
                .setHeaderFontAwesomeIcon(de.th.topbottomalerter.R.string.fas_bell, 0.45F) // letterSpacing bei gleichseitigen Icon ca. 0.2F, bei rechteckigem Icon 0.5F bis 0.9F
                .setHeaderFontAwesomeIconColor(ContextCompat.getColor(getApplicationContext(),R.color.colorSuccess))
				.setHeaderIconEnable(true)
				.setHeaderIconAnimate(true)

				.setButtonRadius(80)
                //.setButtonRippleColor(getResources().getColor(R.color.alert_green))
                //.setButtonTextSize(15)
				.setButtonStrokeSize(1)
				.setPositiveButtonText("JA")
				.setPositiveButtonColor(getResources().getColor(R.color.alert_white))
				//.setPositiveButtonTextColor(getResources().getColor(R.color.alert_green))
				.setPositiveButtonStrokeColor(getResources().getColor(R.color.alert_green))
				.setNegativeButtonText("NEIN")
				.setNegativeButtonColor(getResources().getColor(R.color.alert_white))
                //.setNegativeButtonTextColor(getResources().getColor(R.color.alert_red))
				.setNegativeButtonStrokeColor(getResources().getColor(R.color.alert_green))
                .addPositiveButtonListener(new AlerterDialog.PositiveButtonListener() {
                    @Override
                    public void onClick() {
                        Toast.makeText(MainActivity.this, "Positive", Toast.LENGTH_SHORT).show();
                        //alerterDialog.startAnimHeaderIcon(true);
                    }
                })
                .addNegativeButtonListener(new AlerterDialog.NegativeButtonListener() {
                    @Override
                    public void onClick() {
                        Toast.makeText(MainActivity.this, "Negative", Toast.LENGTH_SHORT).show();
                        //alerterDialog.startAnimHeaderIcon(false);
                        alerterDialog.dismissAlert();
                    }
                })
        ;

        alerterDialog.show(getSupportFragmentManager(), "");
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void callAlerterDialogCenter(){

		// Nur für customView!
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View customView = inflater.inflate(R.layout.custom_alertdialog_layout, null, false);
        final EditText txtEdit = customView.findViewById(R.id.edit1);

        alerterDialog = new AlerterDialog(context)
				.setDialogPosition(AlerterDialog.Position.CENTER)
                .setDialogCustomView(customView)
				.setDialogCancelable(false)
                .setDialogAnimation(false)
				.setDialogRadius(40)
				.setDialogClickDismiss(false)
				.setDialogCloseDuration(0)
                //.setDialogTypeFace(Typeface.createFromAsset(getAssets(), "bsans.ttf"))
				//.setDialogBackgroundColor(getResources().getColor(R.color.colorPrimaryDark))
				//.setDialogTextColor(getResources().getColor(R.color.alert_white)) // nur wenn kein customView
				//.setDialogTitel("Custom Center") // nur wenn kein customView
				//.setDialogMessage("Message<br><b>white VextView</b> Test") // nur wenn kein customView
                
                .setHeaderDrawableIcon(getResources().getDrawable(R.drawable.ic_baseline_error_48)) // muss gleichseitig sein!
                //.setHeaderFontAwesomeIcon(de.th.topbottomalerter.R.string.fas_baby, 0.5F) // letterSpacing bei gleichseitigen Icon 0.2F, bei rechteckigem Icon 0.5F
                //.setHeaderFontAwesomeIconColor(ContextCompat.getColor(getApplicationContext(),R.color.colorSuccess))
				.setHeaderIconEnable(false)
				.setHeaderIconAnimate(false)

                .setButtonRadius(80)
                .setButtonTextSize(16)
				.setButtonStrokeSize(2)
				.setPositiveButtonText("Get Text")
				.setPositiveButtonColor(getResources().getColor(R.color.alert_white))
				.setPositiveButtonTextColor(getResources().getColor(R.color.colorInfo))
				.setPositiveButtonStrokeColor(getResources().getColor(R.color.colorInfo))
				.setNegativeButtonText("CANCEL")
				.setNegativeButtonColor(getResources().getColor(R.color.alert_white))
                .setNegativeButtonTextColor(getResources().getColor(R.color.alert_red))
				.setNegativeButtonStrokeColor(getResources().getColor(R.color.alert_red))

                .addPositiveButtonListener(new AlerterDialog.PositiveButtonListener() {
                    @Override
                    public void onClick() {
                        Toast.makeText(MainActivity.this, txtEdit.getText().toString(), Toast.LENGTH_SHORT).show();
                        //alerterDialog.startAnimHeaderIcon(true);
                    }
                })
                .addNegativeButtonListener(new AlerterDialog.NegativeButtonListener() {
                    @Override
                    public void onClick() {
                        Toast.makeText(MainActivity.this, "Negative", Toast.LENGTH_SHORT).show();
                        //alerterDialog.startAnimHeaderIcon(false);
                        alerterDialog.dismissAlert();
                    }
                })

        ;
        alerterDialog.show(getSupportFragmentManager(), "");
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void callAlerterDialogTop(){

        alerterDialog = new AlerterDialog(context)
                .setDialogPosition(AlerterDialog.Position.TOP)
                //.setDialogCustomView(customView)
                .setDialogCancelable(false)
                .setDialogAnimation(false)
                .setDialogRadius(40)
                .setDialogClickDismiss(true)
                .setDialogCloseDuration(4000)
                //.setDialogTypeFace(Typeface.createFromAsset(getAssets(), "bsans.ttf"))
                .setDialogBackgroundColor(getResources().getColor(R.color.colorWarning))
                .setDialogTextColor(getResources().getColor(R.color.alert_white)) // nur wenn kein customView
                .setDialogTitel("Warning!") // nur wenn kein customView
                .setDialogMessage("Message<br><b>Auto Close in 4 sec</b> Test") // nur wenn kein customView

                .setHeaderDrawableIcon(getResources().getDrawable(R.drawable.ic_baseline_error_48)) // muss gleichseitig sein!
                //.setHeaderFontAwesomeIcon(de.th.topbottomalerter.R.string.fas_baby, 0.5F) // letterSpacing bei gleichseitigen Icon 0.2F, bei rechteckigem Icon 0.5F
                //.setHeaderFontAwesomeIconColor(ContextCompat.getColor(getApplicationContext(),R.color.colorSuccess))
                .setHeaderIconEnable(true)
                .setHeaderIconAnimate(false)

                .setButtonRadius(80)
                .setButtonTextSize(15)
                .setButtonStrokeSize(3)
                .setPositiveButtonText("YES")
                .setPositiveButtonColor(getResources().getColor(R.color.alert_white))
                //.setPositiveButtonTextColor(getResources().getColor(R.color.alert_green))
                .setPositiveButtonStrokeColor(getResources().getColor(R.color.alert_green))
                .setNegativeButtonText("NEVER")
                .setNegativeButtonColor(getResources().getColor(R.color.alert_white))
                //.setNegativeButtonTextColor(getResources().getColor(R.color.alert_red))
                .setNegativeButtonStrokeColor(getResources().getColor(R.color.alert_green))
                /*
                .addPositiveButtonListener(new AlerterDialog.PositiveButtonListener() {
                    @Override
                    public void onClick() {
                        Toast.makeText(MainActivity.this, "Positive", Toast.LENGTH_SHORT).show();
                        alerterDialog.startAnimHeaderIcon(true);
                    }
                })

                 */
        ;
        alerterDialog.show(getSupportFragmentManager(), "");
    }

}