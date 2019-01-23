package cn.yugj.springclound.sleuthsrb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Created by yugj on 18/7/10 14:23.
 */
@Component
public class HellReceiver {

    private static final Logger log = LoggerFactory.getLogger(HellReceiver.class);

    @RabbitHandler
    @RabbitListener(queues = "q.hell.srb")
    public void process(String msg) {
        log.info("receive msg :{}", msg);
    }
}
