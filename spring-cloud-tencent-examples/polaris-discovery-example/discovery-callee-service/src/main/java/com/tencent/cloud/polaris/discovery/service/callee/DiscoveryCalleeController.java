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

package com.tencent.cloud.polaris.discovery.service.callee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Haotian Zhang
 */
@RestController
@RequestMapping("/discovery/service/callee")
public class DiscoveryCalleeController {

	private static Logger logger = LoggerFactory
			.getLogger(DiscoveryCalleeController.class);

	/**
	 * 获取当前服务的信息
	 * @return 返回服务信息
	 */
	@GetMapping("/info")
	public String info() {
		return "Discovery Service Callee";
	}

	/**
	 * 获取相加完的结果
	 * @param value1 值1
	 * @param value2 值2
	 * @return 总值
	 */
	@GetMapping("/sum")
	public int sum(@RequestParam int value1, @RequestParam int value2) {
		return value1 + value2;
	}

}
