package ulb.infof307.g10;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Test;

public class MainTest {
    private static final Logger logger = LoggerFactory.getLogger(MainTest.class);

    @Test
    public void test(){
        logger.debug("test()");
        logger.debug("test() success");
    }
}
