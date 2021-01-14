package com.ices.discrete_event_simulation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
//@MapperScan("com/ices/des_simulation/mapper")
public class DiscreteEventSimulationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscreteEventSimulationApplication.class, args);
    }

}
