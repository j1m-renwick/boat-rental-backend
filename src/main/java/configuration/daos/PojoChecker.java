package configuration.daos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PojoChecker {

    private Logger logger = LoggerFactory.getLogger(PojoChecker.class);

    Class<?> clazz;

    public static PojoChecker of(Class<?> clazz) {
        return new PojoChecker(clazz);
    }

    private PojoChecker(Class<?> clazz) {
        this.clazz = clazz;
    }

    // TODO refactor to store all names as a map on initialisation and also wrap the find method to perform checks before pass-through...or better yet find a way to do similar checking natively
    public void check(String fieldName) {
        try {
            clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            logger.info(fieldName + " was not found in class: " + clazz.getName());
            throw new RuntimeException();
        }
    }
}
