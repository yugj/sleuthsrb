package cn.yugj.springclound.sleuthsrb;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yugj
 * @date 2019/1/23 上午9:44.
 */
@SpringBootApplication
@EnableRabbit
public class SleuthSrbStart implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(SleuthSrbStart.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("runner");

    }
}
