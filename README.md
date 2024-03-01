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



## Setup in Canvas

Go to you account and add a LTI Developer Key, fill in the following:

 * Key name: Name of your tool's key (eg LTI 1.3 Tool Key)
 * Owner email: your email.
 * Redirect URIs: https://server/lti/login
 * Method: Paste JSON
 * LTI 1.3 Configuration: Go to https://server/config.json and copy and paste the content into this field.

## Local Canvas Setup

Create/Open the file `config/application.properties` and add uncomment the following blocks of properties:

    spring.security.oauth2.client.registration.canvas.*
    spring.security.oauth2.client.provider.canvas.*
    spring.security.oauth2.client.provider.canvas.token-uri=https://canvas.instructure.com/login/oauth2/token
    spring.security.oauth2.client.provider.canvas.jwk-set-uri=https://canvas.instructure.com/api/lti/security/jwks
    spring.security.oauth2.client.provider.canvas.user-name-attribute=sub

 * replace the client-id value (1234) with the ID of the LTI Developer Key you have created.
 * replace the client-secret value (secret) with the LTI Developer Key secret.
 * you can update the URIs to point to the canvas instance you are using, but it works fine using the canvas.instructure.com

 ## LTI Reference Implementation Setup

There is a reference implementation for testing LTI integrations available at https://lti-ri.imsglobal.org
which allows you to create a Platform to perform test launches with. These instructions assume you are running the tool on https://localhost:8443/

### Step 1: Generate public / private key pair for platform

 * Open [Generate Keys](https://lti-ri.imsglobal.org/keygen/index) in a new tab and keep this tab open.

### Step 2: Create a Platform (LMS)

 * Open [Manage Platforms](https://lti-ri.imsglobal.org/platforms) in a new tab
 * Click Add Platform.
 * Add a unique name for your Platform.
 * Provide a OAuth2 client id. (ex: 12345)
 * Add a audience (ex: https://lti-ri.imsglobal.org)
 * Set Tool Deep Link Service Endpoint to https://localhost:8443/
 * Copy Public key from Generate Keys tab and paste into Platform Public Key field
 * Copy Private key from Generate Keys tab and paste into Platform Private Key field
 * Run `keytool -list -rfc -keystore config/jwk.jks -alias jwt  -storepass store-pass` at the root of the project and copy the certificate into to Tool Public Key field.
 * Click Save

### Step 3: Add Deployment to Platform

 * Find your Platform in the list of Platforms
 * View your Platform
 * Click Platform Keys
 * Click Add Platform Key.
 * Create a name for your Platform Key (ex: Key 1)
 * Add Deployment ID (ex: 1, you may set up multiple later)
 * Click Save

### Step 4: Configure our tool

 * Add a oauth registration/provider for the LTI RI platform to `config/application.properties`:

       spring.security.oauth2.client.registration.ims-ri.client-id=oauth-12345
       spring.security.oauth2.client.registration.ims-ri.client-secret=unused
       spring.security.oauth2.client.registration.ims-ri.authorization-grant-type=implicit
       spring.security.oauth2.client.registration.ims-ri.scope=openid
       spring.security.oauth2.client.registration.ims-ri.redirect-uri={baseUrl}/lti/login

       spring.security.oauth2.client.provider.ims-ri.authorization-uri=https://lti-ri.imsglobal.org/platforms/343/authorizations/new
       spring.security.oauth2.client.provider.ims-ri.token-uri=https://lti-ri.imsglobal.org/platforms/343/access_tokens
       spring.security.oauth2.client.provider.ims-ri.jwk-set-uri=https://lti-ri.imsglobal.org/platforms/343/platform_keys/333.json
       spring.security.oauth2.client.provider.ims-ri.user-name-attribute=sub

 * Update OAuth2 client id (`spring.security.oauth2.client.registration.ims-ri.client-id`), same value as Platform above (ex: 12345)

 * In Platform tab, navigate to your platform
 * Click View Platform
 * Click Platform Keys
 * Copy well-known/jwks URL endpoint
 * Update JWT URI (`spring.security.oauth2.client.provider.ims-ri.jwk-set-uri`) with the copied value.

 * In Platform tab, navigate to your platform
 * Copy the OAuth2 Access Token URL value
 * Update Token URI (`spring.security.oauth2.client.provider.ims-ri.token-uri`) with the copied value.
 * Copy the OIDC Auth URL from Platform Page
 * Update Authorization URI (`spring.security.oauth2.client.provider.ims-ri.authorization-uri`) with the copied value.

Step 5: In Platform tab, view your Platform

 * Click Courses
 * Click Add Course
 * Populate course values in form
 * Click save

 * Navigate back and view your Platform
 * Click Resource Links
 * Populate resource link values in form
 * For Tool link url, use https://localhost:8443/
 * For Login initiation url, use https://localhost:8443/lti/login_initiation/ims-ri
 * Click Save

 * Navigate and view your Platform
 * Click Resource Links
 * Click Select User for Launch
 * Click on Launch Resource Link (OIDC) for a user
 * Click on Post request
 * Click on Launch Resource Link
 * Click on Perform Launch
 * If configuration is setup correctly, you should see Successful Launch at top of page
