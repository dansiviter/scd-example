package uk.dansiviter.scd;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import org.h2.tools.Server;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import java.sql.SQLException;
import javax.annotation.PreDestroy;

@ApplicationScoped
public class H2Producer {
	@Produces
	private Server server;

	public void init(@Observes @Initialized(ApplicationScoped.class) Object o) throws SQLException {
		this.server = Server.createWebServer("-webPort", "8082");
		this.server.start();
	}

	@PreDestroy
	public void destroy() {
		server.stop();
	}
}
