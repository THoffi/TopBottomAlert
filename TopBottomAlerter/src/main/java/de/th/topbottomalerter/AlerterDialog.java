package de.th.topbottomalerter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.DisplayMetrics;
//import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.mikhaellopez.circularimageview.CircularImageView;

/**
 * Created by Torsten Hoffmann on 25/12/2020.
 * https://github.com/Far5had70/Falert
 * https://github.com/dyablo619/Custom-Alert-Dialog
 */
 
/* Noch zu machen
	- Farben überarbeiten
	- actionSetHeaderIcon() (FontAwesomeIcon) : 336
	
*/

@SuppressLint("ValidFragment")
public class AlerterDialog extends DialogFragment implements View.OnClickListener {
	
    private final Context context;
    private final Activity activity;
    
    private View rootView;
    private View dialogView;
    private View buttonRootView;
    private View customView;
    private TextView txtNegativeButton, txtPositiveButton;
    private FrameLayout frameLayoutContent;
    private CircularImageView circularImageView;
	private Typeface typeFace;
	private Drawable iconDrawable = null;

    private PositiveButtonListener positiveButtonListener;
    private NegativeButtonListener negativeButtonListener;
	
    private boolean clickDismiss = true;
    private boolean iconEnable = true;
    private boolean cancelable = true;
    private boolean animHeaderIconEnable = false;
    private boolean animDialog = false;
    private boolean isCustomView = false;
    private boolean isPositiveListener = false;
    private boolean isNegativeListener = false;
	
    private String positiveButtonText = null;
    private String negativeButtonText = null;
	private String titleText = null;
    private String messageText = null;
	//private final String TAG = "AlerterDialog";
	
	private int iconFontAwesome = 0;				// z.B. R.string.fa_check_circle
    private int iconFontAwesomeColor = 0;			// z.B. getResources().getColor(R.color.alert_green)
	private int alertPosition = 0;
    private int alertRadius = 40;
    private int buttonRadius = 80;
    private int positiveButtonColor = 0;
    private int negativeButtonColor = 0;
    private int positiveButtonTextColor = 0;
    private int negativeButtonTextColor = 0;
    private int buttonRippleColor = 0;
    private int backgroundColor = 0;
	private int textColor = 0;
    private int strokeButtonsSize = 2;
    private int strokePositiveButtonColor = 0;
    private int strokeNegativeButtonColor = 0;
	private int closeDuration = 0;					// Wartezeit bis Dialog automatisch geschlossen wird [ms], 0 = wird niemals autom. geschlossen
	static int dismissDelay = 150;					// Wartezeit wenn Dialog geschlossen wird [ms]
    private float buttonTextSize = 16;				// TextSize für Positive und Negative Button
    private float letterSpacing = 0;				// Spacing für FontAwesomeIcon (0F bis 1F)
	
	private android.os.Handler handlerAutoClose = null;
	private Runnable runnableAutoCloser = null;

    public AlerterDialog (Context context) {
        this.context = context;
        this.activity = (Activity) context;
    }

    public interface PositiveButtonListener {
        void onClick();
    }

    public interface NegativeButtonListener {
        void onClick();
    }

	public static class Position {
		public static int CENTER = 0;
        public static int TOP = 1;
        public static int BOTTOM = 2;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(dialogView == null) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogView = inflater.inflate(R.layout.dialogview_alertdialog_layout, container, false);
        }

		//Log.e(TAG, "View onCreateView()");
        return dialogView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Verhindert Bildschirm kippen
        if(activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        // Init rootView
        initRootView();
		// Set Dialog
        if(getDialog() == null || getDialog().getWindow() == null) return;
        //getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE); // ERROR
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if(animDialog) getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        // Set Dialog Canceable
        setCancelable(cancelable);
        // Set Dialog Position
        actionSetDialogPosition();
        // Set Dialog Background Shape
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT - 1;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes(params);
        GradientDrawable bgShape = new GradientDrawable();
        bgShape.setCornerRadius(alertRadius);
        if (backgroundColor != 0){
            bgShape.setColor(backgroundColor);
            circularImageView.setBorderColor(backgroundColor);
        }else {
            bgShape.setColor(getResources().getColor(de.th.topbottomalerter.R.color.alert_white));
            circularImageView.setBorderColor(getResources().getColor(de.th.topbottomalerter.R.color.alert_white));
        }
        rootView.setBackground(bgShape);
		// Set AutoClose
		actionSetAutoClose();

        //Log.e(TAG, "onViewCreated()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Lässt Bildschirm kippen wieder zu
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		// Remove Handler AutoCloser
        if(handlerAutoClose != null && runnableAutoCloser != null) {
            handlerAutoClose.removeCallbacks(runnableAutoCloser);
        }
    }

