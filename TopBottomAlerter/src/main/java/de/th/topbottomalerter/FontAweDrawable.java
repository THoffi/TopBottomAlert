package de.th.topbottomalerter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
//import android.util.Log;
import android.util.TypedValue;

import androidx.core.content.res.ResourcesCompat;

public class FontAweDrawable extends Drawable {
    private static final int DEFAULT_COLOR = Color.BLUE;
    private static final int DEFAULT_TEXTSIZE = 48;
    private Paint mPaint;
    private String sText;
    private int mIntrinsic;

    public FontAweDrawable(Context context, Resources res, int faIconRes, float letterSpacing, int iconColor) {

        String strFaIconRes = context.getString(faIconRes);
        Typeface tf;
        String[] arr = strFaIconRes.split("#");

        if(arr.length < 2){
            return;
        }

        switch (arr[0]) {
            case "solid":
                tf = ResourcesCompat.getFont(context, R.font.fa_solid_900);
                break;
            case "brands":
                tf = ResourcesCompat.getFont(context, R.font.fa_brands_400);
                break;
            default:
                // "regular"
                tf = ResourcesCompat.getFont(context, R.font.fa_regular_400);
                break;
        }
        // get Text
        CharSequence mText = fromHtml("&#x" + arr[1] + ";");
        sText = mText.toString();
		// init Paint
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // set PaintTypeface
        if (mPaint.getTypeface() != tf) {
            mPaint.setTypeface(tf);
        }
        // set PaintColor
        if (iconColor != 0) {
            mPaint.setColor(iconColor);
        }else{
            mPaint.setColor(DEFAULT_COLOR);
        }
        // set PaintTextAlign
        mPaint.setTextAlign(Align.CENTER);
        // set PaintTextSize
        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXTSIZE, res.getDisplayMetrics());
        mPaint.setTextSize(textSize);
		// set PaintLetterSpacing
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPaint.setLetterSpacing(letterSpacing); // 0 bis 1
        }
        // calc Abmessung
        int mIntrinsicWidth = (int) (mPaint.measureText(mText, 0, mText.length()) + .5);
        int mIntrinsicHeight = mPaint.getFontMetricsInt(null);
        mIntrinsic = Math.max(mIntrinsicHeight, mIntrinsicWidth);
    }

    @Override
    public void draw(Canvas canvas) {
		// https://stackoverflow.com/questions/11120392/android-center-text-on-canvas
		//     drawText (String text, float x, float y, Paint paint)
		// ascent = Der empfohlene Abstand über der Grundlinie für Text mit einzelnem Abstand.
		// descent = Der empfohlene Abstand unter der Grundlinie für Text mit einzelnem Abstand
		float centerX = mIntrinsic * 0.5F;
		float centerY = mIntrinsic * 0.5F - ((mPaint.descent() + mPaint.ascent()) * 0.5F);
        canvas.drawText(sText, centerX, centerY, mPaint);
    }

    @Override
    public int getOpacity() {
        return mPaint.getAlpha();
    }

    @Override
    public int getIntrinsicWidth() {
		//Log.e("getIntrinsicWidth", "" + mIntrinsicWidth);
        return mIntrinsic;
    }

    @Override
    public int getIntrinsicHeight() {
		//Log.e("getIntrinsicHeight", "" + mIntrinsicHeight);
        return mIntrinsic;
    }
    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter filter) {
        mPaint.setColorFilter(filter);
    }

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
}
