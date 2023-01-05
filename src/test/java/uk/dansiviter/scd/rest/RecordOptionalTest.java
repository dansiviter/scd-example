package uk.dansiviter.scd.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import jakarta.json.bind.JsonbBuilder;

import org.junit.jupiter.api.Test;

public class RecordOptionalTest {
	@Test
	void test0() {
		var jsonb = JsonbBuilder.create();
		var myObject = new MyObject(Optional.of("foo"));
		var json = jsonb.toJson(myObject);
		assertEquals("{\"str\":\"foo\"}", json);  // serialised correctly

		myObject = jsonb.fromJson(json, MyObject.class);

		assertEquals("foo", myObject.str().get());  // fails
	}

	@Test
	void test1() {
		var jsonb = JsonbBuilder.create();
		var myObject = new MyOtherObject("foo", Optional.of("bar"));
		var json = jsonb.toJson(myObject);
		assertEquals("{\"str0\":\"foo\",\"str1\":\"bar\"}", json);  // serialised correctly

		myObject = jsonb.fromJson(json, MyOtherObject.class);

		assertEquals("foo", myObject.str0());  // fails
		assertEquals("bar", myObject.str1().get());
	}

	@Test
	void test2() {
		var jsonb = JsonbBuilder.create();
		var myObject = new AnotherObject("foo", "bar");
		var json = jsonb.toJson(myObject);
		assertEquals("{\"str0\":\"foo\",\"str1\":\"bar\"}", json);  // Successful

		myObject = jsonb.fromJson(json, AnotherObject.class);

		assertEquals("foo", myObject.str0());  // fails
		assertEquals("bar", myObject.str1());
	}

	public static record MyObject(Optional<String> str) { }

	public static record MyOtherObject(String str0, Optional<String> str1) { }

	public static record AnotherObject(String str0, String str1) {
		// public AnotherObject() {
		// 	this("hello", "world");
		// }
	}
}
