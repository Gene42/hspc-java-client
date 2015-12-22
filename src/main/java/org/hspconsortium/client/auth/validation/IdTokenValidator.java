package org.hspconsortium.client.auth.validation;

import org.hspconsortium.client.auth.access.IdToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

public interface IdTokenValidator {

    boolean validate(IdToken idToken, String issuerUrl, String clientId);

    class Impl implements IdTokenValidator {

        private static final Logger LOGGER = LoggerFactory.getLogger(IdTokenValidator.Impl.class);

        /**
         * see http://openid.net/specs/openid-connect-core-1_0.html#IDTokenValidation
         * @return token is valid
         */
        @Override
        public boolean validate(IdToken idToken, String issuerUrl, String clientId) {
            Map<String, Object> claimsMap = idToken.getClaimsMap();

            // if the token was encrypted, decrypt it

            // validate issuer id
            String tokenIss = (String)claimsMap.get("iss");
            if (tokenIss == null || !issuerUrl.contains(tokenIss)) {
                LOGGER.error("Token ISS does not match! expected [" + issuerUrl + "] received: [" + tokenIss + "]");
                return false;
            }

            // validate sub
            if (claimsMap.get("sub") == null) {
                LOGGER.error("Token Sub is required");
                return false;
            }

            // validate aud
            String tokenAud = (String)claimsMap.get("aud");
            if (tokenAud == null || !tokenAud.equals(clientId)) {
                LOGGER.error("Token Aud does not match! expected [" + clientId + "] received: [" + tokenAud + "]");
                return false;
            }

            // validate azp

            // validate signature

            // validate alg

            // validate JWT alg

            // validate exp (seconds since epoch)
            Object tokenExpObj = claimsMap.get("exp");
            if (tokenExpObj == null) {
                LOGGER.error("Token Exp is required");
                return false;
            }
            Long tokenExp = null;
            if (tokenExpObj instanceof Integer) {
                tokenExp = new Long((Integer)tokenExpObj);
            } else if (tokenExpObj instanceof Long) {
                tokenExp = (Long)tokenExpObj;
            }
            Date now = new Date();
            if (now.getTime() > (tokenExp * 1000)) {
                LOGGER.error("Token Exp has expired! now [" + now.getTime() +
                        "] exp: [" + tokenExp * 1000 +
                        "] diff: [" + (tokenExp * 1000 - now.getTime()) + "]");
                return false;
            }

            // validate iat
            if (claimsMap.get("iat") == null) {
                LOGGER.error("Token Iat is required");
                return false;
            }

            // validate nonce

            // validate acr

            // validate auth_time

            return true;
        }
    }
}
