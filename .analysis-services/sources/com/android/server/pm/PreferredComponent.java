package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class PreferredComponent {
    private static final java.lang.String ATTR_ALWAYS = "always";
    private static final java.lang.String ATTR_MATCH = "match";
    private static final java.lang.String ATTR_NAME = "name";
    private static final java.lang.String ATTR_SET = "set";
    private static final java.lang.String TAG_SET = "set";
    public final boolean mAlways;
    private final com.android.server.pm.PreferredComponent.Callbacks mCallbacks;
    public final android.content.ComponentName mComponent;
    public final int mMatch;
    private java.lang.String mParseError;
    final java.lang.String[] mSetClasses;
    final java.lang.String[] mSetComponents;
    final java.lang.String[] mSetPackages;
    final java.lang.String mShortComponent;

    public interface Callbacks {
        boolean onReadTag(java.lang.String str, com.android.modules.utils.TypedXmlPullParser typedXmlPullParser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException;
    }

    public PreferredComponent(com.android.server.pm.PreferredComponent.Callbacks callbacks, int match, android.content.ComponentName[] set, android.content.ComponentName component, boolean always) {
        this.mCallbacks = callbacks;
        this.mMatch = 268369920 & match;
        this.mComponent = component;
        this.mAlways = always;
        this.mShortComponent = component.flattenToShortString();
        this.mParseError = null;
        if (set != null) {
            int N = set.length;
            java.lang.String[] myPackages = new java.lang.String[N];
            java.lang.String[] myClasses = new java.lang.String[N];
            java.lang.String[] myComponents = new java.lang.String[N];
            for (int i = 0; i < N; i++) {
                android.content.ComponentName cn = set[i];
                if (cn == null) {
                    this.mSetPackages = null;
                    this.mSetClasses = null;
                    this.mSetComponents = null;
                    return;
                } else {
                    myPackages[i] = cn.getPackageName().intern();
                    myClasses[i] = cn.getClassName().intern();
                    myComponents[i] = cn.flattenToShortString();
                }
            }
            this.mSetPackages = myPackages;
            this.mSetClasses = myClasses;
            this.mSetComponents = myComponents;
            return;
        }
        this.mSetPackages = null;
        this.mSetClasses = null;
        this.mSetComponents = null;
    }

    public PreferredComponent(com.android.server.pm.PreferredComponent.Callbacks callbacks, com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        this.mCallbacks = callbacks;
        java.lang.String str = null;
        this.mShortComponent = parser.getAttributeValue((java.lang.String) null, "name");
        this.mComponent = android.content.ComponentName.unflattenFromString(this.mShortComponent);
        if (this.mComponent == null) {
            this.mParseError = "Bad activity name " + this.mShortComponent;
        }
        this.mMatch = parser.getAttributeIntHex((java.lang.String) null, ATTR_MATCH, 0);
        int setCount = parser.getAttributeInt((java.lang.String) null, "set", 0);
        int i = 1;
        this.mAlways = parser.getAttributeBoolean((java.lang.String) null, ATTR_ALWAYS, true);
        java.lang.String[] myPackages = setCount > 0 ? new java.lang.String[setCount] : null;
        java.lang.String[] myClasses = setCount > 0 ? new java.lang.String[setCount] : null;
        java.lang.String[] myComponents = setCount > 0 ? new java.lang.String[setCount] : null;
        int setPos = 0;
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == i || (type == 3 && parser.getDepth() <= outerDepth)) {
                break;
            }
            if (type == 3 || type == 4) {
                str = null;
                i = 1;
            } else {
                java.lang.String tagName = parser.getName();
                if (!tagName.equals("set")) {
                    if (!this.mCallbacks.onReadTag(tagName, parser)) {
                        android.util.Slog.w("PreferredComponent", "Unknown element: " + parser.getName());
                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                    }
                } else {
                    java.lang.String name = parser.getAttributeValue(str, "name");
                    if (name == null) {
                        if (this.mParseError == null) {
                            this.mParseError = "No name in set tag in preferred activity " + this.mShortComponent;
                        }
                    } else if (setPos >= setCount) {
                        if (this.mParseError == null) {
                            this.mParseError = "Too many set tags in preferred activity " + this.mShortComponent;
                        }
                    } else {
                        android.content.ComponentName cn = android.content.ComponentName.unflattenFromString(name);
                        if (cn == null) {
                            if (this.mParseError == null) {
                                this.mParseError = "Bad set name " + name + " in preferred activity " + this.mShortComponent;
                            }
                        } else {
                            myPackages[setPos] = cn.getPackageName();
                            myClasses[setPos] = cn.getClassName();
                            myComponents[setPos] = name;
                            setPos++;
                        }
                    }
                    com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                }
                str = null;
                i = 1;
            }
        }
        if (setPos != setCount && this.mParseError == null) {
            this.mParseError = "Not enough set tags (expected " + setCount + " but found " + setPos + ") in " + this.mShortComponent;
        }
        this.mSetPackages = myPackages;
        this.mSetClasses = myClasses;
        this.mSetComponents = myComponents;
    }

    public java.lang.String getParseError() {
        return this.mParseError;
    }

    public void writeToXml(com.android.modules.utils.TypedXmlSerializer serializer, boolean full) throws java.io.IOException {
        int NS = this.mSetClasses != null ? this.mSetClasses.length : 0;
        serializer.attribute((java.lang.String) null, "name", this.mShortComponent);
        if (full) {
            if (this.mMatch != 0) {
                serializer.attributeIntHex((java.lang.String) null, ATTR_MATCH, this.mMatch);
            }
            serializer.attributeBoolean((java.lang.String) null, ATTR_ALWAYS, this.mAlways);
            serializer.attributeInt((java.lang.String) null, "set", NS);
            for (int s = 0; s < NS; s++) {
                serializer.startTag((java.lang.String) null, "set");
                serializer.attribute((java.lang.String) null, "name", this.mSetComponents[s]);
                serializer.endTag((java.lang.String) null, "set");
            }
        }
    }

    public boolean sameSet(java.util.List<android.content.pm.ResolveInfo> query, boolean excludeSetupWizardPackage, int userId) {
        boolean z;
        java.util.List<android.content.pm.ResolveInfo> list = query;
        boolean z2 = false;
        if (this.mSetPackages == null) {
            return list == null;
        }
        if (list == null) {
            return false;
        }
        int NQ = query.size();
        int NS = this.mSetPackages.length;
        android.content.pm.PackageManagerInternal pmi = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        java.lang.String setupWizardPackageName = pmi.getSetupWizardPackageName();
        int numMatch = 0;
        int i = 0;
        while (i < NQ) {
            android.content.pm.ResolveInfo ri = list.get(i);
            android.content.pm.ActivityInfo ai = ri.activityInfo;
            boolean good = false;
            if (excludeSetupWizardPackage && ai.packageName.equals(setupWizardPackageName)) {
                z = z2;
            } else {
                com.android.server.pm.pkg.PackageStateInternal ps = pmi.getPackageStateInternal(ai.packageName);
                if (ps == null) {
                    z = z2;
                } else {
                    com.android.server.pm.pkg.PackageUserStateInternal pkgUserState = ps.getUserStates().get(userId);
                    if (pkgUserState == null) {
                        z = z2;
                    } else if (pkgUserState.getInstallReason() == 3) {
                        z = false;
                    } else {
                        int j = 0;
                        while (true) {
                            if (j < NS) {
                                if (!this.mSetPackages[j].equals(ai.packageName) || !this.mSetClasses[j].equals(ai.name)) {
                                    j++;
                                } else {
                                    numMatch++;
                                    good = true;
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        if (!good) {
                            return false;
                        }
                        z = false;
                    }
                }
            }
            i++;
            z2 = z;
            list = query;
        }
        boolean z3 = z2;
        if (numMatch == NS) {
            return true;
        }
        return z3;
    }

    public boolean sameSet(android.content.ComponentName[] comps) {
        if (this.mSetPackages == null) {
            return false;
        }
        int NS = this.mSetPackages.length;
        int numMatch = 0;
        for (android.content.ComponentName cn : comps) {
            boolean good = false;
            int j = 0;
            while (true) {
                if (j >= NS) {
                    break;
                }
                if (!this.mSetPackages[j].equals(cn.getPackageName()) || !this.mSetClasses[j].equals(cn.getClassName())) {
                    j++;
                } else {
                    numMatch++;
                    good = true;
                    break;
                }
            }
            if (!good) {
                return false;
            }
        }
        return numMatch == NS;
    }

    public boolean sameSet(com.android.server.pm.PreferredComponent pc) {
        if (this.mSetPackages == null || pc == null || pc.mSetPackages == null || !sameComponent(pc.mComponent)) {
            return false;
        }
        int otherPackageCount = pc.mSetPackages.length;
        int packageCount = this.mSetPackages.length;
        if (otherPackageCount != packageCount) {
            return false;
        }
        for (int i = 0; i < packageCount; i++) {
            if (!this.mSetPackages[i].equals(pc.mSetPackages[i]) || !this.mSetClasses[i].equals(pc.mSetClasses[i])) {
                return false;
            }
        }
        return true;
    }

    private boolean sameComponent(android.content.ComponentName comp) {
        return this.mComponent != null && comp != null && this.mComponent.getPackageName().equals(comp.getPackageName()) && this.mComponent.getClassName().equals(comp.getClassName());
    }

    public boolean isSuperset(java.util.List<android.content.pm.ResolveInfo> query, boolean excludeSetupWizardPackage) {
        if (this.mSetPackages == null) {
            return query == null;
        }
        if (query == null) {
            return true;
        }
        int NQ = query.size();
        int NS = this.mSetPackages.length;
        if (!excludeSetupWizardPackage && NS < NQ) {
            return false;
        }
        android.content.pm.PackageManagerInternal pmi = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        java.lang.String setupWizardPackageName = pmi.getSetupWizardPackageName();
        for (int i = 0; i < NQ; i++) {
            android.content.pm.ResolveInfo ri = query.get(i);
            android.content.pm.ActivityInfo ai = ri.activityInfo;
            boolean foundMatch = false;
            if (!excludeSetupWizardPackage || !ai.packageName.equals(setupWizardPackageName)) {
                int j = 0;
                while (true) {
                    if (j >= NS) {
                        break;
                    }
                    if (!this.mSetPackages[j].equals(ai.packageName) || !this.mSetClasses[j].equals(ai.name)) {
                        j++;
                    } else {
                        foundMatch = true;
                        break;
                    }
                }
                if (!foundMatch) {
                    return false;
                }
            }
        }
        return true;
    }

    public android.content.ComponentName[] discardObsoleteComponents(java.util.List<android.content.pm.ResolveInfo> query) {
        if (this.mSetPackages == null || query == null) {
            return new android.content.ComponentName[0];
        }
        int NQ = query.size();
        int NS = this.mSetPackages.length;
        java.util.ArrayList<android.content.ComponentName> aliveComponents = new java.util.ArrayList<>();
        for (int i = 0; i < NQ; i++) {
            android.content.pm.ResolveInfo ri = query.get(i);
            android.content.pm.ActivityInfo ai = ri.activityInfo;
            int j = 0;
            while (true) {
                if (j >= NS) {
                    break;
                }
                if (!this.mSetPackages[j].equals(ai.packageName) || !this.mSetClasses[j].equals(ai.name)) {
                    j++;
                } else {
                    aliveComponents.add(new android.content.ComponentName(this.mSetPackages[j], this.mSetClasses[j]));
                    break;
                }
            }
        }
        int i2 = aliveComponents.size();
        return (android.content.ComponentName[]) aliveComponents.toArray(new android.content.ComponentName[i2]);
    }

    public void dump(java.io.PrintWriter out, java.lang.String prefix, java.lang.Object ident) {
        out.print(prefix);
        out.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(ident)));
        out.print(' ');
        out.println(this.mShortComponent);
        out.print(prefix);
        out.print(" mMatch=0x");
        out.print(java.lang.Integer.toHexString(this.mMatch));
        out.print(" mAlways=");
        out.println(this.mAlways);
        if (this.mSetComponents != null) {
            out.print(prefix);
            out.println("  Selected from:");
            for (int i = 0; i < this.mSetComponents.length; i++) {
                out.print(prefix);
                out.print("    ");
                out.println(this.mSetComponents[i]);
            }
        }
    }
}
