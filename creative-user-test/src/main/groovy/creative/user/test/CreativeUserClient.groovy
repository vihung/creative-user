package creative.user.test

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient

class CreativeUserClient {
    private static final URL_BASE="http://localhost:8080/api";

    public static final String URL_LOGIN = URL_BASE + "/session/new";
    public static final String URL_REGISTER = URL_BASE + "/user/new";
    public static final String URL_CURRENT_USER = URL_BASE + "/user/current";

    def client = new RESTClient(URL_BASE);

    /**
     *
     * @param pUsername
     * @param pPassword
     * @return
     */
    public register(pFirstName, pLastName, pNickname, pEmail, pPassword) {
        println("register(): Invoked")
        def request = [firstName: pFirstName, lastName: pLastName, nickname: pNickname, email: pEmail, password: pPassword];
        println("register(): request=" + request);

        if(client == null) client = new RESTClient(URL_REGISTER);

        try {
            def response = client.put(
                    requestContentType : ContentType.JSON,
                    path: URL_REGISTER,
                    body: request
                    );
            println("register(): response=" + response)
            return response;
        } catch(e) {
            println("register(): " + e);
            return e.response;
        }
    }


    /**
     *
     * @param pUsername
     * @param pPassword
     * @return
     */
    public login(pUsername, pPassword) {
        println("login(): Invoked")
        def request = [email: pUsername, password: pPassword];
        println("login(): request=" + request);

        if(client == null) client = new RESTClient(URL_LOGIN);

        try {
            def response = client.put(
                    requestContentType : ContentType.JSON,
                    path: URL_LOGIN,
                    body: request
                    );
            println("logon(): response=" + response)
            return response;
        } catch(e) {
            println("login(): " + e);
            return e.response;
        }
    }
    public getCurrentUser() {

        if(client == null)  client = new RESTClient(URL_CURRENT_USER);

        try {
            def response = client.get(
                    path: URL_CURRENT_USER
                    );
            println("getCurrentUser(): response=" + response)
            return response;
        } catch(e) {
            println("login(): " + e);
            return e.response;
        }
    }
}
