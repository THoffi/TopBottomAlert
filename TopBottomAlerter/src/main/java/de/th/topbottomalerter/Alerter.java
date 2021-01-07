package de.th.topbottomalerter;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Torsten Hoffmann on 15/12/2020.
	based on https://github.com/shasin89/NotificationBanner
 */
 
 /* todo
// https://github.com/Tapadoo/Alerter
// https://github.com/Hamadakram/Sneaker
// https://github.com/salehyarahmadi/AndExAlertDialog
*/

public class Alerter {

    //private Context mContext;
    private Activity activity;
    private View popupView;
    private View rootView;
    private  boolean focusable;
    private PopupWindow popupWindow;
    private ImageView imgClose;

    public static int TOP = Gravity.TOP;
    public static int BOTTOM = Gravity.BOTTOM;

    public static int ICON_SUCCESS = 1;
    public static int ICON_INFO = 2;
    public static int ICON_WARNING = 3;
    public static int ICON_ERROR = 4;
    public static int ICON_DEFAULT = 5;
    public static int ICON_CUSTOM = 6;
    public static int ICON_NONE = 7;

    private boolean showAlerter = false;
    //private boolean pulseAlerter = false;
    private int gravity = TOP;
    private int duration = 0;
    //private int alerterIcon;

    private RelativeLayout rlClose;

    private Integer animationStyle;

    //private int layout;

    private String TAG = getClass().getName();

    @SuppressLint("StaticFieldLeak")
    private static Alerter instance;
    private static int Delay = 0;

    private static Handler handler = null;
    private static Runnable runnable = null;

	// todo ???????????
    /*
    public interface AlerterListener {
        void onViewClickListener(View view);
    }
    */

