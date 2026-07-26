package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public final class LegacyPermission {
    private static final java.lang.String ATTR_NAME = "name";
    private static final java.lang.String ATTR_PACKAGE = "package";
    private static final java.lang.String TAG_ITEM = "item";
    public static final int TYPE_CONFIG = 1;
    public static final int TYPE_DYNAMIC = 2;
    public static final int TYPE_MANIFEST = 0;
    private final int[] mGids;
    private final android.content.pm.PermissionInfo mPermissionInfo;
    private final int mType;
    private final int mUid;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface PermissionType {
    }

    public LegacyPermission(android.content.pm.PermissionInfo permissionInfo, int type, int uid, int[] gids) {
        this.mPermissionInfo = permissionInfo;
        this.mType = type;
        this.mUid = uid;
        this.mGids = gids;
    }

    private LegacyPermission(java.lang.String name, java.lang.String packageName, int type) {
        this.mPermissionInfo = new android.content.pm.PermissionInfo();
        this.mPermissionInfo.name = name;
        this.mPermissionInfo.packageName = packageName;
        this.mPermissionInfo.protectionLevel = 2;
        this.mType = type;
        this.mUid = 0;
        this.mGids = libcore.util.EmptyArray.INT;
    }

    public android.content.pm.PermissionInfo getPermissionInfo() {
        return this.mPermissionInfo;
    }

    public int getType() {
        return this.mType;
    }

    public static boolean read(java.util.Map<java.lang.String, com.android.server.pm.permission.LegacyPermission> out, com.android.modules.utils.TypedXmlPullParser parser) {
        java.lang.String tagName = parser.getName();
        if (!tagName.equals("item")) {
            return false;
        }
        java.lang.String name = parser.getAttributeValue((java.lang.String) null, "name");
        java.lang.String packageName = parser.getAttributeValue((java.lang.String) null, "package");
        java.lang.String ptype = parser.getAttributeValue((java.lang.String) null, "type");
        if (name == null || packageName == null) {
            com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: permissions has no name at " + parser.getPositionDescription());
            return false;
        }
        boolean dynamic = "dynamic".equals(ptype);
        com.android.server.pm.permission.LegacyPermission bp = out.get(name);
        if (bp == null || bp.mType != 1) {
            bp = new com.android.server.pm.permission.LegacyPermission(name.intern(), packageName, dynamic ? 2 : 0);
        }
        bp.mPermissionInfo.protectionLevel = readInt(parser, null, "protection", 0);
        bp.mPermissionInfo.protectionLevel = android.content.pm.PermissionInfo.fixProtectionLevel(bp.mPermissionInfo.protectionLevel);
        if (dynamic) {
            bp.mPermissionInfo.icon = readInt(parser, null, "icon", 0);
            bp.mPermissionInfo.nonLocalizedLabel = parser.getAttributeValue((java.lang.String) null, "label");
        }
        out.put(bp.mPermissionInfo.name, bp);
        return true;
    }

    private static int readInt(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String namespace, java.lang.String name, int defaultValue) {
        return parser.getAttributeInt(namespace, name, defaultValue);
    }

    public void write(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
        if (this.mPermissionInfo.packageName == null) {
            return;
        }
        serializer.startTag((java.lang.String) null, "item");
        serializer.attribute((java.lang.String) null, "name", this.mPermissionInfo.name);
        serializer.attribute((java.lang.String) null, "package", this.mPermissionInfo.packageName);
        if (this.mPermissionInfo.protectionLevel != 0) {
            serializer.attributeInt((java.lang.String) null, "protection", this.mPermissionInfo.protectionLevel);
        }
        if (this.mType == 2) {
            serializer.attribute((java.lang.String) null, "type", "dynamic");
            if (this.mPermissionInfo.icon != 0) {
                serializer.attributeInt((java.lang.String) null, "icon", this.mPermissionInfo.icon);
            }
            if (this.mPermissionInfo.nonLocalizedLabel != null) {
                serializer.attribute((java.lang.String) null, "label", this.mPermissionInfo.nonLocalizedLabel.toString());
            }
        }
        serializer.endTag((java.lang.String) null, "item");
    }

    public boolean dump(java.io.PrintWriter pw, java.lang.String packageName, java.util.Set<java.lang.String> permissionNames, boolean readEnforced, boolean printedSomething, com.android.server.pm.DumpState dumpState) {
        if (packageName != null && !packageName.equals(this.mPermissionInfo.packageName)) {
            return false;
        }
        if (permissionNames != null && !permissionNames.contains(this.mPermissionInfo.name)) {
            return false;
        }
        if (!printedSomething) {
            if (dumpState.onTitlePrinted()) {
                pw.println();
            }
            pw.println("Permissions:");
        }
        pw.print("  Permission [");
        pw.print(this.mPermissionInfo.name);
        pw.print("] (");
        pw.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        pw.println("):");
        pw.print("    sourcePackage=");
        pw.println(this.mPermissionInfo.packageName);
        pw.print("    uid=");
        pw.print(this.mUid);
        pw.print(" gids=");
        pw.print(java.util.Arrays.toString(this.mGids));
        pw.print(" type=");
        pw.print(this.mType);
        pw.print(" prot=");
        pw.println(android.content.pm.PermissionInfo.protectionToString(this.mPermissionInfo.protectionLevel));
        if (this.mPermissionInfo != null) {
            pw.print("    perm=");
            pw.println(this.mPermissionInfo);
            if ((this.mPermissionInfo.flags & 1073741824) == 0 || (this.mPermissionInfo.flags & 2) != 0) {
                pw.print("    flags=0x");
                pw.println(java.lang.Integer.toHexString(this.mPermissionInfo.flags));
            }
        }
        if (java.util.Objects.equals(this.mPermissionInfo.name, "android.permission.READ_EXTERNAL_STORAGE")) {
            pw.print("    enforced=");
            pw.println(readEnforced);
            return true;
        }
        return true;
    }
}