	@Override
    public void onResume() {
        super.onResume();
        // nix
    }
	
	@Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.alert_negative_button) {
			// + Click NegativeButton
            negativeButtonListener.onClick();
            if (clickDismiss) {
                dismissAlert();
            }
        } else if (i == R.id.alert_positive_button) {
			// + Click PositiveButton
            positiveButtonListener.onClick();
            if (clickDismiss) {
                dismissAlert();
            }
        } else if (i == R.id.alert_icon){
			// + Click HeaderIcon
            //if (!animHeaderIconEnable){
                circularImageView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.alerter_rotate));
            //}
        }
    }
	
	/**
     * Init RootView of Dialog
     */
	private void initRootView() {
		
        if (dialogView == null) {
            return;
        }

        rootView = dialogView.findViewById(R.id.root);
        txtNegativeButton = dialogView.findViewById(R.id.alert_negative_button);
        txtPositiveButton = dialogView.findViewById(R.id.alert_positive_button);
        frameLayoutContent = dialogView.findViewById(R.id.frameLayoutContent);
        circularImageView = dialogView.findViewById(R.id.alert_icon);
        circularImageView.setOnClickListener(this);
        View iconView = dialogView.findViewById(R.id.frameIcon);
        buttonRootView = dialogView.findViewById(R.id.button_root);

        if(textColor == 0) {
            textColor = getResources().getColor(R.color.alert_text);
        }

        if (customView != null) {
            actionSetCustomView();
        }else{
			actionSetStandardView();
		}

        actionSetHeaderIcon();

		actionSetButton();

        if (!iconEnable) {
            iconView.setVisibility(View.GONE);

            /*
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) rootView.getLayoutParams();
            params.setMargins(0,0,0,0);
            rootView.setLayoutParams(params);
            rootView.requestLayout();
            */

            rootView.setPadding(0,-70,0,0);
        }

        if (animHeaderIconEnable) {
            circularImageView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.alerter_rotate));
        }

        //Log.e(TAG, "initRootView()");
    }

    /**
     * Set Position of Dialog
     */
    private void actionSetDialogPosition() {
		
        if (getDialog() == null) {
            return; // Abbruch
        }

        Window window = getDialog().getWindow();
        assert window != null;
        WindowManager.LayoutParams params = window.getAttributes();

        if(alertPosition == 0){
            // CENTER
            window.setGravity(Gravity.CENTER);
        }else if(alertPosition == 1){
            // TOP
            window.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
            params.y = 30; // Abstand von oben
            window.setAttributes(params);
        }else{
            // BOTTOM
            window.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
            params.y = 60; // Abstand von unten
            window.setAttributes(params);
        }
    }
	
	/**
     * Set Dialog AutoCloser, schliesst Dialog nach gewisser Zeit autom.
     * Aber nur wenn closeDuration und Keine Button vorhanden sind
     * **/
    private void actionSetAutoClose() {
		if(closeDuration > 0 && !isNegativeListener && !isPositiveListener){
			// nur wenn KEINE Button
			handlerAutoClose = new android.os.Handler();
			runnableAutoCloser = (new Runnable() {
				@Override
				public void run() {
					dismiss();
				}
                });
			// Start Handler
			handlerAutoClose.postDelayed(runnableAutoCloser, closeDuration);
		}
	}

	/**
     * Set drawable Header Icon
     * **/
    private void actionSetHeaderIcon() {
        FontAweDrawable iconFADrawable;
        if (iconDrawable != null) {
            // + set Custom Icon with Drawable
            circularImageView.setImageDrawable(iconDrawable);
        }else if(iconFontAwesome != 0){
			// + set Custom Icon with FontAweDrawable
            // letterSpacing bei quadratischem Icon 0.2F, bei rechteckigem Icon 0.5F
            iconFADrawable = new FontAweDrawable(context, getResources(), iconFontAwesome, letterSpacing, iconFontAwesomeColor);
            circularImageView.setImageDrawable(iconFADrawable);
		}else{
			// + set Standard Icon with FontAwesomeDrawable
            // letterSpacing bei quadratischem Icon 0.2F, bei rechteckigem Icon 0.5F
            iconFADrawable = new FontAweDrawable(context, getResources(), R.string.fas_info_circle, 0.2F, getResources().getColor(R.color.alert_green));
            circularImageView.setImageDrawable(iconFADrawable);
		}
    }

    /**
     * Add CustomView
     * **/
    private void actionSetCustomView() {
        frameLayoutContent.addView(customView);
        this.isCustomView = true;
    }

    /**
     * Add StandardView
     * **/
	private void actionSetStandardView() {
        // Inflate View
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View customView = inflater.inflate(R.layout.standard_alertdialog_layout, null, false);
        frameLayoutContent.addView(customView);
        // set Text Title
        TextView txtTitle = customView.findViewById(R.id.txtDialogTitle);
        txtTitle.setText(titleText);
        txtTitle.setTextColor(textColor);
        if(titleText == null || titleText.length() < 1) txtTitle.setHeight(0);
        // set Text Message
        TextView txtMessage = customView.findViewById(R.id.textDialogMessage);
        txtMessage.setText(fromHtml(messageText));
        txtMessage.setTextColor(textColor);
        if(messageText == null || messageText.length() < 1) txtMessage.setHeight(0);
		// set TypeFace
		if (typeFace != null) {
            txtTitle.setTypeface(typeFace);
            txtMessage.setTypeface(typeFace);
        }
        // is Custom nein
        isCustomView = false;
    }

	 /**
	 * Set Positive and Negative Button
	 **/
    @SuppressLint("SetTextI18n")
    private void actionSetButton() {
        if(buttonRippleColor == 0){
            buttonRippleColor = getResources().getColor(R.color.alert_ripple_grey);
        }
		// Button Text
		txtPositiveButton.setText("YES"); // default
        if (positiveButtonText != null) txtPositiveButton.setText(positiveButtonText); // custom
		txtNegativeButton.setText("NO"); // default
        if (negativeButtonText != null) txtNegativeButton.setText(negativeButtonText); // custom
		// Button TypeFace
		if (typeFace != null) {
            txtPositiveButton.setTypeface(typeFace);
            txtNegativeButton.setTypeface(typeFace);
        }
		// Button TextSize
        txtPositiveButton.setTextSize(buttonTextSize);
        txtNegativeButton.setTextSize(buttonTextSize);
		// Button TextColor
		txtPositiveButton.setTextColor(getResources().getColor(R.color.alert_green)); 		// default
        if (positiveButtonTextColor != 0) txtPositiveButton.setTextColor(positiveButtonTextColor); 	// custom
		txtNegativeButton.setTextColor(getResources().getColor(R.color.alert_red)); 		// default
        if (negativeButtonTextColor != 0) txtNegativeButton.setTextColor(negativeButtonTextColor);	// custom
		// Button Visibility
        if(isPositiveListener){
            txtPositiveButton.setVisibility(View.VISIBLE);
        }else{
            txtPositiveButton.setVisibility(View.INVISIBLE);
        }
        if(isNegativeListener){
            txtNegativeButton.setVisibility(View.VISIBLE);
        }else{
            txtNegativeButton.setVisibility(View.INVISIBLE);
        }
        if(!isNegativeListener && !isPositiveListener){
			// Ohne Button
            //cancelable = true;
            buttonRootView.setVisibility(View.GONE);
            ConstraintLayout placeholder = dialogView.findViewById(R.id.button_root_placeholder);
            placeholder.setVisibility(View.VISIBLE);
        }
		// Button ClickListener
		txtPositiveButton.setOnClickListener(this);
        txtNegativeButton.setOnClickListener(this);
		
		// Button Drawable mit Ripple-Effect
		// https://stackoverflow.com/questions/44245757/set-rippledrawable-corner-radius-programmatically
		// https://stackoverflow.com/questions/41556036/get-ripple-effect-on-transparent-custom-button-view

		// - NegativeButton
		if(isNegativeListener){
			GradientDrawable nGD = new GradientDrawable();
			nGD.setShape(GradientDrawable.RECTANGLE);
			nGD.setColor(getResources().getColor(R.color.alert_red)); // default
			if (negativeButtonColor != 0) nGD.setColor(negativeButtonColor); // custom
			nGD.setStroke(strokeButtonsSize, textColor); // default
			if (strokeNegativeButtonColor != 0) nGD.setStroke(strokeButtonsSize, strokeNegativeButtonColor); // custom
			nGD.setCornerRadius(buttonRadius);
			// Set Ripple ab Android 5.0
			if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				ColorStateList pressedStates = ColorStateList.valueOf(buttonRippleColor); // hellgrau
				RippleDrawable rippleDrawable = new RippleDrawable(pressedStates, nGD, null);
				txtNegativeButton.setBackground(rippleDrawable);
			}else{
				txtNegativeButton.setBackground(nGD);
			}
		}
		
		// oder !! muss wieder in xml weg !!!
		// android:clickable="true"
		// android:background="?attr/selectableItemBackground"
		
		// + PositiveButton
		if(isPositiveListener){
			GradientDrawable pGD = new GradientDrawable();
			pGD.setShape(GradientDrawable.RECTANGLE);
			pGD.setColor(getResources().getColor(R.color.alert_green)); // default
			if (positiveButtonColor != 0) pGD.setColor(positiveButtonColor); // custom
			pGD.setStroke(strokeButtonsSize, textColor); // default
			if (strokePositiveButtonColor != 0) pGD.setStroke(strokeButtonsSize, strokePositiveButtonColor); // custom
			pGD.setCornerRadius(buttonRadius);
			// Set Ripple ab Android 5.0
			if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				ColorStateList pressedStates = ColorStateList.valueOf(buttonRippleColor); // hellgrau
				RippleDrawable rippleDrawable = new RippleDrawable(pressedStates, pGD, null);
				txtPositiveButton.setBackground(rippleDrawable);
			}else{
				txtPositiveButton.setBackground(pGD);
			}
		}
    }

    public void dismissAlert() {
		// NEU mit Verzögerung
        if(animDialog) dismissDelay = 50;
		if(dismissDelay > 0) {
			android.os.Handler handler = new android.os.Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					dismiss();
				}
			}, dismissDelay);
		}else{
			dismiss();
		}
    }
	
	public void startAnimHeaderIcon(boolean enable) {
        animHeaderIconEnable = enable;
        if (enable){
            circularImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_cycle,context.getTheme()));
            circularImageView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.alerter_rotate_indefinitely));
        }else {
            circularImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_cycle,context.getTheme()));
            circularImageView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.alerter_rotate));
        }

    }

