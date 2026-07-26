package android.net.shared;

/* JADX INFO: loaded from: classes.dex */
public class InitialConfiguration {
    public static final java.net.InetAddress INET6_ANY = android.net.InetAddresses.parseNumericAddress("::");
    private static final int RFC6177_MIN_PREFIX_LENGTH = 48;
    private static final int RFC7421_PREFIX_LENGTH = 64;
    public final java.util.Set<android.net.LinkAddress> ipAddresses = new java.util.HashSet();
    public final java.util.Set<android.net.IpPrefix> directlyConnectedRoutes = new java.util.HashSet();
    public final java.util.Set<java.net.InetAddress> dnsServers = new java.util.HashSet();

    public static android.net.shared.InitialConfiguration copy(android.net.shared.InitialConfiguration config) {
        if (config == null) {
            return null;
        }
        android.net.shared.InitialConfiguration configCopy = new android.net.shared.InitialConfiguration();
        configCopy.ipAddresses.addAll(config.ipAddresses);
        configCopy.directlyConnectedRoutes.addAll(config.directlyConnectedRoutes);
        configCopy.dnsServers.addAll(config.dnsServers);
        return configCopy;
    }

    public java.lang.String toString() {
        return java.lang.String.format("InitialConfiguration(IPs: {%s}, prefixes: {%s}, DNS: {%s})", android.text.TextUtils.join(", ", this.ipAddresses), android.text.TextUtils.join(", ", this.directlyConnectedRoutes), android.text.TextUtils.join(", ", this.dnsServers));
    }

