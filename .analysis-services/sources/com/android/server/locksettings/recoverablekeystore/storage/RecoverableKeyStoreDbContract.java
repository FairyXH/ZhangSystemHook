package com.android.server.locksettings.recoverablekeystore.storage;

/* JADX INFO: loaded from: classes2.dex */
class RecoverableKeyStoreDbContract {
    RecoverableKeyStoreDbContract() {
    }

    static class KeysEntry implements android.provider.BaseColumns {
        static final java.lang.String COLUMN_NAME_ALIAS = "alias";
        static final java.lang.String COLUMN_NAME_GENERATION_ID = "platform_key_generation_id";
        static final java.lang.String COLUMN_NAME_KEY_METADATA = "key_metadata";
        static final java.lang.String COLUMN_NAME_LAST_SYNCED_AT = "last_synced_at";
        static final java.lang.String COLUMN_NAME_NONCE = "nonce";
        static final java.lang.String COLUMN_NAME_RECOVERY_STATUS = "recovery_status";
        static final java.lang.String COLUMN_NAME_UID = "uid";
        static final java.lang.String COLUMN_NAME_USER_ID = "user_id";
        static final java.lang.String COLUMN_NAME_WRAPPED_KEY = "wrapped_key";
        static final java.lang.String TABLE_NAME = "keys";

        KeysEntry() {
        }
    }

    static class UserMetadataEntry implements android.provider.BaseColumns {
        static final java.lang.String COLUMN_NAME_BAD_REMOTE_GUESS_COUNTER = "bad_remote_guess_counter";
        static final java.lang.String COLUMN_NAME_PLATFORM_KEY_GENERATION_ID = "platform_key_generation_id";
        static final java.lang.String COLUMN_NAME_USER_ID = "user_id";
        static final java.lang.String COLUMN_NAME_USER_SERIAL_NUMBER = "user_serial_number";
        static final java.lang.String TABLE_NAME = "user_metadata";

        UserMetadataEntry() {
        }
    }

    static class RecoveryServiceMetadataEntry implements android.provider.BaseColumns {
        static final java.lang.String COLUMN_NAME_ACTIVE_ROOT_OF_TRUST = "active_root_of_trust";
        static final java.lang.String COLUMN_NAME_CERT_PATH = "cert_path";
        static final java.lang.String COLUMN_NAME_CERT_SERIAL = "cert_serial";
        static final java.lang.String COLUMN_NAME_COUNTER_ID = "counter_id";
        static final java.lang.String COLUMN_NAME_PUBLIC_KEY = "public_key";
        static final java.lang.String COLUMN_NAME_SECRET_TYPES = "secret_types";
        static final java.lang.String COLUMN_NAME_SERVER_PARAMS = "server_params";
        static final java.lang.String COLUMN_NAME_SHOULD_CREATE_SNAPSHOT = "should_create_snapshot";
        static final java.lang.String COLUMN_NAME_SNAPSHOT_VERSION = "snapshot_version";
        static final java.lang.String COLUMN_NAME_UID = "uid";
        static final java.lang.String COLUMN_NAME_USER_ID = "user_id";
        static final java.lang.String TABLE_NAME = "recovery_service_metadata";

        RecoveryServiceMetadataEntry() {
        }
    }

    static class RootOfTrustEntry implements android.provider.BaseColumns {
        static final java.lang.String COLUMN_NAME_CERT_PATH = "cert_path";
        static final java.lang.String COLUMN_NAME_CERT_SERIAL = "cert_serial";
        static final java.lang.String COLUMN_NAME_ROOT_ALIAS = "root_alias";
        static final java.lang.String COLUMN_NAME_UID = "uid";
        static final java.lang.String COLUMN_NAME_USER_ID = "user_id";
        static final java.lang.String TABLE_NAME = "root_of_trust";

        RootOfTrustEntry() {
        }
    }
}
