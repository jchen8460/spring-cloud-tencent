/*
 * Tencent is pleased to support the open source community by making Spring Cloud Tencent available.
 *
 * Copyright (C) 2019 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.tencent.cloud.polaris.router.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties of Ribbon.
 *
 * @author Haotian Zhang
 */
@ConfigurationProperties("spring.cloud.polaris.ribbon")
public class PolarisRibbonProperties {

	/**
	 * If load-balance enabled.
	 */
	@Value("${spring.cloud.polaris.discovery.loadbalancer.enabled:#{true}}")
	private Boolean loadbalancerEnabled;

	/**
	 * Load balance strategy.
	 */
	@Value("${spring.cloud.polaris.loadbalancer.strategy:#{'weightedRandom'}}")
	private String strategy;

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public Boolean getLoadbalancerEnabled() {
		return loadbalancerEnabled;
	}

	public void setLoadbalancerEnabled(Boolean loadbalancerEnabled) {
		this.loadbalancerEnabled = loadbalancerEnabled;
	}

	@Override
	public String toString() {
		return "PolarisRibbonProperties{" + "loadbalancerEnabled=" + loadbalancerEnabled
				+ ", strategy='" + strategy + '\'' + '}';
	}

}
