package uk.dansiviter.scd.rest;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

@Provider
@Produces(APPLICATION_JSON)
public class JsonbContextResolver implements ContextResolver<Jsonb> {
	@Override
	public Jsonb getContext(Class<?> type) {
		JsonbConfig config = new JsonbConfig();//.withStrictIJSON(true);
		return JsonbBuilder.create(config);
	}
}
