package com.ludo.study.studymatchingplatform.auth.common.util;

import jakarta.servlet.http.HttpServletRequest;

public class WebUtils {

	private static String getHeader(final Headers headers, final HttpServletRequest request) {
		final String header = headers.getHeader();
		return request.getHeader(header);
	}

	private static String getUnknown(final Headers headers) {
		return headers.getHeader();
	}

	public static String getUserAgent(final HttpServletRequest request) {
		return getHeader(Headers.USER_AGENT, request);
	}

	public static String getClientIp(final HttpServletRequest request) {

		String ip = getHeader(Headers.X_FORWARDED_FOR, request);
		final String unknown = getUnknown(Headers.UNKNOWN);

		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase(unknown)) {
			ip = getHeader(Headers.X_REAL_IP, request);
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase(unknown)) {
			ip = getHeader(Headers.X_ORIGINAL_FORWARDED_FOR, request);
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase(unknown)) {
			ip = getHeader(Headers.PROXY_CLIENT_IP, request);
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase(unknown)) {
			ip = getHeader(Headers.WL_PROXY_CLIENT_IP, request);
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase(unknown)) {
			ip = getHeader(Headers.HTTP_X_FORWARDED_FOR, request);
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase(unknown)) {
			ip = getHeader(Headers.HTTP_X_FORWARDED, request);
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase(unknown)) {
			ip = getHeader(Headers.HTTP_X_CLUSTER_CLIENT_IP, request);
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase(unknown)) {
			ip = getHeader(Headers.HTTP_CLIENT_IP, request);
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase(unknown)) {
			ip = getHeader(Headers.HTTP_FORWARDED_FOR, request);
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase(unknown)) {
			ip = getHeader(Headers.HTTP_FORWARDED, request);
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase(unknown)) {
			ip = getHeader(Headers.HTTP_VIA, request);
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase(unknown)) {
			ip = getHeader(Headers.REMOTE_ADDR, request);
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase(unknown)) {
			ip = request.getRemoteAddr();
		}

		return ip;
	}

}
