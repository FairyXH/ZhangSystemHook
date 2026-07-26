package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
final class GlobalKeyManager {
    private static final java.lang.String ATTR_COMPONENT = "component";
    private static final java.lang.String ATTR_DISPATCH_WHEN_NON_INTERACTIVE = "dispatchWhenNonInteractive";
    private static final java.lang.String ATTR_KEY_CODE = "keyCode";
    private static final java.lang.String ATTR_VERSION = "version";
    private static final int GLOBAL_KEY_FILE_VERSION = 1;
    private static final java.lang.String TAG = "GlobalKeyManager";
    private static final java.lang.String TAG_GLOBAL_KEYS = "global_keys";
    private static final java.lang.String TAG_KEY = "key";
    private final android.util.SparseArray<com.android.server.policy.GlobalKeyManager.GlobalKeyAction> mKeyMapping = new android.util.SparseArray<>();
    private boolean mBeganFromNonInteractive = false;

    public GlobalKeyManager(android.content.Context context) {
        loadGlobalKeys(context);
    }

    boolean handleGlobalKey(android.content.Context context, int keyCode, android.view.KeyEvent event) {
        com.android.server.policy.GlobalKeyManager.GlobalKeyAction action;
        if (this.mKeyMapping.size() <= 0 || (action = this.mKeyMapping.get(keyCode)) == null) {
            return false;
        }
        android.content.Intent intent = new com.android.server.policy.GlobalKeyIntent(action.mComponentName, event, this.mBeganFromNonInteractive).getIntent();
        context.sendBroadcastAsUser(intent, android.os.UserHandle.CURRENT, null);
        if (event.getAction() == 1) {
            this.mBeganFromNonInteractive = false;
        }
        return true;
    }

    boolean shouldHandleGlobalKey(int keyCode) {
        return this.mKeyMapping.get(keyCode) != null;
    }

    boolean shouldDispatchFromNonInteractive(int keyCode) {
        com.android.server.policy.GlobalKeyManager.GlobalKeyAction action = this.mKeyMapping.get(keyCode);
        if (action == null) {
            return false;
        }
        return action.mDispatchWhenNonInteractive;
    }

    void setBeganFromNonInteractive() {
        this.mBeganFromNonInteractive = true;
    }

    class GlobalKeyAction {
        private final android.content.ComponentName mComponentName;
        private final boolean mDispatchWhenNonInteractive;

        GlobalKeyAction(java.lang.String componentName, java.lang.String dispatchWhenNonInteractive) {
            this.mComponentName = android.content.ComponentName.unflattenFromString(componentName);
            this.mDispatchWhenNonInteractive = java.lang.Boolean.parseBoolean(dispatchWhenNonInteractive);
        }
    }

    private void loadGlobalKeys(android.content.Context context) {
        try {
            android.content.res.XmlResourceParser parser = context.getResources().getXml(android.R.xml.global_keys);
            try {
                com.android.internal.util.XmlUtils.beginDocument(parser, TAG_GLOBAL_KEYS);
                int version = parser.getAttributeIntValue(null, ATTR_VERSION, 0);
                if (1 == version) {
                    while (true) {
                        com.android.internal.util.XmlUtils.nextElement(parser);
                        java.lang.String element = parser.getName();
                        if (element == null) {
                            break;
                        }
                        if (TAG_KEY.equals(element)) {
                            java.lang.String keyCodeName = parser.getAttributeValue(null, ATTR_KEY_CODE);
                            java.lang.String componentName = parser.getAttributeValue(null, ATTR_COMPONENT);
                            java.lang.String dispatchWhenNonInteractive = parser.getAttributeValue(null, ATTR_DISPATCH_WHEN_NON_INTERACTIVE);
                            if (keyCodeName == null || componentName == null) {
                                android.util.Log.wtf(TAG, "Failed to parse global keys entry: " + parser.getText());
                            } else {
                                int keyCode = android.view.KeyEvent.keyCodeFromString(keyCodeName);
                                if (keyCode == 0) {
                                    android.util.Log.wtf(TAG, "Global keys entry does not map to a valid key code: " + keyCodeName);
                                } else {
                                    this.mKeyMapping.put(keyCode, new com.android.server.policy.GlobalKeyManager.GlobalKeyAction(componentName, dispatchWhenNonInteractive));
                                }
                            }
                        }
                    }
                }
                if (parser != null) {
                    parser.close();
                }
            } catch (java.lang.Throwable th) {
                if (parser != null) {
                    try {
                        parser.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (android.content.res.Resources.NotFoundException e) {
            android.util.Log.wtf(TAG, "global keys file not found", e);
        } catch (java.io.IOException e2) {
            android.util.Log.e(TAG, "I/O exception reading global keys file", e2);
        } catch (org.xmlpull.v1.XmlPullParserException e3) {
            android.util.Log.wtf(TAG, "XML parser exception reading global keys file", e3);
        }
    }

    public void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        int numKeys = this.mKeyMapping.size();
        if (numKeys == 0) {
            pw.print(prefix);
            pw.println("mKeyMapping.size=0");
            return;
        }
        pw.print(prefix);
        pw.println("mKeyMapping={");
        for (int i = 0; i < numKeys; i++) {
            pw.print("  ");
            pw.print(prefix);
            pw.print(android.view.KeyEvent.keyCodeToString(this.mKeyMapping.keyAt(i)));
            pw.print("=");
            pw.print(this.mKeyMapping.valueAt(i).mComponentName.flattenToString());
            pw.print(",dispatchWhenNonInteractive=");
            pw.println(this.mKeyMapping.valueAt(i).mDispatchWhenNonInteractive);
        }
        pw.print(prefix);
        pw.println("}");
    }
}
