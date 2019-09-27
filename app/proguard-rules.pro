# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\androidstudioSdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
# java提供给web的接口
-keepattributes *JavascriptInterface*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Dialog
-keep public class * extends android.app.Service
-keep public class * extends android.app.View
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-dontwarn  com.cylty.zmkj.**
-keep class com.cylty.zmkj.**{*;}

-dontwarn  com.nirvana.zmkj.**
-keep class com.nirvana.zmkj.**{*;}

-dontwarn  com.nirvana.share.**
-keep class com.nirvana.share.**{*;}

-dontwarn  com.antelope.lib_banner.**
-keep class com.antelope.lib_banner.**{*;}


-keep class com.antelope.goodbrother.business.model.**{*;}

-keep class com.antelope.goodbrother.receiver.**{*;}
-keep class com.antelope.goodbrother.widget.**{*;}
-keep class com.antelope.goodbrother.wxapi.**{*;}
-keep class com.antelope.goodbrother.http.**{*;}
-keep class com.antelope.goodbrother.http.**{*;}


-keep public class * implements java.io.Serializable {
        public *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keep public class com.antelope.goodbrother.R$*{
public static final int *;
}

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.# 泛型
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.# 异常
-keepattributes Exceptions
# 反射
-keepattributes EnclosingMethod
# 注解
-keepattributes *Annotation*


#第三方jar
-dontwarn com.android.support.**
-keep class com.support.** { *; }
-dontwarn com.android.widget.**
-keep class com.widget.** { *; }
-dontwarn cn.**
-keep class cn.** { *; }
-dontwarn com.alibaba.**
-keep class com.alibaba.** { *; }
-dontwarn com.bumptech.glide.**
-keep class com.bumptech.glide.** { *; }
-dontwarn com.githup.**
-keep class com.githup.** { *; }
-dontwarn com.google.**
-keep class com.google.** { *; }
-dontwarn com.squareup.**
-keep class com.squareup.** { *; }
-dontwarn com.tencent.**
-keep class com.tencent.** { *; }
-dontwarn com.umeng.**
-keep class com.umeng.** { *; }
-dontwarn in.srain.**
-keep class in.srain.** { *; }
-dontwarn io.**
-keep class io.** { *; }
# OkHttp3
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-dontwarn okio.**
-keep class okio.** { *; }
-dontwarn org.**
-keep class org.** { *; }
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-dontwarn rx.**
-keep class rx.** { *; }
-dontwarn sun.**
-keep class sun.**
-dontwarn springfox.**
-keep class springfox.**
-dontwarn jp.**
-keep class jp.**
-keep class com.sina.weibo.sdk.** { *; }

-keep class com.a.a**
-dontwarn  com.a.a.**
-keep class com.szzk.**
-dontwarn  com.szzk.**
#gson
-dontwarn com.google.**
-keep class com.google.gson.** {*;}

# EventBus
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#基线包使用，生成mapping.txt
-printmapping mapping.txt
#生成的mapping.txt在app/build/outputs/mapping/release路径下，移动到/app路径下
#修复后的项目使用，保证混淆结果一致
#-applymapping mapping.txt
#hotfix
-keep class com.taobao.sophix.**{*;}
-keep class com.ta.utdid2.device.**{*;}
-dontwarn com.alibaba.sdk.android.utils.**
#防止inline
-dontoptimize
-keepclassmembers class com.nirvana.ylmc.application.MyApplication {
    public <init>();
}
