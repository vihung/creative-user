/**
 * 
 */
package creative.user.service;

/**
 * @author vihung
 *
 */
public class UnknownEmailException extends Exception {

    /**
     * 
     */
    public UnknownEmailException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pMessage
     */
    public UnknownEmailException(String pMessage) {
        super(pMessage);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pCause
     */
    public UnknownEmailException(Throwable pCause) {
        super(pCause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pMessage
     * @param pCause
     */
    public UnknownEmailException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pMessage
     * @param pCause
     * @param pEnableSuppression
     * @param pWritableStackTrace
     */
    public UnknownEmailException(String pMessage, Throwable pCause, boolean pEnableSuppression, boolean pWritableStackTrace) {
        super(pMessage, pCause, pEnableSuppression, pWritableStackTrace);
        // TODO Auto-generated constructor stub
    }

}
