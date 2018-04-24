/**
 * 
 */
package creative.user.service;

/**
 * @author vihung
 *
 */
public class InvalidUpdateUserRequestException extends Exception {

    /**
     * 
     */
    public InvalidUpdateUserRequestException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pMessage
     */
    public InvalidUpdateUserRequestException(String pMessage) {
        super(pMessage);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pCause
     */
    public InvalidUpdateUserRequestException(Throwable pCause) {
        super(pCause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pMessage
     * @param pCause
     */
    public InvalidUpdateUserRequestException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pMessage
     * @param pCause
     * @param pEnableSuppression
     * @param pWritableStackTrace
     */
    public InvalidUpdateUserRequestException(String pMessage, Throwable pCause, boolean pEnableSuppression, boolean pWritableStackTrace) {
        super(pMessage, pCause, pEnableSuppression, pWritableStackTrace);
        // TODO Auto-generated constructor stub
    }

}
