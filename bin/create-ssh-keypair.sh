KEY_NAME=$1
KEY_COMMENT="$KEY_NAME User Public Key"

PASSWORD_FILE_NAME="$KEY_NAME.password"
#PASSWORD=`date +%s | shasum | base64 | head -c 8`
PASSWORD=

mkdir -p keys
ssh-keygen -q -t rsa -f keys/$KEY_NAME -N "$PASSWORD" -C "$KEY_COMMENT"
echo $PASSWORD > keys/$PASSWORD_FILE_NAME
chmod 600 keys/$KEY_NAME*

mv keys/$KEY_NAME.pub ansible/keys