/** >> START Outsite Settings **/
	// Dialog
	public AlerterDialog setDialogPosition(int alertPosition) {
        this.alertPosition = alertPosition;
        return this;
    }
	public AlerterDialog setDialogCustomView(View customView) {
        this.customView = customView;
        return this;
    }
	public AlerterDialog setDialogCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }
    public AlerterDialog setDialogAnimation(boolean animDialog) {
        this.animDialog = animDialog;
        return this;
    }
	public AlerterDialog setDialogRadius(int alertRadius) {
        this.alertRadius = alertRadius;
        return this;
    }
	public AlerterDialog setDialogClickDismiss(boolean clickDismiss) {
        this.clickDismiss = clickDismiss;
        return this;
    }
	public AlerterDialog setDialogCloseDuration(int closeDuration) {
        this.closeDuration = closeDuration;
        return this;
    }
	public AlerterDialog setDialogTypeFace(Typeface typeFace) {
        this.typeFace = typeFace;
        return this;
    }
	public AlerterDialog setDialogBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }
	// Dialog Title and Message
	public AlerterDialog setDialogTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }
	public AlerterDialog setDialogTitel(String titleText) {
        this.titleText = titleText;
        return this;
    }
	public AlerterDialog setDialogMessage(String messageText) {
        this.messageText = messageText;
        return this;
    }
	// Dialog Header
	public AlerterDialog setHeaderDrawableIcon(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
        return this;
    }
    public AlerterDialog setHeaderFontAwesomeIcon(int iconFontAwesome, float letterSpacing) {
        this.iconFontAwesome = iconFontAwesome;
        this.letterSpacing = letterSpacing;
        return this;
    }
    public AlerterDialog setHeaderFontAwesomeIconColor(int iconFontAwesomeColor) {
        this.iconFontAwesomeColor = iconFontAwesomeColor;
        return this;
    }
	public AlerterDialog setHeaderIconEnable(boolean iconEnable) {
        this.iconEnable = iconEnable;
        return this;
    }
    public AlerterDialog setHeaderIconAnimate(boolean animHeaderIconEnable) {
        this.animHeaderIconEnable = animHeaderIconEnable;
        return this;
    }
	// Button's allgemein
	public AlerterDialog setButtonRadius(int buttonRadius) {
        this.buttonRadius = buttonRadius;
        return this;
    }
    public AlerterDialog setButtonRippleColor(int buttonRippleColor) {
        this.buttonRippleColor = buttonRippleColor;
        return this;
    }
	public AlerterDialog setButtonTextSize(float buttonTextSize) {
        this.buttonTextSize = buttonTextSize;
        return this;
    }
	public AlerterDialog setButtonStrokeSize(int strokeButtonsSize) {
        this.strokeButtonsSize = strokeButtonsSize;
        return this;
    }
	// PositiveButton
	public AlerterDialog setPositiveButtonText(String positiveButtonText) {
        this.positiveButtonText = positiveButtonText;
        return this;
    }
	public AlerterDialog setPositiveButtonColor(int positiveButtonColor) {
        this.positiveButtonColor = positiveButtonColor;
        return this;
    }
	public AlerterDialog setPositiveButtonTextColor(int positiveButtonTextColor) {
        this.positiveButtonTextColor = positiveButtonTextColor;
        return this;
    }
	public AlerterDialog setPositiveButtonStrokeColor(int strokePositiveButtonColor) {
        this.strokePositiveButtonColor = strokePositiveButtonColor;
        return this;
    }
	// NagativeButton
	public AlerterDialog setNegativeButtonText(String negativeButtonText) {
        this.negativeButtonText = negativeButtonText;
        return this;
    }
	public AlerterDialog setNegativeButtonColor(int negativeButtonColor) {
        this.negativeButtonColor = negativeButtonColor;
        return this;
    }
	public AlerterDialog setNegativeButtonTextColor(int negativeButtonTextColor) {
        this.negativeButtonTextColor = negativeButtonTextColor;
        return this;
    }
	public AlerterDialog setNegativeButtonStrokeColor(int strokeNegativeButtonColor) {
        this.strokeNegativeButtonColor = strokeNegativeButtonColor;
        return this;
    }
	// Listener
    public AlerterDialog addPositiveButtonListener(PositiveButtonListener positiveButtonListener) {
        this.positiveButtonListener = positiveButtonListener;
        isPositiveListener = true;
        //Log.e(TAG, "PositiveButtonListener added");
        return this;
    }
	public AlerterDialog addNegativeButtonListener(NegativeButtonListener negativeButtonListener) {
        this.negativeButtonListener = negativeButtonListener;
        isNegativeListener = true;
        //Log.e(TAG, "NegativeButtonListener added");
        return this;
    }

/* <<< END  Outsite Settings */

	/**
     * Helper: This method convert htmlText to Spannend for TextView
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
	
	/**
     * Helper: DP to Pixel
	 *@param valueInDp float
     */
    private int dpToPx(float valueInDp) {
        if(getActivity() == null) return 0;
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

}