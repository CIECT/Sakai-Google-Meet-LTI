#!/bin/sh
BASE_PATH=$(dirname "$0")
ROOT_PATH=$(realpath "$BASE_PATH/..")
PROPERTIES_FILE="$BASE_PATH/application.properties"

if [ ! -f "$PROPERTIES_FILE" ]; then
  echo "Error: Properties file at $PROPERTIES_FILE not found."
  exit 1
fi

# Function to lookup property value from PROPERTIES_FILE
lookup_property() {
  grep "^$1=" "$PROPERTIES_FILE" | cut -d'=' -f2 | tr -d '\r\n'
}

JWT_KEYSTORE_FILE="$ROOT_PATH/$(lookup_property 'lti.jwk.location')"
JWT_KEYSTORE_PASS="$(lookup_property 'lti.jwk.password')"

TLS_KEYSTORE_FILE="$ROOT_PATH/$(lookup_property 'server.ssl.key-store')"
TLS_KEYSTORE_PASS="$(lookup_property 'server.ssl.key-store-password')"

echo "0. Removing existing keystores"
rm -v $JWT_KEYSTORE_FILE $TLS_KEYSTORE_FILE

echo "1. Creating TLS keystore"
mkcert -pkcs12 -p12-file $TLS_KEYSTORE_FILE localhost

echo "2. Creating JWT keystore"
keytool -genkeypair \
  -alias jwt \
  -keyalg RSA \
  -keystore $JWT_KEYSTORE_FILE \
  -storepass $JWT_KEYSTORE_PASS \
  -dname CN=development

keytool -list -rfc -keystore $JWT_KEYSTORE_FILE -storepass $JWT_KEYSTORE_PASS
