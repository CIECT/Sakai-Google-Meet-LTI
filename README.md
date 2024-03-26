# Opencollab Meetings Tool (LTI 1.3)

This is an LTI 1.3 tool to mange meetings for the Google Meet platform.

## Prerequisites
* A database - project supports mariadb by default
* Java 17 (check `java --version`)
* Maven (check `mvn --version`)

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

## Local setup (development)

* Create a copy of `config/_application.properties` at `config/application.properties`
  * `cp config/_application.properties config/application.properties`
* Open the copy and insert configuration values (see comments)
* Install mkcert and run `mkcert -install` (On ubuntu also install `libnss3-tools` as dependency before)
* Review the script `config/setup.sh`, make it executable and run it
* Build project `mvn clean install`
* Start application `mvn spring-boot:run`

## Production setup

Production setup will be similar to the local setup with differences when setting up certificates and keypairs.

* TLS can be configured using the spring-boot SSL support https://www.baeldung.com/spring-boot-https-self-signed-certificate
* To generate a keypair for the JWT involved in LTI authentification
`keytool -genkeypair -alias jwt -keyalg RSA -keystore config/jwk-keystore.jks ...` with appropiate options can be used
