package cn.yugj.springcloud.sleuthsrb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yugj
 * @date 2019/1/23 上午9:55.
 */
@RestController
public class SleuthSrController {

    private static final Logger log = LoggerFactory.getLogger(SleuthSrController.class);

    @Autowired
    private AmqpTemplate amqpTemplate;


    @GetMapping("/sr/test")
    public Object test() {

        log.info("send msg");
        amqpTemplate.convertAndSend("q.hell", "hell");

        return "ehll";
    }
}
