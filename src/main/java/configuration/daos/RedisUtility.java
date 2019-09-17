package configuration.daos;

import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.sync.RedisCommands;

public class RedisUtility {

    private static final int RETRY_ATTEMPTS = 5;

    public static TransactionResult execWithRetries(RedisCommands<String, String> commander, Runnable runnable) {

        for (int i = 0; i <= RETRY_ATTEMPTS; i++) {
            runnable.run();
            TransactionResult result = commander.exec();
            if (!result.isEmpty()) {
                return result;
            }
        }

        return null;

    }




}
