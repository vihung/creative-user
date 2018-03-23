/**
 *
 */
package creative.user.filter;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import creative.user.model.AccessToken;
import creative.user.model.User;
import creative.user.repository.AccessTokenRepository;
import creative.user.repository.UserRepository;

/**
 * @author Vihung Marathe
 *
 */
@Component
public class AuthFilter implements Filter {

    // Logger for this class
    private static final Log log = LogFactory.getLog(AuthFilter.class);

    @Value("${authfilter.protected.includes}")
    private String mIncludes;

    @Value("${authfilter.protected.excludes}")
    private String[] mExcludes;

    @Autowired
    private AccessTokenRepository mAccessTokenRepository;

    @Autowired
    private UserRepository mUserRepository;

    /**
     *
     */
    public AuthFilter() {
        super();
    }

    /**
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        log.debug("destroy(): Invoked");

    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(final ServletRequest pRequest, final ServletResponse pResponse, final FilterChain pChain) throws IOException, ServletException {
        // Convert to HTTP-specific objects
        final HttpServletRequest request = (HttpServletRequest) pRequest;
        final HttpServletResponse response = (HttpServletResponse) pResponse;
        final String requestUri = request.getRequestURI();

        final boolean included = currentRequestIsIncluded(requestUri);
        final boolean excluded = currentRequestIsExcluded(requestUri);

        if (excluded || !included) {
            log.debug("doFilter(): Not Applying Auth Logic for " + requestUri);
            pChain.doFilter(pRequest, pResponse);
        } else {
            // for protected requests
            log.debug("doFilter(): Applying Auth Logic for " + requestUri);

            // check if user has cookie
            final Cookie atCookie = extractAccessTokenCookie(request);
            if (atCookie == null) {
                log.debug("doFilter(): No Access Token cookie on request");
                // if not, return unauthorized response
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            } else {
                // if yes, then load access token
                final String tokenValue = atCookie.getValue();
                log.debug("doFilter(): Cookie found. tokenValue=" + tokenValue);
                final AccessToken accessToken = getAccessTokenRepository().findOne(tokenValue);

                // check expiry
                final long expiryTimestamp = accessToken.getExpiryTimestamp();
                log.debug("doFilter(): Server-side expiryTimestamp=" + new Date(expiryTimestamp));
                final long now = System.currentTimeMillis();
                if (expiryTimestamp <= now) {
                    log.debug("doFilter(): Access Token expired");
                    // if invalid, expire cookie,
                    // ensure client cookie also expired
                    atCookie.setMaxAge(0);
                    response.addCookie(atCookie);
                    // send UNAUTHORIZED response
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                } else {
                    log.debug("doFilter(): Access token present and not expired");
                    // if valid, then extend expiry on token
                    final Calendar expiry = Calendar.getInstance();
                    log.debug("doFilter(): now=" + expiry);
                    expiry.add(Calendar.DAY_OF_MONTH, 7);
                    log.debug("doFilter(): Updating access token with new expiry=" + expiry);
                    accessToken.setExpiryTimestamp(expiry.getTimeInMillis());
                    accessToken.setClientAddress(request.getRemoteAddr());
                    accessToken.setClientHostname(request.getRemoteHost());
                    getAccessTokenRepository().save(accessToken);

                    // extend expiry on cookie
                    // 7 days in seconds
                    final int maxAge = 7 * 24 * 60 * 60;
                    atCookie.setMaxAge(maxAge);
                    atCookie.setPath("/");
                    response.addCookie(atCookie);

                    // load current user
                    loadCurrentUser(request, accessToken);
                    loadAccessToken(request, accessToken);
                    pChain.doFilter(pRequest, pResponse);
                }
                log.debug("doFilter(): Returning");
            }
        }
    }

    private void loadCurrentUser(final HttpServletRequest request, final AccessToken pAccessToken) {
        final String userId = pAccessToken.getUserId();
        final User user = getUserRepository().findOne(userId);
        request.setAttribute("currentUser", user);
    }

    private void loadAccessToken(final HttpServletRequest request, final AccessToken pAccessToken) {
        request.setAttribute("accessToken", pAccessToken);
    }

    private Cookie extractAccessTokenCookie(final HttpServletRequest request) {
        Cookie atCookie = null;
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (final Cookie cookie : cookies) {
                final String name = cookie.getName();
                final String value = cookie.getValue();
                final int maxAge = cookie.getMaxAge();

                if ("ACCESS_TOKEN".equals(name)) {
                    atCookie = cookie;
                    break;
                }
            }
        }
        log.debug("hasAccessTokenCookie(): Returning atCookie=" + atCookie);
        return atCookie;
    }

    private boolean currentRequestIsExcluded(final String pRequestUri) {
        boolean excluded = false;

        for (final String excludePattern : mExcludes) {
            if (pRequestUri.startsWith(excludePattern)) {
                excluded = true;
                break;
            }

        }
        return excluded;
    }

    private boolean currentRequestIsIncluded(final String requestUri) {
        return requestUri.startsWith(mIncludes);
    }

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(final FilterConfig pConfig) throws ServletException {
        log.debug("init(): Invoked");
        log.debug("init(): pConfig=" + pConfig);
    }

    /**
     * @return the accessTokenRepository
     */
    public AccessTokenRepository getAccessTokenRepository() {
        return mAccessTokenRepository;
    }

    /**
     * @param pAccessTokenRepository
     *            the accessTokenRepository to set
     */
    public void setAccessTokenRepository(final AccessTokenRepository pAccessTokenRepository) {
        mAccessTokenRepository = pAccessTokenRepository;
    }

    /**
     * @return the userService
     */
    public UserRepository getUserRepository() {
        return mUserRepository;
    }

    /**
     * @param pUserRepository
     *            the userService to set
     */
    public void setUserRepository(final UserRepository pUserRepository) {
        mUserRepository = pUserRepository;
    }

}
