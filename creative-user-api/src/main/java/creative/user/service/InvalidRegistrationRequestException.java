/**
 * 
 */
package creative.user.service;

/**
 * @author vihung
 *
 */
public class InvalidRegistrationRequestException extends Exception {

    /**
     * 
     */
    public InvalidRegistrationRequestException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pMessage
     */
    public InvalidRegistrationRequestException(String pMessage) {
        super(pMessage);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pCause
     */
    public InvalidRegistrationRequestException(Throwable pCause) {
        super(pCause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pMessage
     * @param pCause
     */
    public InvalidRegistrationRequestException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pMessage
     * @param pCause
     * @param pEnableSuppression
     * @param pWritableStackTrace
     */
    public InvalidRegistrationRequestException(String pMessage, Throwable pCause, boolean pEnableSuppression, boolean pWritableStackTrace) {
        super(pMessage, pCause, pEnableSuppression, pWritableStackTrace);
        // TODO Auto-generated constructor stub
    }

}
