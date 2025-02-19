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

package com.tencent.cloud.polaris.ratelimit.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tencent.cloud.metadata.constant.MetadataConstant.SystemMetadataKey;
import com.tencent.cloud.metadata.context.MetadataContextHolder;
import com.tencent.cloud.polaris.ratelimit.constant.RateLimitConstant;
import com.tencent.cloud.polaris.ratelimit.utils.QuotaCheckUtils;
import com.tencent.polaris.ratelimit.api.core.LimitAPI;
import com.tencent.polaris.ratelimit.api.rpc.QuotaResponse;
import com.tencent.polaris.ratelimit.api.rpc.QuotaResultCode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

/**
 * Servlet filter to check quota.
 *
 * @author Haotian Zhang
 */
@Order(RateLimitConstant.FILTER_ORDER)
public class QuotaCheckServletFilter extends OncePerRequestFilter {

	private static final Logger LOG = LoggerFactory
			.getLogger(QuotaCheckServletFilter.class);

	private final LimitAPI limitAPI;

	public QuotaCheckServletFilter(LimitAPI limitAPI) {
		this.limitAPI = limitAPI;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String localNamespace = MetadataContextHolder.get()
				.getSystemMetadata(SystemMetadataKey.LOCAL_NAMESPACE);
		String localService = MetadataContextHolder.get()
				.getSystemMetadata(SystemMetadataKey.LOCAL_SERVICE);
		String method = MetadataContextHolder.get()
				.getSystemMetadata(SystemMetadataKey.LOCAL_PATH);
		Map<String, String> labels = null;
		if (StringUtils.isNotBlank(method)) {
			labels = new HashMap<>();
			labels.put("method", method);
		}

		try {
			QuotaResponse quotaResponse = QuotaCheckUtils.getQuota(limitAPI,
					localNamespace, localService, 1, labels, null);
			if (quotaResponse.getCode() == QuotaResultCode.QuotaResultLimited) {
				response.setStatus(TOO_MANY_REQUESTS.value());
				response.getWriter().write(
						RateLimitConstant.QUOTA_LIMITED_INFO + quotaResponse.getInfo());
			}
			else {
				filterChain.doFilter(request, response);
			}
		}
		catch (Throwable t) {
			// 限流API调用出现异常，不应该影响业务流程的调用
			LOG.error("fail to invoke getQuota, service is " + localService, t);
			filterChain.doFilter(request, response);
		}
	}

}
