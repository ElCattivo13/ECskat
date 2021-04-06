package io.github.elcattivo13.ecskat.errorhandling;

public class KarteNotAllowedException extends EcSkatException {

	private static final long serialVersionUID = 652577838146668350L;

	public KarteNotAllowedException() {
        super(Reason.KARTE_NOT_ALLOWED);
    }
}
