/**
 *
 */
package creative.user.service;

/**
 * @author vihung
 *
 */
public class IncorrectPasswordException extends Exception {

    /**
     *
     */
    public IncorrectPasswordException() {
        super();
    }

    /**
     * @param pMessage
     */
    public IncorrectPasswordException(final String pMessage) {
        super(pMessage);
    }

    /**
     * @param pMessage
     * @param pCause
     */
    public IncorrectPasswordException(final String pMessage, final Throwable pCause) {
        super(pMessage, pCause);
    }

    /**
     * @param pMessage
     * @param pCause
     * @param pEnableSuppression
     * @param pWritableStackTrace
     */
    public IncorrectPasswordException(final String pMessage, final Throwable pCause, final boolean pEnableSuppression, final boolean pWritableStackTrace) {
        super(pMessage, pCause, pEnableSuppression, pWritableStackTrace);
    }

    /**
     * @param pCause
     */
    public IncorrectPasswordException(final Throwable pCause) {
        super(pCause);
    }

}
