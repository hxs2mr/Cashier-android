# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#保留系统中继承v4/v7包的类，不被混淆
-keep class android.support.** { *; }
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.** { *; }
-keep public class * extends android.support.v7.**
-keep interface android.support.v7.app.** { *; }

-dontwarn android.support.v4.**
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v4.** { *; }
-keep public class * extends android.app.Application
-dontwarn android.support.v7.**
-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }
-keep class android.support.v7.** { *; }


#四大组件不能混淆
-dontwarn android.support.**
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference



#保留系统中继实现v4/v7包的接口，不被混淆
-keep public class * implements android.support.v4.**
-dontwarn android.support.v4.**

#所有的native方法不被混淆
-keepclasseswithmembers class * {
    native <methods>;
}

#自定义View构造方法不混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}


#枚举不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#Design包不混淆
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }


#确保JavaBean不被混淆-否则Gson将无法将数据解析成具体对象
-keep class com.wrs.gykjewm.baselibrary.domain.** { *; }
-keep class com.gykj.cashier.module.cashier.entity.** { *; }
-keep class com.gykj.cashier.module.edit.entity.** { *; }
-keep class com.gykj.cashier.module.finance.entity.** { *; }
-keep class com.gykj.cashier.module.index.entity.** { *; }
-keep class com.gykj.cashier.module.inventory.entity.** { *; }
-keep class com.gykj.cashier.module.login.entity.** { *; }
-keep class com.gykj.cashier.module.order.entity.** { *; }
-keep class com.gykj.cashier.module.setting.entity.** { *; }
-keep class com.gykj.cashier.module.storage.entity.** { *; }



#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

#acface
-dontwarn com.arcsoft.**
-keep class com.arcsoft.** { *;}


-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}

-keepattributes Signature, InnerClasses, EnclosingMethod
# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**


# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*
# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform

#baidu tts
-keep class com.baidu.tts.**{*;}
-keep class com.baidu.speechsynthesizer.**{*;}


-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *;}

-dontwarn com.rabbitmq.**
-keep class com.rabbitmq.** { *;}

-dontwarn org.slf4j.**
-keep class org.slf4j.** { *;}

-dontwarn com.zkteco.**
-keep class com.zkteco.** { *;}
-keep interface com.zkteco.** { *;}

-keep class com.wrs.gykjewm.baselibrary.realm.** { *;}


-dontwarn jx.vein.javajar.**
-keep class jx.vein.javajar.** { *;}
