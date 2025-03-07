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
#-keep class androidx.appcompat.widget.** { *; }

#r8 ignore
-dontpreverify
-optimizationpasses 3
-mergeinterfacesaggressively

-repackageclasses
-allowaccessmodification
#-flattenpackagehierarchy
-dontskipnonpubliclibraryclasses

#r8 ignore
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*,method/inlining/short,class/merging/horizontal,class/merging/vertical

#-obfuscationdictionary
#-classobfuscationdictionary
#-packageobfuscationdictionary

-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*
-keep public class com.joaomgcd.taskerpluginlibrary.output.TaskerOutputObject { *; }
-keep public class com.joaomgcd.taskerpluginlibrary.output.TaskerOutputVariable { *; }

-keepclasseswithmembers class * {
    @com.joaomgcd.taskerpluginlibrary.input.TaskerInputField <fields>;
}
-keep @com.joaomgcd.taskerpluginlibrary.input.TaskerInputRoot public class *
-keep @com.joaomgcd.taskerpluginlibrary.input.TaskerInputObject public class *
-keep @com.joaomgcd.taskerpluginlibrary.output.TaskerOutputObject public class *
-keepclassmembers class * {
    @com.joaomgcd.taskerpluginlibrary.output.TaskerOutputObject *;
}
-keepclassmembers class * {
    @com.joaomgcd.taskerpluginlibrary.output.TaskerOutputVariable *;
}
-keepclassmembers @com.joaomgcd.taskerpluginlibrary.output.TaskerOutputObject class * { *; }
-keep public class * extends com.joaomgcd.taskerpluginlibrary.runner.TaskerPluginRunner { *; }


-keep public class net.dinglisch.android.tasker.PluginResultReceiver { *; }

-dontwarn android.**
-dontwarn com.google.**

-keep public class kotlin.Unit { *; }
