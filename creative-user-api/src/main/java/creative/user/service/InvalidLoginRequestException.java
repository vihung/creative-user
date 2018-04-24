/**
 * 
 */
package creative.user.service;

/**
 * @author vihung
 *
 */
public class InvalidLoginRequestException extends Exception {

    /**
     * 
     */
    public InvalidLoginRequestException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pMessage
     */
    public InvalidLoginRequestException(String pMessage) {
        super(pMessage);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pCause
     */
    public InvalidLoginRequestException(Throwable pCause) {
        super(pCause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pMessage
     * @param pCause
     */
    public InvalidLoginRequestException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pMessage
     * @param pCause
     * @param pEnableSuppression
     * @param pWritableStackTrace
     */
    public InvalidLoginRequestException(String pMessage, Throwable pCause, boolean pEnableSuppression, boolean pWritableStackTrace) {
        super(pMessage, pCause, pEnableSuppression, pWritableStackTrace);
        // TODO Auto-generated constructor stub
    }

}
