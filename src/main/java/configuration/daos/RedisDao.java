package configuration.daos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import configuration.response.ResponseItem;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;

public class RedisDao<T extends ResponseItem> {

    Logger log = LoggerFactory.getLogger(RedisDao.class);

    @Inject
    private StatefulRedisConnection<String, String> connection;

    private RedisCommands<String, String> commands;

    @Inject
    private ObjectMapper objectMapper;

    @PostConstruct
    protected void initialise() {
        commands = connection.sync();
    }

    public String get(String key) {
        return commands.get(key);
    }

    // TODO replace this crap with a proper codec config

    public T get(String key, Class<T> clazz) {

        String value = get(key);

        try {
            return objectMapper.readValue(value, clazz);
        } catch (IOException e) {
            log.info(String.format("could not convert %s to class:%s", value, clazz.getName()));
            e.printStackTrace();
            return null;
        }
    }

    public boolean put(String key, ResponseItem toSave) {

        String toSaveStr;

        try {
            toSaveStr = objectMapper.writeValueAsString(toSave);
        } catch (JsonProcessingException e) {
            log.info("could not write value as string: " + toSave);
            e.printStackTrace();
            return false;
        }

        return commands.set(key, toSaveStr).equals("OK");

    }
}
