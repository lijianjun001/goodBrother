package com.cylty.zhongmukeji.loadSkin.entity;


public class AttrFactory {

    public static final String TOPBACKGROUND = "drawableTop";
    public static final String BACKGROUND = "background";
    public static final String TEXT_COLOR = "textColor";
    public static final String LIST_SELECTOR = "listSelector";
    public static final String DIVIDER = "divider";
    public static final String WIDTH = "layout_width";
    public static final String HEIGHT = "layout_height";
    public static final String DRAWABLE_WIDTH = "drawableWidth";
    public static final String DRAWABLE_HEIGHT = "drawableheight";

    public static SkinAttr get(String attrName, int attrValueRefId, String attrValueRefName, String typeName) {

        SkinAttr mSkinAttr = null;

        if (TOPBACKGROUND.equals(attrName)) {
            mSkinAttr = new BackgroundAttr();
        } else if (BACKGROUND.equals(attrName)) {
            mSkinAttr = new BackgroundAttr();
        } else if (TEXT_COLOR.equals(attrName)) {
            mSkinAttr = new TextColorAttr();
        } else if (LIST_SELECTOR.equals(attrName)) {
            mSkinAttr = new ListSelectorAttr();
        } else if (DIVIDER.equals(attrName)) {
            mSkinAttr = new DividerAttr();
        } else if (WIDTH.equals(attrName) || HEIGHT.equals(attrName) || DRAWABLE_WIDTH.equals(attrName) || DRAWABLE_HEIGHT.equals(attrName)) {
            mSkinAttr = new SizeAttr();
        } else {
            return null;
        }

        mSkinAttr.attrName = attrName;
        mSkinAttr.attrValueRefId = attrValueRefId;
        mSkinAttr.attrValueRefName = attrValueRefName;
        mSkinAttr.attrValueTypeName = typeName;
        return mSkinAttr;
    }


    public static boolean isSupportedAttr(String attrName) {
        if (BACKGROUND.equals(attrName)) {
            return true;
        }
        if (TOPBACKGROUND.equals(attrName)) {
            return true;
        }
        if (TEXT_COLOR.equals(attrName)) {
            return true;
        }
        if (LIST_SELECTOR.equals(attrName)) {
            return true;
        }
        if (DIVIDER.equals(attrName)) {
            return true;
        }
        if (WIDTH.equals(attrName)) {
            return true;
        }
        if (HEIGHT.equals(attrName)) {
            return true;
        }
        if (DRAWABLE_WIDTH.equals(attrName)) {
            return true;
        }
        if (DRAWABLE_HEIGHT.equals(attrName)) {
            return true;
        }
        return false;
    }
}
