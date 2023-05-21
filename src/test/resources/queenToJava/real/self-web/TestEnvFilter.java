package com.selfxdsd.selfweb;

import org.springframework.stereotype.Component;
import javax.servlet.*;
import java.io.IOException;

@Component
public final class TestEnvFilter implements Filter {

    private Environment environment;

    public TestEnvFilter() {
        this(() -> Boolean.valueOf(System.getenv("self_test_env")));
    }

    public TestEnvFilter(final Environment environment) {
        this.environment = environment;
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        if (this.environment.selfTestEnv()) {
            servletRequest.setAttribute("testEnvironment", "true");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    interface Environment {

        boolean selfTestEnv();
    }
}
