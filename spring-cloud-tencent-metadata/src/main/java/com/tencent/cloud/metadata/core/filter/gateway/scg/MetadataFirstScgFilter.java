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
 *
 */

package com.tencent.cloud.metadata.core.filter.gateway.scg;

import com.tencent.cloud.metadata.constant.MetadataConstant;
import com.tencent.cloud.metadata.context.MetadataContext;
import com.tencent.cloud.metadata.context.MetadataContextHolder;
import reactor.core.publisher.Mono;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;

import static org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter.ROUTE_TO_URL_FILTER_ORDER;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

/**
 * Scg output first filter used for setting peer info in context.
 *
 * @author Haotian Zhang
 */
public class MetadataFirstScgFilter implements GlobalFilter, Ordered {

	/**
	 * Order of MetadataFirstScgFilter.
	 */
	public static final int METADATA_FIRST_FILTER_ORDER = ROUTE_TO_URL_FILTER_ORDER + 1;

	@Override
	public int getOrder() {
		return METADATA_FIRST_FILTER_ORDER;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// get request context
		Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);

		// get metadata of current thread
		MetadataContext metadataContext = exchange
				.getAttribute(MetadataConstant.HeaderName.METADATA_CONTEXT);

		// TODO 对端命名空间暂时与本地命名空间相同
		metadataContext.putSystemMetadata(
				MetadataConstant.SystemMetadataKey.PEER_NAMESPACE,
				MetadataContextHolder.get().getSystemMetadata(
						MetadataConstant.SystemMetadataKey.LOCAL_NAMESPACE));
		metadataContext.putSystemMetadata(MetadataConstant.SystemMetadataKey.PEER_SERVICE,
				route.getId());
		metadataContext.putSystemMetadata(MetadataConstant.SystemMetadataKey.PEER_PATH,
				exchange.getRequest().getURI().getPath());

		exchange.getAttributes().put(MetadataConstant.HeaderName.METADATA_CONTEXT,
				metadataContext);

		return chain.filter(exchange);
	}

}
