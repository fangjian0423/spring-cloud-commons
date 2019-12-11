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

package org.springframework.cloud.loadbalancer.core;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.core.env.Environment;

/**
 * @author Olga Maciaszek-Sharma
 */
public class ZonePreferenceLoadBalancerFilter implements LoadBalancerFilter {

	private final Environment environment;
	private String zone;

	public ZonePreferenceLoadBalancerFilter(Environment environment) {
		this.environment = environment;
	}

	@Override
	public List<ServiceInstance> filter(List<ServiceInstance> serviceInstances) {
		if (zone == null) {
			zone = environment.getProperty("spring.cloud.loadbalancer.zone");
		}
		if (zone != null) {
			List<ServiceInstance> filteredInstances = serviceInstances.stream()
					.filter(serviceInstance -> serviceInstance.getZone() != null)
					.filter(serviceInstance -> zone
							.equalsIgnoreCase(serviceInstance.getZone()))
					.collect(Collectors.toList());
			if (filteredInstances.size() > 0) {
				return filteredInstances;
			}
		}
		return serviceInstances;
	}

}
