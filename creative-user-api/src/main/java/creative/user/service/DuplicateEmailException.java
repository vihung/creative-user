/**
 * 
 */
package creative.user.service;

/**
 * @author vihung
 *
 */
public class DuplicateEmailException extends Exception {

    /**
     * 
     */
    public DuplicateEmailException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pMessage
     */
    public DuplicateEmailException(String pMessage) {
        super(pMessage);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pCause
     */
    public DuplicateEmailException(Throwable pCause) {
        super(pCause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pMessage
     * @param pCause
     */
    public DuplicateEmailException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pMessage
     * @param pCause
     * @param pEnableSuppression
     * @param pWritableStackTrace
     */
    public DuplicateEmailException(String pMessage, Throwable pCause, boolean pEnableSuppression, boolean pWritableStackTrace) {
        super(pMessage, pCause, pEnableSuppression, pWritableStackTrace);
        // TODO Auto-generated constructor stub
    }

}
