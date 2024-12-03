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

# 카카오 SDK 모델 클래스의 필드 보존
-keep class com.kakao.sdk.**.model.* { *; }

# Gson TypeAdapter를 상속하는 모든 클래스 보존
-keep class * extends com.google.gson.TypeAdapter

# LogInKakaoDto의 idToken 필드를 보존
-keep class com.example.myapplication.data.**{*;}

# 경고 무시 규칙
-dontwarn org.bouncycastle.jsse.**
-dontwarn org.conscrypt.*
-dontwarn org.openjsse.**



