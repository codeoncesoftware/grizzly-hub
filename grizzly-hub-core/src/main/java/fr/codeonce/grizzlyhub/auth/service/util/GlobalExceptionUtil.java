package fr.codeonce.grizzlyhub.auth.service.util;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import org.springframework.dao.DuplicateKeyException;

public class GlobalExceptionUtil {

	public static Supplier<NoSuchElementException> notFoundException(Class<?> clazz, String id) {
		String message = clazz.getSimpleName() + " not found with id : %s";
		return () -> new NoSuchElementException(String.format(message, id));
	}

	public static Supplier<DuplicateKeyException> duplicateNameFound(Class<?> clazz, String name) {
		String message = clazz.getSimpleName() + " already exists with : %s";
		return () -> new DuplicateKeyException(String.format(message, name));
	}

	public static Supplier<IOException> fileNotFoundException(String id) {
		String message = "File not found in container with id : %s";
		return () -> new IOException(String.format(message, id));
	}

	public static Supplier<IOException> fileNotValid() {
		String message = "File is not valid";
		return () -> new IOException(String.format(message));
	}

	private GlobalExceptionUtil() {

	}

}
