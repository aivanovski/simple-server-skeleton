package com.simple.server.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestLogFilter implements Filter {

	private final String nodeId = Integer.toHexString((int) (Math.random() * 256)) + "-";
	private final Logger logger = LoggerFactory.getLogger("requestLogger");
	private final AtomicInteger requestId = new AtomicInteger(0);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		ResettableStreamHttpServletRequest wrappedRequest = new ResettableStreamHttpServletRequest(httpRequest);
		try {
			Thread.currentThread().setName(nodeId + requestId.incrementAndGet());

			logger.info(request.getRemoteAddr() +
					" " + httpRequest.getMethod() +
					" " + httpRequest.getRequestURI() +
					" " + serializeParams(request) +
					" Content: " + serializeBody(wrappedRequest));

			filterChain.doFilter(wrappedRequest, response);
		} catch (Throwable e) {
			logger.error("", e);
		}
	}

	private String serializeParams(ServletRequest request) {
		StringBuilder builder = new StringBuilder(256);
		for (Map.Entry<String, String[]> param : request.getParameterMap().entrySet()) {
			builder.append(param.getKey()).append("=").append(normalize(param.getValue())).append("\t");
		}
		return builder.toString();
	}

	private String serializeBody(ResettableStreamHttpServletRequest request) {
		String result = "*EMPTY*";

		try {
			String body = IOUtils.toString(request.getReader());
			if (body != null && body.length() != 0) {
				result = body;
			}
		} catch (IOException e) {
			logger.error("", e);
		} finally {
			request.resetInputStream();
		}

		return result;
	}

	private String normalize(String[] value) {
		String result = value.length == 1 ? value[0] : Arrays.toString(value);
		return result.replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t");
	}

	@Override
	public void destroy() {
	}
}

