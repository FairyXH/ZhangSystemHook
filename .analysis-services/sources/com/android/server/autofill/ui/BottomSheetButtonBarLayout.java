package com.android.server.autofill.ui;

/* JADX INFO: loaded from: classes.dex */
public class BottomSheetButtonBarLayout extends com.android.internal.widget.ButtonBarLayout {
    public BottomSheetButtonBarLayout(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        android.view.View spacer = findViewById(android.R.id.alerted_icon);
        if (spacer == null) {
            return;
        }
        if (isStacked()) {
            spacer.getLayoutParams().width = 0;
            spacer.getLayoutParams().height = getResources().getDimensionPixelSize(android.R.dimen.alertDialog_material_letter_spacing_body_1);
            setGravity(8388629);
            return;
        }
        spacer.getLayoutParams().width = getResources().getDimensionPixelSize(android.R.dimen.alertDialog_material_letter_spacing_title);
        spacer.getLayoutParams().height = 0;
        setGravity(16);
    }

    private boolean isStacked() {
        return getOrientation() == 1;
    }
}
