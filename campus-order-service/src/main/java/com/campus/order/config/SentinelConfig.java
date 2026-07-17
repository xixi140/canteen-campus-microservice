package com.campus.order.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Sentinel 熔断规则配置
 *
 * 熔断策略：
 * - 慢调用比例：当 createOrder 接口的慢调用比例超过阈值时，触发熔断
 * - 异常比例：当 createOrder 接口的异常比例超过阈值时，触发熔断
 */
@Configuration
public class SentinelConfig {

    @Bean
    public CommandLineRunner initSentinelRules() {
        return args -> {
            List<DegradeRule> rules = new ArrayList<>();

            // 创建订单接口熔断规则
            DegradeRule createOrderRule = new DegradeRule();
            createOrderRule.setResource("createOrder");
            createOrderRule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO);
            createOrderRule.setCount(0.5);       // 异常比例阈值 50%
            createOrderRule.setTimeWindow(10);   // 熔断时长 10秒
            createOrderRule.setMinRequestAmount(5); // 最小请求数
            createOrderRule.setStatIntervalMs(1000); // 统计窗口 1秒
            rules.add(createOrderRule);

            DegradeRuleManager.loadRules(rules);
        };
    }
}
