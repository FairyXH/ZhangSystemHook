package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class PackageManagerShellCommandDataLoader extends android.service.dataloader.DataLoaderService {
    private static final char ARGS_DELIM = '&';
    private static final int INVALID_SHELL_COMMAND_ID = -1;
    private static final java.lang.String PACKAGE = "android";
    private static final java.lang.String SHELL_COMMAND_ID_PREFIX = "shellCommandId=";
    private static final java.lang.String STDIN_PATH = "-";
    public static final java.lang.String TAG = "PackageManagerShellCommandDataLoader";
    private static final int TOO_MANY_PENDING_SHELL_COMMANDS = 10;
    private static final java.lang.String CLASS = com.android.server.pm.PackageManagerShellCommandDataLoader.class.getName();
    static final java.security.SecureRandom sRandom = new java.security.SecureRandom();
    static final android.util.SparseArray<java.lang.ref.WeakReference<android.os.ShellCommand>> sShellCommands = new android.util.SparseArray<>();

    private static native void nativeInitialize();

    private static java.lang.String getDataLoaderParamsArgs(android.os.ShellCommand shellCommand) {
        int commandId;
        nativeInitialize();
        synchronized (sShellCommands) {
            for (int i = sShellCommands.size() - 1; i >= 0; i--) {
                java.lang.ref.WeakReference<android.os.ShellCommand> oldRef = sShellCommands.valueAt(i);
                if (oldRef.get() == null) {
                    sShellCommands.removeAt(i);
                }
            }
            if (sShellCommands.size() > 10) {
                android.util.Slog.e(TAG, "Too many pending shell commands: " + sShellCommands.size());
            }
            do {
                commandId = sRandom.nextInt(2147483646) + 1;
            } while (sShellCommands.contains(commandId));
            sShellCommands.put(commandId, new java.lang.ref.WeakReference<>(shellCommand));
        }
        return SHELL_COMMAND_ID_PREFIX + commandId;
    }

    static android.content.pm.DataLoaderParams getStreamingDataLoaderParams(android.os.ShellCommand shellCommand) {
        return android.content.pm.DataLoaderParams.forStreaming(new android.content.ComponentName("android", CLASS), getDataLoaderParamsArgs(shellCommand));
    }

    static android.content.pm.DataLoaderParams getIncrementalDataLoaderParams(android.os.ShellCommand shellCommand) {
        return android.content.pm.DataLoaderParams.forIncremental(new android.content.ComponentName("android", CLASS), getDataLoaderParamsArgs(shellCommand));
    }

    private static int extractShellCommandId(java.lang.String args) {
        int sessionIdIdx = args.indexOf(SHELL_COMMAND_ID_PREFIX);
        if (sessionIdIdx < 0) {
            android.util.Slog.e(TAG, "Missing shell command id param.");
            return -1;
        }
        int sessionIdIdx2 = sessionIdIdx + SHELL_COMMAND_ID_PREFIX.length();
        int delimIdx = args.indexOf(38, sessionIdIdx2);
        try {
            if (delimIdx < 0) {
                return java.lang.Integer.parseInt(args.substring(sessionIdIdx2));
            }
            return java.lang.Integer.parseInt(args.substring(sessionIdIdx2, delimIdx));
        } catch (java.lang.NumberFormatException e) {
            android.util.Slog.e(TAG, "Incorrect shell command id format.", e);
            return -1;
        }
    }

    public static class Metadata {
        static final byte ARCHIVED = 4;
        static final byte DATA_ONLY_STREAMING = 2;
        static final byte LOCAL_FILE = 1;
        static final byte STDIN = 0;
        static final byte STREAMING = 3;
        private static final java.util.concurrent.atomic.AtomicLong sGlobalSalt = new java.util.concurrent.atomic.AtomicLong(new java.security.SecureRandom().nextLong());
        private final byte[] mData;
        private final byte mMode;
        private final java.lang.String mSalt;

        private static java.lang.Long nextGlobalSalt() {
            return java.lang.Long.valueOf(sGlobalSalt.incrementAndGet());
        }

        static com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata forStdIn(java.lang.String fileId) {
            return new com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata((byte) 0, fileId);
        }

        public static com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata forLocalFile(java.lang.String filePath) {
            return new com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata((byte) 1, filePath, nextGlobalSalt().toString());
        }

        public static com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata forArchived(android.content.pm.ArchivedPackageParcel archivedPackage) {
            return new com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata((byte) 4, writeArchivedPackageParcel(archivedPackage), (java.lang.String) null);
        }

        static com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata forDataOnlyStreaming(java.lang.String fileId) {
            return new com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata((byte) 2, fileId);
        }

        static com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata forStreaming(java.lang.String fileId) {
            return new com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata((byte) 3, fileId);
        }

        private Metadata(byte mode, java.lang.String data) {
            this(mode, data, (java.lang.String) null);
        }

        private Metadata(byte mode, java.lang.String data, java.lang.String salt) {
            this(mode, (data != null ? data : "").getBytes(java.nio.charset.StandardCharsets.UTF_8), salt);
        }

        private Metadata(byte mode, byte[] data, java.lang.String salt) {
            this.mMode = mode;
            this.mData = data;
            this.mSalt = salt;
        }

        static com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata fromByteArray(byte[] bytes) throws java.io.IOException {
            byte[] data;
            java.lang.String salt;
            if (bytes == null || bytes.length < 5) {
                return null;
            }
            byte mode = bytes[0];
            int offset = 0 + 1;
            switch (mode) {
                case 1:
                    int dataSize = java.nio.ByteBuffer.wrap(bytes, offset, 4).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
                    int offset2 = offset + 4;
                    data = java.util.Arrays.copyOfRange(bytes, offset2, offset2 + dataSize);
                    int offset3 = offset2 + dataSize;
                    salt = new java.lang.String(bytes, offset3, bytes.length - offset3, java.nio.charset.StandardCharsets.UTF_8);
                    break;
                default:
                    data = java.util.Arrays.copyOfRange(bytes, offset, bytes.length);
                    salt = null;
                    break;
            }
            return new com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata(mode, data, salt);
        }

        public byte[] toByteArray() {
            byte[] dataBytes = this.mData;
            switch (this.mMode) {
                case 1:
                    int dataSize = dataBytes.length;
                    byte[] saltBytes = this.mSalt.getBytes(java.nio.charset.StandardCharsets.UTF_8);
                    byte[] result = new byte[dataSize + 5 + saltBytes.length];
                    result[0] = this.mMode;
                    int offset = 0 + 1;
                    java.nio.ByteBuffer.wrap(result, offset, 4).order(java.nio.ByteOrder.LITTLE_ENDIAN).putInt(dataSize);
                    int offset2 = offset + 4;
                    java.lang.System.arraycopy(dataBytes, 0, result, offset2, dataSize);
                    java.lang.System.arraycopy(saltBytes, 0, result, offset2 + dataSize, saltBytes.length);
                    return result;
                default:
                    byte[] result2 = new byte[dataBytes.length + 1];
                    result2[0] = this.mMode;
                    java.lang.System.arraycopy(dataBytes, 0, result2, 1, dataBytes.length);
                    return result2;
            }
        }

        byte getMode() {
            return this.mMode;
        }

        byte[] getData() {
            return this.mData;
        }

        android.content.pm.ArchivedPackageParcel getArchivedPackage() {
            if (getMode() != 4) {
                throw new java.lang.IllegalStateException("Not an archived package metadata.");
            }
            return readArchivedPackageParcel(this.mData);
        }

        static android.content.pm.ArchivedPackageParcel readArchivedPackageParcel(byte[] bytes) {
            android.os.Parcel parcel = android.os.Parcel.obtain();
            try {
                parcel.unmarshall(bytes, 0, bytes.length);
                parcel.setDataPosition(0);
                android.content.pm.ArchivedPackageParcel result = parcel.readParcelable(android.content.pm.ArchivedPackageParcel.class.getClassLoader());
                return result;
            } finally {
                parcel.recycle();
            }
        }

        static byte[] writeArchivedPackageParcel(android.content.pm.ArchivedPackageParcel archivedPackage) {
            android.os.Parcel parcel = android.os.Parcel.obtain();
            try {
                parcel.writeParcelable(archivedPackage, 0);
                return parcel.marshall();
            } finally {
                parcel.recycle();
            }
        }
    }

    private static class DataLoader implements android.service.dataloader.DataLoaderService.DataLoader {
        private android.service.dataloader.DataLoaderService.FileSystemConnector mConnector;
        private android.content.pm.DataLoaderParams mParams;

        private DataLoader() {
            this.mParams = null;
            this.mConnector = null;
        }

        public boolean onCreate(android.content.pm.DataLoaderParams dataLoaderParams, android.service.dataloader.DataLoaderService.FileSystemConnector connector) {
            this.mParams = dataLoaderParams;
            this.mConnector = connector;
            return true;
        }

        public boolean onPrepareImage(java.util.Collection<android.content.pm.InstallationFile> addedFiles, java.util.Collection<java.lang.String> removedFiles) throws java.lang.Throwable {
            android.os.ShellCommand shellCommand = com.android.server.pm.PackageManagerShellCommandDataLoader.lookupShellCommand(this.mParams.getArguments());
            try {
                for (android.content.pm.InstallationFile file : addedFiles) {
                    com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata metadata = com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata.fromByteArray(file.getMetadata());
                    if (metadata == null) {
                        android.util.Slog.e(com.android.server.pm.PackageManagerShellCommandDataLoader.TAG, "Invalid metadata for file: " + file.getName());
                        return false;
                    }
                    switch (metadata.getMode()) {
                        case 0:
                            if (shellCommand == null) {
                                android.util.Slog.e(com.android.server.pm.PackageManagerShellCommandDataLoader.TAG, "Missing shell command for Metadata.STDIN.");
                                return false;
                            }
                            android.os.ParcelFileDescriptor inFd = com.android.server.pm.PackageManagerShellCommandDataLoader.getStdInPFD(shellCommand);
                            this.mConnector.writeData(file.getName(), 0L, file.getLengthBytes(), inFd);
                            break;
                            break;
                        case 1:
                            if (shellCommand == null) {
                                android.util.Slog.e(com.android.server.pm.PackageManagerShellCommandDataLoader.TAG, "Missing shell command for Metadata.LOCAL_FILE.");
                                return false;
                            }
                            android.os.ParcelFileDescriptor incomingFd = null;
                            try {
                                java.lang.String filePath = new java.lang.String(metadata.getData(), java.nio.charset.StandardCharsets.UTF_8);
                                android.os.ParcelFileDescriptor incomingFd2 = com.android.server.pm.PackageManagerShellCommandDataLoader.getLocalFilePFD(shellCommand, filePath);
                                try {
                                    this.mConnector.writeData(file.getName(), 0L, incomingFd2.getStatSize(), incomingFd2);
                                    libcore.io.IoUtils.closeQuietly(incomingFd2);
                                } catch (java.lang.Throwable th) {
                                    th = th;
                                    incomingFd = incomingFd2;
                                    libcore.io.IoUtils.closeQuietly(incomingFd);
                                    throw th;
                                }
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                            }
                            break;
                            break;
                        case 2:
                        case 3:
                        default:
                            android.util.Slog.e(com.android.server.pm.PackageManagerShellCommandDataLoader.TAG, "Unsupported metadata mode: " + ((int) metadata.getMode()));
                            return false;
                        case 4:
                            break;
                    }
                }
                return true;
            } catch (java.io.IOException e) {
                android.util.Slog.e(com.android.server.pm.PackageManagerShellCommandDataLoader.TAG, "Exception while streaming files", e);
                return false;
            }
        }
    }

    static android.os.ShellCommand lookupShellCommand(java.lang.String args) {
        java.lang.ref.WeakReference<android.os.ShellCommand> shellCommandRef;
        int commandId = extractShellCommandId(args);
        if (commandId == -1) {
            return null;
        }
        synchronized (sShellCommands) {
            shellCommandRef = sShellCommands.get(commandId, null);
        }
        android.os.ShellCommand shellCommand = shellCommandRef != null ? shellCommandRef.get() : null;
        return shellCommand;
    }

    static android.os.ParcelFileDescriptor getStdInPFD(android.os.ShellCommand shellCommand) {
        try {
            return android.os.ParcelFileDescriptor.dup(shellCommand.getInFileDescriptor());
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Exception while obtaining STDIN fd", e);
            return null;
        }
    }

    static android.os.ParcelFileDescriptor getLocalFilePFD(android.os.ShellCommand shellCommand, java.lang.String filePath) {
        return shellCommand.openFileForSystem(filePath, com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_SHORT_CMD);
    }

    static int getStdIn(android.os.ShellCommand shellCommand) {
        android.os.ParcelFileDescriptor pfd = getStdInPFD(shellCommand);
        if (pfd == null) {
            return -1;
        }
        return pfd.detachFd();
    }

    static int getLocalFile(android.os.ShellCommand shellCommand, java.lang.String filePath) {
        android.os.ParcelFileDescriptor pfd = getLocalFilePFD(shellCommand, filePath);
        if (pfd == null) {
            return -1;
        }
        return pfd.detachFd();
    }

    public android.service.dataloader.DataLoaderService.DataLoader onCreateDataLoader(android.content.pm.DataLoaderParams dataLoaderParams) {
        if (dataLoaderParams.getType() == 1) {
            return new com.android.server.pm.PackageManagerShellCommandDataLoader.DataLoader();
        }
        return null;
    }
}
