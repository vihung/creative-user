package creative.user.model


import org.junit.Test

import spock.lang.Specification

class AccessTokenSpec extends Specification {
    creative.user.model.AccessToken a = new AccessToken()

    @Test
    def 'Set and Get User Id'() {
        given:
        a != null;

        when:
        a.userId = '1234';

        then:
        a.userId == '1234';
    }

    @Test
    def 'Set and Get Token Value'() {
        given:
        a != null;

        when:
        a.tokenValue = '1234';

        then:
        a.tokenValue == '1234';
    }

    @Test
    def 'Set and Get Created Timestamp'() {
        given:
        a != null;

        when:
        a.createdTimestamp = 1234l;

        then:
        a.createdTimestamp == 1234l;
    }

    @Test
    def 'Set and Get Expiry Timestamp'() {
        given:
        a != null;

        when:
        a.expiryTimestamp = 1234l;

        then:
        a.expiryTimestamp == 1234l;
    }

    @Test
    def 'Set and Get Client Address'() {
        given:
        a != null;

        when:
        a.clientAddress = '127.0.0.1';

        then:
        a.clientAddress == '127.0.0.1';
    }

    @Test
    def 'Set and Get Client Hostname'() {
        given:
        a != null;

        when:
        a.clientAddress = 'foo.local';

        then:
        a.clientAddress == 'foo.local';
    }
}
