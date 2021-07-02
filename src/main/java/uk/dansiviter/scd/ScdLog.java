package uk.dansiviter.scd;

import static uk.dansiviter.juli.annotations.Message.Level.ERROR;
import static uk.dansiviter.juli.annotations.Message.Level.WARN;

import javax.ws.rs.WebApplicationException;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Supplier;
import uk.dansiviter.juli.annotations.Log;
import uk.dansiviter.juli.annotations.Message;

@Log
public interface ScdLog {
	@Message(value = "{0}", level = WARN)
	void clientError(Supplier<String> object);

	@Message(value = "{0}", level = ERROR)
	void serverError(Supplier<String> object, WebApplicationException ex);
}