    public boolean isValid() {
        if (this.ipAddresses.isEmpty()) {
            return false;
        }
        for (final android.net.LinkAddress addr : this.ipAddresses) {
            if (!any(this.directlyConnectedRoutes, new java.util.function.Predicate() { // from class: android.net.shared.InitialConfiguration$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return ((android.net.IpPrefix) obj).contains(addr.getAddress());
                }
            })) {
                return false;
            }
        }
        for (final java.net.InetAddress addr2 : this.dnsServers) {
            if (!any(this.directlyConnectedRoutes, new java.util.function.Predicate() { // from class: android.net.shared.InitialConfiguration$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return ((android.net.IpPrefix) obj).contains(addr2);
                }
            })) {
                return false;
            }
        }
        if (any(this.ipAddresses, not(new java.util.function.Predicate() { // from class: android.net.shared.InitialConfiguration$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return android.net.shared.InitialConfiguration.isPrefixLengthCompliant((android.net.LinkAddress) obj);
            }
        }))) {
            return false;
        }
        return ((any(this.directlyConnectedRoutes, new java.util.function.Predicate() { // from class: android.net.shared.InitialConfiguration$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return android.net.shared.InitialConfiguration.isIPv6DefaultRoute((android.net.IpPrefix) obj);
            }
        }) && all(this.ipAddresses, not(new java.util.function.Predicate() { // from class: android.net.shared.InitialConfiguration$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return android.net.shared.InitialConfiguration.isIPv6GUA((android.net.LinkAddress) obj);
            }
        }))) || any(this.directlyConnectedRoutes, not(new java.util.function.Predicate() { // from class: android.net.shared.InitialConfiguration$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return android.net.shared.InitialConfiguration.isPrefixLengthCompliant((android.net.IpPrefix) obj);
            }
        })) || this.ipAddresses.stream().filter(new java.util.function.Predicate() { // from class: android.net.shared.InitialConfiguration$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return android.net.shared.InitialConfiguration.isIPv4((android.net.LinkAddress) obj);
            }
        }).count() > 1) ? false : true;
    }

    public boolean isProvisionedBy(java.util.List<android.net.LinkAddress> addresses, java.util.List<android.net.RouteInfo> routes) {
        if (this.ipAddresses.isEmpty()) {
            return false;
        }
        for (final android.net.LinkAddress addr : this.ipAddresses) {
            if (!any(addresses, new java.util.function.Predicate() { // from class: android.net.shared.InitialConfiguration$$ExternalSyntheticLambda9
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return addr.isSameAddressAs((android.net.LinkAddress) obj);
                }
            })) {
                return false;
            }
        }
        if (routes != null) {
            for (final android.net.IpPrefix prefix : this.directlyConnectedRoutes) {
                if (!any(routes, new java.util.function.Predicate() { // from class: android.net.shared.InitialConfiguration$$ExternalSyntheticLambda10
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return android.net.shared.InitialConfiguration.isDirectlyConnectedRoute((android.net.RouteInfo) obj, prefix);
                    }
                })) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    public android.net.InitialConfigurationParcelable toStableParcelable() {
        android.net.InitialConfigurationParcelable p = new android.net.InitialConfigurationParcelable();
        p.ipAddresses = (android.net.LinkAddress[]) this.ipAddresses.toArray(new android.net.LinkAddress[0]);
        p.directlyConnectedRoutes = (android.net.IpPrefix[]) this.directlyConnectedRoutes.toArray(new android.net.IpPrefix[0]);
        p.dnsServers = (java.lang.String[]) android.net.shared.ParcelableUtil.toParcelableArray(this.dnsServers, new android.net.shared.InitialConfiguration$$ExternalSyntheticLambda11(), java.lang.String.class);
        return p;
    }

    public static android.net.shared.InitialConfiguration fromStableParcelable(android.net.InitialConfigurationParcelable p) {
        if (p == null) {
            return null;
        }
        android.net.shared.InitialConfiguration config = new android.net.shared.InitialConfiguration();
        config.ipAddresses.addAll(java.util.Arrays.asList(p.ipAddresses));
        config.directlyConnectedRoutes.addAll(java.util.Arrays.asList(p.directlyConnectedRoutes));
        config.dnsServers.addAll(android.net.shared.ParcelableUtil.fromParcelableArray(p.dnsServers, new android.net.shared.InitialConfiguration$$ExternalSyntheticLambda8()));
        return config;
    }

    public boolean equals(java.lang.Object obj) {
        if (!(obj instanceof android.net.shared.InitialConfiguration)) {
            return false;
        }
        android.net.shared.InitialConfiguration other = (android.net.shared.InitialConfiguration) obj;
        return this.ipAddresses.equals(other.ipAddresses) && this.directlyConnectedRoutes.equals(other.directlyConnectedRoutes) && this.dnsServers.equals(other.dnsServers);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isDirectlyConnectedRoute(android.net.RouteInfo route, android.net.IpPrefix prefix) {
        return !route.hasGateway() && route.getType() == 1 && prefix.equals(route.getDestination());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isPrefixLengthCompliant(android.net.LinkAddress addr) {
        return isIPv4(addr) || isCompliantIPv6PrefixLength(addr.getPrefixLength());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isPrefixLengthCompliant(android.net.IpPrefix prefix) {
        return isIPv4(prefix) || isCompliantIPv6PrefixLength(prefix.getPrefixLength());
    }

    private static boolean isCompliantIPv6PrefixLength(int prefixLength) {
        return 48 <= prefixLength && prefixLength <= 64;
    }

    private static boolean isIPv4(android.net.IpPrefix prefix) {
        return prefix.getAddress() instanceof java.net.Inet4Address;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isIPv4(android.net.LinkAddress addr) {
        return addr.getAddress() instanceof java.net.Inet4Address;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isIPv6DefaultRoute(android.net.IpPrefix prefix) {
        return prefix.getAddress().equals(INET6_ANY);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isIPv6GUA(android.net.LinkAddress addr) {
        return addr.isIpv6() && addr.isGlobalPreferred();
    }

    public static <T> boolean any(java.lang.Iterable<T> coll, java.util.function.Predicate<T> fn) {
        for (T t : coll) {
            if (fn.test(t)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean all(java.lang.Iterable<T> coll, java.util.function.Predicate<T> fn) {
        return !any(coll, not(fn));
    }

    static /* synthetic */ boolean lambda$not$4(java.util.function.Predicate fn, java.lang.Object t) {
        return !fn.test(t);
    }

    public static <T> java.util.function.Predicate<T> not(final java.util.function.Predicate<T> fn) {
        return new java.util.function.Predicate() { // from class: android.net.shared.InitialConfiguration$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return android.net.shared.InitialConfiguration.lambda$not$4(fn, obj);
            }
        };
    }
}
