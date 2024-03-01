#!/bin/sh
#STORE_PASS=changeit

rm jwk.jks keystore.p12

mkcert -pkcs12 -p12-file ./keystore.p12 localhost

keytool -genkeypair \
  -alias jwt \
  -keyalg RSA \
  -keystore jwk.jks \
  -storepass store-pass \
  -dname CN=development

keytool -list -rfc -keystore jwk.jks -alias jwt -storepass store-pass
