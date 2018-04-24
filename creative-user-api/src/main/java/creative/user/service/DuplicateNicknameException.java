/**
 * 
 */
package creative.user.service;

/**
 * @author vihung
 *
 */
public class DuplicateNicknameException extends Exception {

    /**
     * 
     */
    public DuplicateNicknameException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pMessage
     */
    public DuplicateNicknameException(String pMessage) {
        super(pMessage);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pCause
     */
    public DuplicateNicknameException(Throwable pCause) {
        super(pCause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pMessage
     * @param pCause
     */
    public DuplicateNicknameException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param pMessage
     * @param pCause
     * @param pEnableSuppression
     * @param pWritableStackTrace
     */
    public DuplicateNicknameException(String pMessage, Throwable pCause, boolean pEnableSuppression, boolean pWritableStackTrace) {
        super(pMessage, pCause, pEnableSuppression, pWritableStackTrace);
        // TODO Auto-generated constructor stub
    }

}
