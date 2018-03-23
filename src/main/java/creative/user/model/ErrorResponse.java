package creative.user.model;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ErrorResponse {
    public ErrorResponse(final int pErrorCode, final String pErrorMessage) {
        super();
        mErrorCode = pErrorCode;
        mErrorMessage = pErrorMessage;
    }

    public ErrorResponse() {
        super();
    }

    private int mErrorCode;
    private String mErrorMessage;

    /**
     * @return the errorCode
     */
    public int getErrorCode() {
        return mErrorCode;
    }

    /**
     * @param pErrorCode
     *            the errorCode to set
     */
    public void setErrorCode(final int pErrorCode) {
        mErrorCode = pErrorCode;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return mErrorMessage;
    }

    /**
     * @param pErrorMessage
     *            the errorMessage to set
     */
    public void setErrorMessage(final String pErrorMessage) {
        mErrorMessage = pErrorMessage;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(17, 37, this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
