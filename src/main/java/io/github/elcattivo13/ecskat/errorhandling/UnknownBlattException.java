package io.github.elcattivo13.ecskat.errorhandling;

public class UnknownBlattException extends EcSkatException {

	private static final long serialVersionUID = -1764592147232836531L;

	public UnknownBlattException() {
        super(Reason.UNKNOWN_BLATT);
    }
}
