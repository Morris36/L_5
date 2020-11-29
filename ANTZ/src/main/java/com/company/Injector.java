package com.company;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * @author ED
 */
public class Injector {
    private final Properties properties = new Properties();

    /**
     * @param object - Класс
     * @return ссылка на ин. класс
     * @throws ClassNotFoundException - стандартное исключение
     * @throws IllegalAccessException - стандартное исключение
     * @throws InstantiationException - стандартное исключение
     */
    public Object inject(Object object) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
        FileInputStream fileInputStream = new FileInputStream("myProperties");
        properties.load(fileInputStream);
        Class cl = Class.forName(object.getClass().getCanonicalName());
        Field[] fields = cl.getDeclaredFields();
        for (Field fl : fields
        ) {
            if (fl.isAnnotationPresent(AutoInjectable.class)) {
                Class vl = Class.forName(properties.getProperty(String.valueOf(fl.getType())));
                fl.setAccessible(true);
                fl.set(object, vl.newInstance());
            }
        }
        return object;
    }


    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        Injector injector = new Injector();
        SomeBean sb = (SomeBean) injector.inject(new SomeBean());
        sb.foo();
    }

    /**
     * @param one Первый аргумент
     * @param two Второй аргумент
     */
    private void SPr(String one, String two) {
        properties.setProperty(one, two);
    }


}