    // +++ Constructors Begin +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public Alerter(){
		// todo
    }

    public Alerter(final Context context) {
        this.activity = (Activity) context;
        //this.rootView = view;
		this.rootView = activity.findViewById(android.R.id.content);
		/* wenn das geht, kann "rootview" in MainActivity
		** und "View view" hier weg!!
		*/
    }

	/**
     * This constructor is used for standard
     */
    public static Alerter make(Context context, boolean focusable, int alerterIcon, boolean alerterPulse, int backgroundColor, String title, String message, int position) {

        checkInstance();
		instance.activity = (Activity) context;
		//instance.rootView = view;
		instance.rootView = instance.activity.findViewById(android.R.id.content); // NEU ???
		instance.setLayout(R.layout.standard_alert_layout);
        instance.setIcon(alerterIcon);
        instance.setPulse(alerterPulse);
		instance.setTitleMessage(title, message);
        instance.setBackgroundColor(backgroundColor);
		instance.setDuration(0);
		instance.setGravity(position);
        instance.setFocusable(focusable);
        instance.setAnimationstyle();
		instance.setCancelButton();
        return instance;
    }

    /**
     * This constructor is used for autodismiss
     */
    public static Alerter make(Context context, boolean focusable, int alerterIcon, boolean alerterPulse, int backgroundColor, String title, String message, int position, int duration) {

        checkInstance();
		instance.activity = (Activity) context;
        //instance.rootView = view;
		instance.rootView = instance.activity.findViewById(android.R.id.content); // NEU ???
        instance.setLayout(R.layout.standard_alert_layout);
        instance.setIcon(alerterIcon);
        instance.setPulse(alerterPulse);
        instance.setTitleMessage(title, message);
        instance.setBackgroundColor(backgroundColor);
        instance.setDuration(duration);
        instance.setGravity(position);
        instance.setFocusable(focusable);
        instance.setAnimationstyle();
        instance.setCancelButton();
        return instance;
    }

    /**
     * this constructor is used for customlayout
     */
    public static Alerter make(Context context, int position, int Customlayout) {

        checkInstance();
		instance.activity = (Activity) context;
        //instance.rootView = view;
		instance.rootView = instance.activity.findViewById(android.R.id.content); // NEU ???
        instance.setLayout(Customlayout);
        instance.setDuration(0);
        instance.setGravity(position);
        instance.setAnimationstyle();
        return instance;
    }
	// +++ Constructors End +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private static void checkInstance(){
        // Ist PopupWindow bereits offen?
        Delay = 0;
        if(instance != null && instance.popupWindow != null && instance.popupWindow.isShowing()){
            Delay = 800;
        }
        // Ist Duration Handler bereits aktiv => dann Handler remove Callbacks
        if(handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }

        if(instance == null){
            instance = new Alerter();
        }else {
            if(instance.showAlerter){
                instance.dismissAlerter();
            }
        }
    }

    public static Alerter getInstance(){
        if(instance == null){
            instance = new Alerter();
        }
        return instance;
    }

    /**
     * Set this to auto dismiss  notification alerter
     * By defauly duration is 0 ms
     * @param duration int
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public int getDuration() {
        return duration;
    }

    /**
     * Set the location of the alerter to TOP,CENTER,BOTTOM
     * By defauly gravity is set to TOP
     * @param gravity int
     */
    public void setGravity(int gravity) {
        this.gravity = gravity;
    }
    public int getGravity() {
        return gravity;
    }

    /**
     * Alerter will be disappear when clicked outside ot the alerter, when focusable is true
     * focusable default value is false
     * @param focusable boolean
     */
    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }
    public boolean isFocusable() {
        return focusable;
    }

    private void setPulse(boolean pulse){
        if(pulse){
            ImageView imgIcon = popupView.findViewById(R.id.alerter_icon);
            Animation anim = AnimationUtils.loadAnimation(activity, R.anim.alerter_pulse);
            imgIcon.setAnimation(anim);
        }
    }

    private void setIcon(int type){
        ImageView imgIcon = popupView.findViewById(R.id.alerter_icon);

        de.th.fontawesome.FontAwesomeDrawable drawable;

        switch (type){
            case 1:
                // succes
                drawable = new de.th.fontawesome.FontAwesomeDrawable(activity, R.string.fa_check_circle);
                drawable.setTextColor(ContextCompat.getColor(activity, android.R.color.white));
                imgIcon.setImageDrawable(drawable);
                break;
            case 2:
                // info;
                drawable = new de.th.fontawesome.FontAwesomeDrawable(activity, R.string.fas_info_circle);
                drawable.setTextColor(ContextCompat.getColor(activity, android.R.color.white));
                imgIcon.setImageDrawable(drawable);
                break;
            case 3:
                // warning;
                drawable = new de.th.fontawesome.FontAwesomeDrawable(activity, R.string.fas_exclamation_circle);
                drawable.setTextColor(ContextCompat.getColor(activity, android.R.color.white));
                imgIcon.setImageDrawable(drawable);
                break;
            case 4:
                // error;
                drawable = new de.th.fontawesome.FontAwesomeDrawable(activity, R.string.fas_exclamation_triangle);
                drawable.setTextColor(ContextCompat.getColor(activity, android.R.color.white));
                imgIcon.setImageDrawable(drawable);
                break;
            case 5:
                // default;
                drawable = new de.th.fontawesome.FontAwesomeDrawable(activity, R.string.fas_bell);
                drawable.setTextColor(ContextCompat.getColor(activity, android.R.color.white));
                imgIcon.setImageDrawable(drawable);
                break;
            case 7:
                // none;
                imgIcon.setVisibility(View.INVISIBLE);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(0, 38);
                imgIcon.setLayoutParams(layoutParams);
                break;

        }

    }

    public void setAlerterIcon(int type) {
        ImageView imgIcon = popupView.findViewById(R.id.alerter_icon);
        de.th.fontawesome.FontAwesomeDrawable drawable = new de.th.fontawesome.FontAwesomeDrawable(activity, type);
        drawable.setTextColor(ContextCompat.getColor(activity, android.R.color.white));
        imgIcon.setImageDrawable(drawable);
    }

    /**
     * This method set textviews for the default alerter
     * @param title String
     * @param message String
     */
    private void setTitleMessage(String title, String message){
		TextView txtTitle = popupView.findViewById(R.id.txt_title);
		txtTitle.setText(title);
		if(title.length()<1) txtTitle.setHeight(0);
		
		TextView txtMessage = popupView.findViewById(R.id.txt_message);
		txtMessage.setText(fromHtml(message));
        if(message.length()<1) txtMessage.setHeight(0);
    }

    /**
     * This method set textviews for the default alerter
     * @param color int
     */
    private void setBackgroundColor(int color){
        popupView.setBackgroundColor(color);
    }

    /**
     * Hide or Close icon in layout "rlCancel"
	 * if duration is already set onClickListener
     */
    private void setCancelButton(){
        if(duration > 0){
            rlClose.setVisibility(View.INVISIBLE);
        }else {
            rlClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rlClose.setVisibility(View.INVISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismissAlerter();
                        }
                    }, 10);

                    //dismissAlerter();
                }
            });
        }
    }

    /**
     *Initialize the alerter view
     * @param layout int
     */
    public void setLayout(int layout){
        if(activity != null){
            LayoutInflater inflater = (LayoutInflater) activity.getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            popupView = inflater.inflate(layout, null);
            rlClose = popupView.findViewById(R.id.rlClose);
            imgClose = popupView.findViewById(R.id.imgClose);
        }
    }

    /**
     * This method returns the alerter view set by setLayout
     * This method must be invoke after setLayout in order to avoid null pointer
     */
    public View getAlerterView() {
        return popupView;
    }

	/**
     * This method dismiss PopupWindow
     */
    public void dismissAlerter(){
        try{
            popupWindow.dismiss();
            showAlerter = false;
        }catch (Exception e){
            Log.e(TAG,e.toString());
        }
    }
	
	/**
     * This method set default PopupWindow-Animstyle for gravity TOP or BOTTOM
     */
    private void setAnimationstyle(){
        if(gravity == TOP)
            animationStyle = R.style.NotificationAnimationTop;
        else if (gravity == BOTTOM)
            animationStyle = R.style.NotificationAnimationBottom;
    }
	
    /**
     * This method create a new popup window
     * This method must be called after setLayout
     */
    public void show(){

        if(activity != null){

            showAlerter = true;

			int width = LinearLayout.LayoutParams.MATCH_PARENT;
			int height = LinearLayout.LayoutParams.WRAP_CONTENT;

			popupWindow = new PopupWindow(popupView, width, height, focusable);
	
			if(animationStyle != null){
				popupWindow.setAnimationStyle(animationStyle);
			}

			// Show PopupWindow
			android.os.Handler handler = new android.os.Handler();
			handler.postDelayed(new Runnable() {
			    @Override
                public void run() {
			        popupWindow.showAtLocation(rootView, gravity, 0, 0);
			    }
			    }, Delay);
			
			// Interface definition for a callback to be invoked when this view is attached or detached from its window.
			rootView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // funktioniert :-)
				@Override
				public void onViewAttachedToWindow(View v) {
					// todo
				}
				@Override
				public void onViewDetachedFromWindow(View v) {
					dismissAlerter();
					System.out.println("MyCustomView.onViewDetachedFromWindow");
				}
			});

			autoDismiss(duration);
        }
    }

    /**
     * This method auto dismiss alerter
     *@param duration int
     */
    private void autoDismiss(int duration){
        if(duration > 0){
            handler = new android.os.Handler();
            runnable = new Runnable() {
                public void run() {
                    // do something
                    dismissAlerter();
                }
            };
            handler.postDelayed(runnable, duration);
        }
    }

    /**
     * This method convert htmlText to Spannend for TextView
     *@param html String
     */
    @SuppressWarnings("deprecation")
    private static Spanned fromHtml(String html){
        if(html == null){
            // return an empty spannable if the html is null
            return new SpannableString("");
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
            // we are using this flag to give a consistent behaviour
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }

}
