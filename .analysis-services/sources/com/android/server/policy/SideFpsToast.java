package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public class SideFpsToast extends android.app.Dialog {
    SideFpsToast(android.content.Context context) {
        super(context);
    }

    @Override // android.app.Dialog
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.R.layout.progress_dialog_holo);
    }

    @Override // android.app.Dialog
    protected void onStart() {
        super.onStart();
        android.view.Window window = getWindow();
        android.view.WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.0f;
        windowParams.flags |= 2;
        windowParams.gravity = 80;
        window.setAttributes(windowParams);
    }

    public void setOnClickListener(android.view.View.OnClickListener listener) {
        android.widget.Button turnOffScreen = (android.widget.Button) findViewById(android.R.id.textEnd);
        if (turnOffScreen != null) {
            turnOffScreen.setOnClickListener(listener);
        }
    }

    public void addAccessibilityDelegate() {
        android.widget.Button turnOffScreen = (android.widget.Button) findViewById(android.R.id.textEnd);
        if (turnOffScreen != null) {
            turnOffScreen.setAccessibilityDelegate(new android.view.View.AccessibilityDelegate() { // from class: com.android.server.policy.SideFpsToast.1
                @Override // android.view.View.AccessibilityDelegate
                public void onInitializeAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
                    if (event.getEventType() == 65536 && com.android.server.policy.SideFpsToast.this.isShowing()) {
                        com.android.server.policy.SideFpsToast.this.dismiss();
                    }
                    super.onInitializeAccessibilityEvent(host, event);
                }
            });
        }
    }
}
