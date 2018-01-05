package org.libermundi.theorcs.exceptions;

public class ImageManipulationException extends Exception {
	public ImageManipulationException(String message) {
		super(message);
	}

	public ImageManipulationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ImageManipulationException(Throwable cause) {
		super(cause);
	}
}
