package it.ale.common;

import it.ale.exception.GenericException;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class CommonUtils {
    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER_TOKEN_PREFIX = "Bearer ";
    public static final String HUG_SESSION = "hug-session-id";
    public static final String CERTIFICATE = "certificate";

    private CommonUtils() {}

    @SuppressWarnings("unchecked")
    public static <T> T cloneEntityWithoutProperty(T entityToClone, Class<T> clazz,
                                                   String... ignoreProperties) throws GenericException {
        try {
            T entityCloned = (T) Class.forName(clazz.getName()).getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(entityToClone, entityCloned, ignoreProperties);
            return entityCloned;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                 | SecurityException | InvocationTargetException | NoSuchMethodException
                 | ClassNotFoundException e) {
            throw new GenericException(
                    String.format("Class %s is not instantiable.", clazz.getSimpleName()), e);
        }
    }
    public static String getTempDir() {
        String tempDir = System.getProperty("java.io.tmpdir");
        if (!(tempDir.endsWith("/") || tempDir.endsWith("\\"))) {
            tempDir = tempDir + FileSystems.getDefault().getSeparator();
        }
        return tempDir;
    }

    public static Instant getLocalInstant() {
        ZoneId zoneId = ZoneId.of("Europe/Zurich");
        ZonedDateTime nowLocal = ZonedDateTime.now(zoneId);
        return nowLocal.toInstant();
    }

    public static Date getLocalDate() {
        return new Date(getLocalInstant().toEpochMilli());
    }
}
