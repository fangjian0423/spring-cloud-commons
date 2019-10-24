/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.client.loadbalancer.reactive;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * An auto-configuration that provides a {@link BeanPostProcessor} that allows the use of
 * a {@link LoadBalanced} {@link WebClient.Builder} with
 * {@link ReactorLoadBalancerExchangeFilterFunction} and {@link ReactiveLoadBalancer} used
 * under the hood. NOTE: This has been extracted to a separate configuration in order to
 * not impact instantiation and post-processing of other Reactor-LoadBalancer-related
 * beans.
 *
 * @author Olga Maciaszek-Sharma
 * @since 2.2.0
 */
@Configuration
@ConditionalOnClass(WebClient.class)
@ConditionalOnBean(ReactiveLoadBalancer.Factory.class)
@Conditional(ReactorLoadBalancerClientAutoConfiguration.OnNoRibbonDefaultCondition.class)
public class ReactorLoadBalancerBeanPostProcessorConfiguration {

	@Bean
	public LoadBalancerWebClientBuilderBeanPostProcessor loadBalancerWebClientBuilderBeanPostProcessor(
			DeferringReactorLoadBalancerExchangeFilterFunction deferringExchangeFilterFunction) {
		return new LoadBalancerWebClientBuilderBeanPostProcessor(
				deferringExchangeFilterFunction);
	}

	@Bean
	DeferringReactorLoadBalancerExchangeFilterFunction deferringLoadBalancerExchangeFilterFunction(
			ObjectProvider<ReactorLoadBalancerExchangeFilterFunction> exchangeFilterFunctionProvider) {
		return new DeferringReactorLoadBalancerExchangeFilterFunction(
				exchangeFilterFunctionProvider);
	}

}
