package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class PackageProperty {
    private android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.content.pm.PackageManager.Property>>> mActivityProperties;
    private android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.content.pm.PackageManager.Property>>> mApplicationProperties;
    private android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.content.pm.PackageManager.Property>>> mProviderProperties;
    private android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.content.pm.PackageManager.Property>>> mReceiverProperties;
    private android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.content.pm.PackageManager.Property>>> mServiceProperties;

    public android.content.pm.PackageManager.Property getProperty(java.lang.String propertyName, java.lang.String packageName, java.lang.String className) {
        if (className == null) {
            return getApplicationProperty(propertyName, packageName);
        }
        return getComponentProperty(propertyName, packageName, className);
    }

    public java.util.List<android.content.pm.PackageManager.Property> queryProperty(java.lang.String propertyName, int componentType, java.util.function.Predicate<java.lang.String> filter) {
        android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.content.pm.PackageManager.Property>>> propertyMap;
        android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.content.pm.PackageManager.Property>> packagePropertyMap;
        if (componentType == 5) {
            propertyMap = this.mApplicationProperties;
        } else if (componentType == 1) {
            propertyMap = this.mActivityProperties;
        } else if (componentType == 4) {
            propertyMap = this.mProviderProperties;
        } else if (componentType == 2) {
            propertyMap = this.mReceiverProperties;
        } else if (componentType == 3) {
            propertyMap = this.mServiceProperties;
        } else {
            propertyMap = null;
        }
        if (propertyMap == null || (packagePropertyMap = propertyMap.get(propertyName)) == null) {
            return null;
        }
        android.os.Binder.getCallingUid();
        android.os.UserHandle.getCallingUserId();
        int mapSize = packagePropertyMap.size();
        java.util.List<android.content.pm.PackageManager.Property> result = new java.util.ArrayList<>(mapSize);
        for (int i = 0; i < mapSize; i++) {
            java.lang.String packageName = packagePropertyMap.keyAt(i);
            if (!filter.test(packageName)) {
                result.addAll(packagePropertyMap.valueAt(i));
            }
        }
        return result;
    }

    void addAllProperties(com.android.server.pm.pkg.AndroidPackage pkg) {
        this.mApplicationProperties = addProperties(pkg.getProperties(), this.mApplicationProperties);
        this.mActivityProperties = addComponentProperties(pkg.getActivities(), this.mActivityProperties);
        this.mProviderProperties = addComponentProperties(pkg.getProviders(), this.mProviderProperties);
        this.mReceiverProperties = addComponentProperties(pkg.getReceivers(), this.mReceiverProperties);
        this.mServiceProperties = addComponentProperties(pkg.getServices(), this.mServiceProperties);
    }

    void removeAllProperties(com.android.server.pm.pkg.AndroidPackage pkg) {
        this.mApplicationProperties = removeProperties(pkg.getProperties(), this.mApplicationProperties);
        this.mActivityProperties = removeComponentProperties(pkg.getActivities(), this.mActivityProperties);
        this.mProviderProperties = removeComponentProperties(pkg.getProviders(), this.mProviderProperties);
        this.mReceiverProperties = removeComponentProperties(pkg.getReceivers(), this.mReceiverProperties);
        this.mServiceProperties = removeComponentProperties(pkg.getServices(), this.mServiceProperties);
    }

    private static <T extends com.android.internal.pm.pkg.component.ParsedComponent> android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.content.pm.PackageManager.Property>>> addComponentProperties(java.util.List<T> components, android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.content.pm.PackageManager.Property>>> propertyCollection) {
        android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.content.pm.PackageManager.Property>>> returnCollection = propertyCollection;
        int componentsSize = components.size();
        for (int i = 0; i < componentsSize; i++) {
            java.util.Map<java.lang.String, android.content.pm.PackageManager.Property> properties = components.get(i).getProperties();
            if (properties.size() != 0) {
                returnCollection = addProperties(properties, returnCollection);
            }
        }
        return returnCollection;
    }

    private static android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.content.pm.PackageManager.Property>>> addProperties(java.util.Map<java.lang.String, android.content.pm.PackageManager.Property> properties, android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.content.pm.PackageManager.Property>>> propertyCollection) {
        if (properties.size() == 0) {
            return propertyCollection;
        }
        android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.content.pm.PackageManager.Property>>> returnCollection = propertyCollection == null ? new android.util.ArrayMap<>(10) : propertyCollection;
        for (android.content.pm.PackageManager.Property property : properties.values()) {
            java.lang.String propertyName = property.getName();
            java.lang.String packageName = property.getPackageName();
            android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.content.pm.PackageManager.Property>> propertyMap = returnCollection.get(propertyName);
            if (propertyMap == null) {
                propertyMap = new android.util.ArrayMap<>();
                returnCollection.put(propertyName, propertyMap);
            }
            java.util.ArrayList<android.content.pm.PackageManager.Property> packageProperties = propertyMap.get(packageName);
            if (packageProperties == null) {
                packageProperties = new java.util.ArrayList<>(properties.size());
                propertyMap.put(packageName, packageProperties);
            }
            packageProperties.add(property);
        }
        return returnCollection;
    }

    private static <T extends com.android.internal.pm.pkg.component.ParsedComponent> android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.content.pm.PackageManager.Property>>> removeComponentProperties(java.util.List<T> components, android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.content.pm.PackageManager.Property>>> propertyCollection) {
        android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.content.pm.PackageManager.Property>>> returnCollection = propertyCollection;
        int componentsSize = components.size();
        for (int i = 0; returnCollection != null && i < componentsSize; i++) {
            java.util.Map<java.lang.String, android.content.pm.PackageManager.Property> properties = components.get(i).getProperties();
            if (properties.size() != 0) {
                returnCollection = removeProperties(properties, returnCollection);
            }
        }
        return returnCollection;
    }

    private static android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.content.pm.PackageManager.Property>>> removeProperties(java.util.Map<java.lang.String, android.content.pm.PackageManager.Property> properties, android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.content.pm.PackageManager.Property>>> propertyCollection) {
        java.util.ArrayList<android.content.pm.PackageManager.Property> packageProperties;
        if (propertyCollection == null) {
            return null;
        }
        for (android.content.pm.PackageManager.Property property : properties.values()) {
            java.lang.String propertyName = property.getName();
            java.lang.String packageName = property.getPackageName();
            android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.content.pm.PackageManager.Property>> propertyMap = propertyCollection.get(propertyName);
            if (propertyMap != null && (packageProperties = propertyMap.get(packageName)) != null) {
                packageProperties.remove(property);
                if (packageProperties.size() == 0) {
                    propertyMap.remove(packageName);
                }
                if (propertyMap.size() == 0) {
                    propertyCollection.remove(propertyName);
                }
            }
        }
        if (propertyCollection.size() == 0) {
            return null;
        }
        return propertyCollection;
    }

    private static android.content.pm.PackageManager.Property getProperty(java.lang.String propertyName, java.lang.String packageName, java.lang.String className, android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.content.pm.PackageManager.Property>>> propertyMap) {
        java.util.List<android.content.pm.PackageManager.Property> propertyList;
        android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.content.pm.PackageManager.Property>> packagePropertyMap = propertyMap.get(propertyName);
        if (packagePropertyMap == null || (propertyList = packagePropertyMap.get(packageName)) == null) {
            return null;
        }
        for (int i = propertyList.size() - 1; i >= 0; i--) {
            android.content.pm.PackageManager.Property property = propertyList.get(i);
            if (java.util.Objects.equals(className, property.getClassName())) {
                return property;
            }
        }
        return null;
    }

    private android.content.pm.PackageManager.Property getComponentProperty(java.lang.String propertyName, java.lang.String packageName, java.lang.String className) {
        android.content.pm.PackageManager.Property property = null;
        if (0 == 0 && this.mActivityProperties != null) {
            property = getProperty(propertyName, packageName, className, this.mActivityProperties);
        }
        if (property == null && this.mProviderProperties != null) {
            property = getProperty(propertyName, packageName, className, this.mProviderProperties);
        }
        if (property == null && this.mReceiverProperties != null) {
            property = getProperty(propertyName, packageName, className, this.mReceiverProperties);
        }
        if (property == null && this.mServiceProperties != null) {
            return getProperty(propertyName, packageName, className, this.mServiceProperties);
        }
        return property;
    }

    private android.content.pm.PackageManager.Property getApplicationProperty(java.lang.String propertyName, java.lang.String packageName) {
        java.util.List<android.content.pm.PackageManager.Property> propertyList;
        android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.content.pm.PackageManager.Property>> packagePropertyMap = this.mApplicationProperties != null ? this.mApplicationProperties.get(propertyName) : null;
        if (packagePropertyMap == null || (propertyList = packagePropertyMap.get(packageName)) == null) {
            return null;
        }
        return propertyList.get(0);
    }
}
