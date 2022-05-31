package rightshot.converters;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class LocalDateDeserializerSlash extends StdDeserializer<LocalDate> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected LocalDateDeserializerSlash() {
		super(LocalDate.class);
	}

	@Override
	public LocalDate deserialize(final JsonParser jp, final DeserializationContext ctxt)
			throws IOException, JsonProcessingException {

		return LocalDate.parse(jp.readValueAs(String.class), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
