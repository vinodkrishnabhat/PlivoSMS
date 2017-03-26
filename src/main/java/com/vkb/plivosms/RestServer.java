package com.vkb.plivosms;

import com.vkb.plivosms.services.InboundService;
import com.vkb.plivosms.services.OutboundService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class RestServer {
    public static void main(String[] args) throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        Server jettyServer = new Server(8080);
        jettyServer.setHandler(context);

        ServletHolder inboundServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/inbound/*");
        inboundServlet.setInitOrder(0);
        inboundServlet.setInitParameter("jersey.config.server.provider.classnames", InboundService.class.getCanonicalName());

        ServletHolder outboundServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/outbound/*");
        outboundServlet.setInitOrder(1);
        outboundServlet.setInitParameter("jersey.config.server.provider.classnames", OutboundService.class.getCanonicalName());

        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
    }
}