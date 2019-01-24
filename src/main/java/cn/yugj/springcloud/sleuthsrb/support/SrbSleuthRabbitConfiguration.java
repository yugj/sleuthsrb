package cn.yugj.springcloud.sleuthsrb.support;

import brave.Tracing;
import brave.spring.rabbit.SpringRabbitTracing;
import cn.yugj.springcloud.sleuthsrb.support.log.SrbSlf4jCurrentTraceContext;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.sleuth.autoconfig.TraceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yugj
 * @date 2019/1/23 下午2:02.
 */
@Configuration
@ConditionalOnProperty(value = "spring.sleuth.messaging.rabbit.enabled", matchIfMissing = true)
@ConditionalOnClass(RabbitTemplate.class)
@EnableConfigurationProperties(SrbSleuthMessagingProperties.class)
@AutoConfigureAfter({ TraceAutoConfiguration.class })
public class SrbSleuthRabbitConfiguration {

    /**
     * 重写默认tracing 使用 sleuth2.0.2 Slf4jCurrentTraceContext 默认的不处理sl4j mdc 设置traceid spanid
     * @return Tracing
     */
    @Bean
    Tracing tracing() {
        return Tracing.newBuilder().currentTraceContext(SrbSlf4jCurrentTraceContext.create()).build();
    }

    @Bean
    @ConditionalOnMissingBean
    @Autowired
    SpringRabbitTracing springRabbitTracing(Tracing tracing,
                                            SrbSleuthMessagingProperties properties) {
        return SpringRabbitTracing.newBuilder(tracing)
                .remoteServiceName(properties.getMessaging().getRabbit().getRemoteServiceName())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    SleuthRabbitBeanPostProcessor sleuthRabbitBeanPostProcessor(BeanFactory beanFactory) {
        return new SleuthRabbitBeanPostProcessor(beanFactory);
    }


    class SleuthRabbitBeanPostProcessor implements BeanPostProcessor {

        private final BeanFactory beanFactory;
        private SpringRabbitTracing tracing;

        SleuthRabbitBeanPostProcessor(BeanFactory beanFactory) {
            this.beanFactory = beanFactory;
        }

        @Override public Object postProcessBeforeInitialization(Object bean, String beanName)
                throws BeansException {
            if (bean instanceof RabbitTemplate) {
                return rabbitTracing()
                        .decorateRabbitTemplate((RabbitTemplate) bean);
            } else if (bean instanceof SimpleRabbitListenerContainerFactory) {
                return rabbitTracing()
                        .decorateSimpleRabbitListenerContainerFactory((SimpleRabbitListenerContainerFactory) bean);
            }
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

        SpringRabbitTracing rabbitTracing() {
            if (this.tracing == null) {
                this.tracing = this.beanFactory.getBean(SpringRabbitTracing.class);
            }
            return this.tracing;
        }
    }
}
