# Opencollab Meetings Tool (LTI 1.3)

This is an LTI 1.3 tool to mange meetings for the Google Meet platform.

## TLS Setup (development)

To setup TLS in a development environment it's recommended that you install [mkcert](https://github.com/FiloSottile/mkcert) which will install a trusted root certificate and then allow a certificate to be generated for localhost. Once installed run this in the root of the project:
```
mkcert -pkcs12 -p12-file config/keystore.p12 localhost
```

You might have to run the following command so your browsers trust the certificate:
```
mkcert -install
```

Then start the application with the ssl profile enabled and the LTI tool should be accessible on https://localhost:8443/

## LTI 1.3 Setup

For LTI 1.3 a public/private keypair is needed. To generate one for development use:

    keytool -genkeypair \
      -alias jwt \
      -keyalg RSA \
      -keystore config/jwk.jks \
      -storepass store-pass \
      -dname CN=development

## Google setup
* [Create a new google cloud project](https://support.google.com/googleapi/answer/6251787#zippy=%2Ccreate-a-project)
* [Create a new OAuth 2.0 Client ID](https://support.google.com/cloud/answer/6158849)
  * Set the return URL to `https://localhost:8443/login/oauth2/code/google`

## Local Sakai Setup

* In Sakai, open the Administration Workspace > External Tools
* Click on `Install LTI 1.x Tool`
* Set the tool title and button text
* Set Launch URL to `https://localhost:8443/`
* Set the Launch secret - A secret key you have to choose (e.g. super-secret-client-secret)
* Select `Tool supports LTI 1.3`
* Set LTI 1.3 Tool Keyset URL to `https://localhost:8443/.well-known/jwks.json`
* Set LTI 1.3 Tool OpenID Connect/Initialization Endpoint to `https://localhost:8443/lti/login_initiation/sakai`
* Set LTI 1.3 Tool Redirect Endpoint to `https://localhost:8443/lti/login`
* Save
* Click on the newly created tool entry
* Copy the LTI 1.3 Client ID (e.g. 2754204f-d8a0-4ec7-8a37-512f4b2556b6)
* Open the file `config/application.properties`
* Insert the client id for the sakai client 
```
spring.security.oauth2.client.registration.sakai.client-id=2754204f-d8a0-4ec7-8a37-512f4b2556b6
```
* Insert the client secret for the sakai client
```
spring.security.oauth2.client.registration.sakai.client-secret=super-secret-client-secret
```
* Also set the following url properties for the provider configuration found on the view in Sakai (Example values below)
```
### LTI 1.3 Platform OAuth2 Well-Known/KeySet URL
spring.security.oauth2.client.provider.sakai.authorization-uri=http://localhost:8080/imsoidc/lti13/oidc_auth
### LTI 1.3 Platform OAuth2 Bearer Token Retrieval URL
spring.security.oauth2.client.provider.sakai.token-uri=http://localhost:8080/imsblis/lti13/token/1
### LTI 1.3 Platform OIDC Authentication URL
spring.security.oauth2.client.provider.sakai.jwk-set-uri=http://localhost:8080/imsblis/lti13/keyset
```
