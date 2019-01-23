package cn.yugj.springclound.sleuthsrb.support;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yugj
 * @date 2019/1/23 上午10:03.
 */
@Configuration
public class RabbitMqConfiguration {

    @Bean
    public Queue queue() {
        return new Queue("q.hell.srb");
    }
}
