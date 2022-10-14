package hs.saga.config.camel;

public class CamelThreadLocal {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<String>();

    public static String getLra() {
        return threadLocal.get();
    }

    public static void setLra(String lra) {
        threadLocal.set(lra);
    }

    public static void remove() {
        threadLocal.remove();
    }
}
