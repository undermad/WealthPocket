package ectimel.exceptions;

public abstract class EWalletException extends RuntimeException {
    public EWalletException(String message) {
        super(message);
    }
}
