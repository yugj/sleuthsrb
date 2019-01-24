package cn.yugj.springcloud.sleuthsrb.support;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yugj
 * @date 2019/1/23 上午10:03.
 */
@Configuration
public class RabbitMq4TestConfiguration {

    @Bean
    public Queue queue() {
        return new Queue("q.hell.srb");
    }
}
