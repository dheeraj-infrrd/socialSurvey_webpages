package com.realtech.socialsurvey.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.ELRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;

import com.realtech.socialsurvey.core.commons.CommonConstants;
import com.realtech.socialsurvey.core.exception.UserSessionInvalidateException;

@SuppressWarnings("deprecation")
public class AjaxAwareLoginUrlAuthenticationEntryPoint extends
		LoginUrlAuthenticationEntryPoint {

	private static final Logger LOG = LoggerFactory
			.getLogger(AjaxAwareLoginUrlAuthenticationEntryPoint.class);
	private static final RequestMatcher requestMatcher = new ELRequestMatcher(
			"hasHeader('X-Requested-With','XMLHttpRequest')");

	public AjaxAwareLoginUrlAuthenticationEntryPoint() {
		super();
	}

	public AjaxAwareLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
	}

	@Override
	public void commence(final HttpServletRequest request,
			final HttpServletResponse response,
			final AuthenticationException authException) throws IOException,
			ServletException {

		if (requestMatcher.matches(request)) {
			LOG.info("Redirecting to login.do");
			StringBuilder urlBuilder = new StringBuilder();
			urlBuilder.append(request.getScheme()).append("://")
					.append(request.getServerName());

			// if port is other than 80 or 443, then append that to the url
			if (request.getServerPort() != 80 && request.getServerPort() != 443) {
				urlBuilder.append(":").append(request.getServerPort());
			}
			urlBuilder.append("/login.do?s=sessionerror");
			LOG.info("Url it is redirecting too " + urlBuilder.toString());
			/*
			 * RedirectStrategy redirectStrategy = new
			 * DefaultRedirectStrategy(); redirectStrategy.sendRedirect(request,
			 * response, urlBuilder.toString());
			 */
			response.setStatus(HttpServletResponse.SC_RESET_CONTENT);
			/*
			 * response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
			 * "Unauthorized");
			 */
			/*
			 * throw new UserSessionInvalidateException(
			 * "User session is no longer available.");
			 */
			return;
		} else {
			super.commence(request, response, authException);
		}
	}
}