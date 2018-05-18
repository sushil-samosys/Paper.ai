package com.samosys.paperai.activity.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by samosys on 21/7/17.
 */

public class CustomFonts {

    public Typeface
            HelveticaNeue,
            HelveticaNeueBold,
            HelveticaNeueBoldItalic,
            HelveticaNeueCondensedBlack,
            HelveticaNeueItalic,
            HelveticaNeueLight,
            HelveticaNeueLightItalic,
            HelveticaNeueMedium,
            HelveticaNeueUltraLight,
            HelveticaNeueUltraLightItalic,
            calibri,
            CabinBold,
            calibrii,CalibriL,CabinRegular
                    ;


    public CustomFonts(Context mContext)
    {
        this.HelveticaNeue = Typeface.createFromAsset(mContext.getAssets(), "HelveticaNeue.ttf");
        this.HelveticaNeueBold = Typeface.createFromAsset(mContext.getAssets(), "HelveticaNeueBold.ttf");
        this.HelveticaNeueBoldItalic= Typeface.createFromAsset(mContext.getAssets(), "HelveticaNeueBoldItalic.ttf");
        this.HelveticaNeueCondensedBlack= Typeface.createFromAsset(mContext.getAssets(), "HelveticaNeueCondensedBlack.ttf");
        this.HelveticaNeueItalic= Typeface.createFromAsset(mContext.getAssets(), "HelveticaNeueItalic.ttf");
        this.HelveticaNeueLight= Typeface.createFromAsset(mContext.getAssets(), "HelveticaNeueLight.ttf");
        this.HelveticaNeueLightItalic= Typeface.createFromAsset(mContext.getAssets(), "HelveticaNeueLightItalic.ttf");
        this.HelveticaNeueMedium= Typeface.createFromAsset(mContext.getAssets(), "HelveticaNeue-Medium.otf");
        this.HelveticaNeueUltraLight= Typeface.createFromAsset(mContext.getAssets(), "HelveticaNeueUltraLight.ttf");

        this.calibri= Typeface.createFromAsset(mContext.getAssets(), "calibri.ttf");
        this.CabinBold= Typeface.createFromAsset(mContext.getAssets(), "CabinBold.otf");
        this.calibrii= Typeface.createFromAsset(mContext.getAssets(), "calibrii.ttf");
        this.CalibriL= Typeface.createFromAsset(mContext.getAssets(), "CalibriL.ttf");
        this.CabinRegular= Typeface.createFromAsset(mContext.getAssets(), "CabinRegular.otf");
        this.HelveticaNeueUltraLightItalic= Typeface.createFromAsset(mContext.getAssets(), "HelveticaNeueUltraLightItalic.ttf");
    }

}
