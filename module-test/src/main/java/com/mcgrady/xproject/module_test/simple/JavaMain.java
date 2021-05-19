package com.mcgrady.xproject.module_test.simple;

/**
 * Created by mcgrady on 5/1/21.
 */
public class JavaMain {

    public static void main(String[] args) {

        JavaMain javaMain = new JavaMain();
        javaMain.testStringUtils();
    }

    private void testStringUtils() {

        println(StringUtilsFormJvmName.isEmpty(""));
        println(StringUtilsFormINSTANCE.INSTANCE.isEmpty("xx"));
        println(StringUtilsFormJvmStatic.isEmpty(""));
        println(StringUtilsFormCompObj.Companion.isEmpty("xx"));
    }

    private void println(boolean result) {
        System.out.println(result);
    }
}
