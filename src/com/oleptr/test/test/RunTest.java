package com.oleptr.test.test;

import com.oleptr.test.core.RandomInt;
import com.oleptr.test.core.TestExample;
import javafx.application.Application;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class RunTest {

    private static int setRandomInt(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
       // System.out.println(File.separator);
       // System.out.println(File.separatorChar);

        //String path = packageName.replace('.', File.separatorChar);
        String path = packageName.replace('.', '/');

        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {

            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {

        String pkn = "";
        List<Class> classes = new ArrayList<Class>();
        boolean ife = directory.exists();
        //System.out.println(ife);
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String fn = file.getName().substring(0, file.getName().length() - 6);
                String clname = packageName + '.' + fn;
                //String clname = pkn + '.' + fn;
                // clname = clname.substring(1);
                classes.add(Class.forName(clname));

            } else if (file.getName().endsWith(".java") & !file.getName().startsWith("Test")) {
                String fn = file.getName().substring(0, file.getName().length() - 5);
                String clname = packageName + '.' + fn;
                //clname = clname.substring(1);
                classes.add(Class.forName(clname));
            }

        }
        return classes;
    }


    private static List<Class> findClasses_mod(File directory, String packageName, String pkn) throws ClassNotFoundException {

        List<Class> classes = new ArrayList<Class>();
        String firstdir = (directory.getName());
        firstdir.replace(".", "");
        directory = new File(directory.getPath());
        File[] files = directory.listFiles();
        //String teststr = "";
        for (File file : files) {
            if (directory.getPath().endsWith(firstdir)) {
                if (pkn.length() > 1) {
                    if (pkn.indexOf(".") > 0) {
                        firstdir = pkn.substring(0, pkn.indexOf("."));
                    } else {
                        firstdir = pkn;
                    }
                    pkn = pkn.substring(pkn.indexOf(".") + 1);
                    if (file.isDirectory()) {
                        assert !file.getName().contains(".");
                        classes.addAll(findClasses_mod(file, packageName + "." + file.getName(), pkn));
                    } else if (file.getName().endsWith(".class")) {
                        String fn = file.getName().substring(0, file.getName().length() - 6);
                        String clname = packageName + '.' + fn;
                        try {
                            clname = clname.substring(1);
                            classes.add(Class.forName(clname));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            System.out.println("Probably Annotation class error");
                        }
                    }
                }
            } else {
                System.out.println("Class is not in given packet");
                continue;
            }
        }
        return classes;
    }


    public static void main(String[] args) throws Exception {
        Class[] cs = null;
        cs = getClasses("com.oleptr.test.core");
        System.out.println("Total classes founded: " + cs.length);
        cs = finalfun("com.oleptr.test.core");
        System.out.println("Total classes founded: " + cs.length);

        ArrayList<Object> objs = new ArrayList<Object>();
        Class obj = null;
        for (Class cl : cs) {
            if (!cl.isInterface()) {
                obj = Class.forName(cl.getName());
                System.out.println("Processing class: " + cl.getName());
                try {
                    Constructor<?> cons = obj.getDeclaredConstructor();
                    cons.setAccessible(true);
                    // Constructor is exist
                    Object object = cons.newInstance();
                    objs.add(object);
                    RandomIntAnnotationProcessor randomIntProcessor = new RandomIntAnnotationProcessor(obj, object).invoke();
                } catch (NoSuchMethodException e) {
                    // Constructor doesn't exist
                    System.out.println("!!Class " + cl.getName() + " does not have constructor ");
                }
            }
        }


        System.out.println("Array of created Objects: " + objs);

        System.out.println("/////////////////////////////////////////");
        System.out.println("Testing...");
        int passed = 0, failed = 0, count = 0, ignore = 0;
        obj = Class.forName("com.oleptr.test.core.Phone");
        System.out.println(obj.getName());
    }

    private static class RandomIntAnnotationProcessor {
        private Class obj;
        private int passed;
        private int failed;
        private int count;
        private Object object;

        public RandomIntAnnotationProcessor(Class obj, Object object) {
            this.obj = obj;
            this.passed = 0;
            this.failed = 0;
            this.count = 0;
            this.object = object;
        }

        public int getPassed() {
            return passed;
        }

        public int getFailed() {
            return failed;
        }

        public int getCount() {
            return count;
        }

        public RandomIntAnnotationProcessor invoke() throws IllegalAccessException {
            for (Field field : obj.getDeclaredFields()) {
                if (field.isAnnotationPresent(RandomInt.class)) {
                    Annotation annotation = field.getAnnotation(RandomInt.class);
                    RandomInt ri = (RandomInt) annotation;
                    if (field.isAnnotationPresent(RandomInt.class)) {
                        field.setAccessible(true);

                        System.out.println("  Field annotation founded -> " + field.getName() + " = " + field.getInt(object));
                        field.setInt(object, setRandomInt(ri.min(), ri.max()));
                        System.out.println("  Field annotation processed -> " + field.getName() + " = " + field.getInt(object) + "");
                    }
                    // if enabled = true (default)
//                    try {
//                        System.out.printf("%s - com.oleptr.test '%s' - passed %n", ++count, field.getName());
//                        passed++;
//                    } catch (Throwable ex) {
//                        System.out.printf("%s - com.oleptr.test '%s' - failed: %s %n", ++count, field.getName(), ex.getCause());
//                        failed++;
//                    }
                }

            }
            return this;
        }
    }

    private static Class[] finalfun(String pkn) throws ClassNotFoundException {
        String path = System.getProperty("user.dir");
        //File file = new File(path.concat("/out/production/annotation"));
        String appName = path.toString().substring(path.toString().lastIndexOf("\\") + 1, path.toString().length());
        File file = new File(path.concat("/out/production/" + appName + "/"));
        //File file = new File(path.concat("/out/production/annotation"));
        List<Class> lc;
        lc = findClasses_mod(file, "", pkn);
        return (Class[]) lc.toArray(new Class[lc.size()]);
        //return null;
    }
}
