-injars build/libs/SimLocalAnesthetics.jar
-outjars build/libs/SimLocalAnesthetics-minified.jar
-libraryjars <java.home>/jmods

-dontwarn **
-dontobfuscate
-keepattributes SourceFile,LineNumberTable,Signature,Exceptions,*Annotation*

-keep class io.github.toshiara.simla.** { *; }

-keep class javax.swing.** { *; }
-keep class java.awt.** { *; }
-keep class sun.awt.** { *; }

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
