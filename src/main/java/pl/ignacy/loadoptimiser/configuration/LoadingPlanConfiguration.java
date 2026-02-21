package pl.ignacy.loadoptimiser.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.ignacy.loadoptimiser.enums.StrategyType;
import pl.ignacy.loadoptimiser.strategy.FirstFitDecreasingStrategy;
import pl.ignacy.loadoptimiser.strategy.GreedyStrategy;
import pl.ignacy.loadoptimiser.strategy.LoadOptimiserStrategy;

import java.util.EnumMap;
import java.util.List;

@Configuration
public class LoadingPlanConfiguration {

    @Bean
    public EnumMap<StrategyType, LoadOptimiserStrategy> strategyMap(List<LoadOptimiserStrategy> strategies){
        EnumMap<StrategyType, LoadOptimiserStrategy> map = new EnumMap<>(StrategyType.class);
        strategies.forEach(s -> map.put(s.getType(), s));
        return map;
    }
}
