package creative.user.test

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient

class CreativeUserClient {
    private static final URL_BASE="http://localhost:8080/api";

    public static final String URL_LOGIN = URL_BASE + "/session/new";

    /**
     *
     * @param pUsername
     * @param pPassword
     * @return
     */
    public login(pUsername, pPassword) {
        def request = [email: pUsername, password: pPassword];

        def client = new RESTClient(URL_LOGIN);

        try {
            def response = client.put(
                    requestContentType : ContentType.JSON,
                    path: URL_LOGIN,
                    body: request
                    );

            return response;
        } catch(e) {
            return e.response;
        }
    }
}
