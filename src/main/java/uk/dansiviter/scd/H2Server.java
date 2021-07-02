package uk.dansiviter.scd;

import java.sql.SQLException;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;

import org.h2.tools.Server;

@ApplicationScoped
public class H2Server {
	private Server server;

	public void init(@Observes @Initialized(ApplicationScoped.class) Object o) throws SQLException {
		this.server = Server.createWebServer("-webPort", "8082").start();
	}

	@PreDestroy
	public void destroy() {
		server.stop();
	}
}
