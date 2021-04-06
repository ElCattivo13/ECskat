package io.github.elcattivo13.ecskat.errorhandling;

public class UnknownFarbeException extends EcSkatException {

	private static final long serialVersionUID = -5559164670455907601L;

	public UnknownFarbeException() {
        super(Reason.UNKNOWN_FARBE);
    }
}
