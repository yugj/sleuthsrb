package cn.yugj.springcloud.sleuthsrb.support.log;

import brave.propagation.CurrentTraceContext;
import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.sleuth.log.SleuthSlf4jProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yugj
 * @date 2019/1/24 上午9:39.
 */
@Configuration
@ConditionalOnClass(MDC.class)
@EnableConfigurationProperties(SleuthSlf4jProperties.class)
public class SrbSleuthLogAutoConfiguration {

    @Bean
//    @ConditionalOnProperty(value = "spring.sleuth.log.slf4j.enabled", matchIfMissing = true)
//    @ConditionalOnMissingBean
    public CurrentTraceContext slf4jSpanLogger2() {
        return SrbSlf4jCurrentTraceContext.create();
    }

    @Bean
    @ConditionalOnProperty(value = "spring.sleuth.log.slf4j.enabled", matchIfMissing = true)
    @ConditionalOnBean(CurrentTraceContext.class)
    public static BeanPostProcessor slf4jSpanLoggerBPP() {
        return new Slf4jBeanPostProcessor();
    }

    static class Slf4jBeanPostProcessor implements BeanPostProcessor {

        @Override public Object postProcessBeforeInitialization(Object bean,
                                                                String beanName) throws BeansException {
            return bean;
        }

        @Override public Object postProcessAfterInitialization(Object bean,
                                                               String beanName) throws BeansException {
            if (bean instanceof CurrentTraceContext && !(bean instanceof SrbSlf4jCurrentTraceContext)) {
                return SrbSlf4jCurrentTraceContext.create((CurrentTraceContext) bean);
            }
            return bean;
        }
    }
}
