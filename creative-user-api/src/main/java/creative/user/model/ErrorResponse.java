package creative.user.model;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ErrorResponse {
    private String mErrorCode;

    private String mErrorMessage;

    public ErrorResponse() {
        super();
    }

    public ErrorResponse(final String pErrorCode, final String pErrorMessage) {
        super();
        mErrorCode = pErrorCode;
        mErrorMessage = pErrorMessage;
    }

    /**
     * @return the errorCode
     */
    public String getErrorCode() {
        return mErrorCode;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return mErrorMessage;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(17, 37, this);
    }

    /**
     * @param pErrorCode
     *            the errorCode to set
     */
    public void setErrorCode(final String pErrorCode) {
        mErrorCode = pErrorCode;
    }

    /**
     * @param pErrorMessage
     *            the errorMessage to set
     */
    public void setErrorMessage(final String pErrorMessage) {
        mErrorMessage = pErrorMessage;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
