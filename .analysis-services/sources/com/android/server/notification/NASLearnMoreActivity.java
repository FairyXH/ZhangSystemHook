package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class NASLearnMoreActivity extends android.app.Activity {
    @Override // android.app.Activity
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLearnMoreDialog();
    }

    private void showLearnMoreDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        android.app.AlertDialog alertDialog = builder.setMessage(android.R.string.mmcc_illegal_ms_msim_template).setPositiveButton(android.R.string.ok, new android.content.DialogInterface.OnClickListener() { // from class: com.android.server.notification.NASLearnMoreActivity.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(android.content.DialogInterface dialog, int which) {
                com.android.server.notification.NASLearnMoreActivity.this.finish();
            }
        }).create();
        alertDialog.getWindow().setType(2003);
        alertDialog.show();
    }
}
