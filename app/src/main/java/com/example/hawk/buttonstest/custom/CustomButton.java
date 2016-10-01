package com.example.hawk.buttonstest.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.Button;

import com.example.hawk.buttonstest.R;
import com.example.hawk.buttonstest.Utils;

public class CustomButton extends Button {

    private Bitmap mIcon;
    private Paint mPaint;
    private Rect mSrcRect;
    private int mIconPadding;
    private int mIconSize;
    private int mRoundedCornerRadius;
    private boolean mIconCenterAligned;
    private boolean mRoundedCorner;
    private boolean mTransparentBackground;
    private boolean mWithIcon;

    public CustomButton(Context context) {
        super(context);
        setStyle(R.color.colorPrimary,context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs, 0);
        setStyle(R.color.colorPrimary,context);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr,int color,int logo) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,logo);
        setStyle(color,context);
    }

    public CustomButton(Context context, AttributeSet attrs, int color, int logo){
        super(context,attrs);
        init(context, attrs,logo);
        setStyle(color,context);

    }

    private void setStyle(int color, Context context){

        if(mTransparentBackground)
            setTextColor(Color.BLACK);
        else
            setTextColor(Color.WHITE);

        setBackgroundResource(R.drawable.round_corner);
        GradientDrawable drawable = (GradientDrawable) getBackground().mutate();
        drawable.setColor(getResources().getColor(color));
        drawable.setCornerRadius(0);

        if(mRoundedCorner)
            drawable.setCornerRadius(mRoundedCornerRadius);

        if(mTransparentBackground){
            drawable.setColor(Color.TRANSPARENT);
            drawable.setStroke(4,getResources().getColor(color));
        }

        drawable.invalidateSelf();

        setPadding((int) Utils.convertDpToPixel(30,context),0,(int)Utils.convertDpToPixel(30,context),0);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Recalculate width and amount to shift by, taking into account icon size
        if(mWithIcon){
            int shift = (mIconSize + mIconPadding) / 2;

            canvas.save();
            canvas.translate(shift, 0);

            super.onDraw(canvas);

            float textWidth = getPaint().measureText((String)getText());
            int left = (int)((getWidth() / 2f) - (textWidth / 2f) - mIconSize - mIconPadding);
            int top = getHeight()/2 - mIconSize/2;

            if(!mIconCenterAligned)
                left = 0;

                Rect destRect = new Rect(left, top, left + mIconSize, top + mIconSize);
                canvas.drawBitmap(mIcon, mSrcRect, destRect, mPaint);
        } else {
            canvas.save();
            super.onDraw(canvas);
        }

        canvas.restore();
    }

    private void init(Context context, AttributeSet attrs, int logo) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.IconButton);

        // Initialize variables to default values
        setDefaultValues(context,logo);

        // Don't add padding when text isn't present
        if(attrs.getAttributeValue("http://schemas.android.com/apk/res/android","text")!=null){
            mIconPadding = (int)Utils.convertDpToPixel(20,context);
        }

        // Load the custom properties and assign values
        for (int i = 0; i < array.getIndexCount(); ++i) {
            int attr = array.getIndex(i);
            if(attr == R.styleable.IconButton_iconPadding){
                mIconPadding = array.getDimensionPixelSize(attr, (int)Utils.convertDpToPixel(20,context));
            }
            if(attr == R.styleable.IconButton_iconCenterAligned){
                mIconCenterAligned = array.getBoolean(attr,true);
            }
            if(attr == R.styleable.IconButton_iconSize) {
                mIconSize = array.getDimensionPixelSize(attr, (int) Utils.convertDpToPixel(20, context));
            }
            if(attr == R.styleable.IconButton_roundedCorner){
                mRoundedCorner = array.getBoolean(attr,false);
            }
            if(attr == R.styleable.IconButton_roundedCornerRadius){
                mRoundedCornerRadius = array.getDimensionPixelSize(attr,(int)Utils.convertDpToPixel(40,context));
            }
            if(attr == R.styleable.IconButton_transparentBackground){
                mTransparentBackground = array.getBoolean(attr,false);
            }
            if (attr == R.styleable.IconButton_withIcon) {
                mWithIcon = array.getBoolean(attr,false);
            }
        }

        array.recycle();

        if(mWithIcon){
            mPaint = new Paint();
            mSrcRect = new Rect(0, 0, mIcon.getWidth(), mIcon.getHeight());
        }

    }

    private void setDefaultValues(Context context,int logo){

        if(logo != 0)
            mIcon = Utils.drawableToBitmap(getResources().getDrawable(logo));
        mIconSize = (int)Utils.convertDpToPixel(20,context);
        mIconCenterAligned = true;
        mRoundedCorner = false;
        mWithIcon = false;
        mTransparentBackground = false;
        mRoundedCornerRadius = (int)Utils.convertDpToPixel(40,context);

    }
}
