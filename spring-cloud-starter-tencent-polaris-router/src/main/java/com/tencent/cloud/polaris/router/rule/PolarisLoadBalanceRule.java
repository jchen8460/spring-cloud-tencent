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

package com.tencent.cloud.polaris.router.rule;

import java.util.Arrays;

/**
 * Load balance rule.
 *
 * @author Haotian Zhang
 */
public enum PolarisLoadBalanceRule {

	/**
	 * Weighted random load balance rule.
	 */
	WEIGHTED_RANDOM_RULE("weighted_random");

	/**
	 * Load balance strategy.
	 */
	final String policy;

	PolarisLoadBalanceRule(String strategy) {
		this.policy = strategy;
	}

	public static PolarisLoadBalanceRule fromStrategy(String strategy) {
		return Arrays.stream(values()).filter(t -> t.getPolicy().equals(strategy))
				.findAny().orElse(WEIGHTED_RANDOM_RULE);
	}

	public String getPolicy() {
		return policy;
	}

}
