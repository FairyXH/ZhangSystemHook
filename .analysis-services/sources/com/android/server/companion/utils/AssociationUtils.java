package com.android.server.companion.utils;

/* JADX INFO: loaded from: classes.dex */
public final class AssociationUtils {
    private static final int ASSOCIATIONS_IDS_PER_USER_RANGE = 100000;

    public static int getFirstAssociationIdForUser(int userId) {
        return (100000 * userId) + 1;
    }

    public static int getLastAssociationIdForUser(int userId) {
        return (userId + 1) * 100000;
    }

    private AssociationUtils() {
    }
}
