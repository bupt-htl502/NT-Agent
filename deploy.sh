BASE_DIR=$(cd "$(dirname "$0")"; pwd)


echo "==== start backend ===="
cd "$BASE_DIR/Server" || exit
nohup mvn spring-boot:run > "$BASE_DIR/backend.log" 2>&1 &

echo "==== start frontend ===="
cd "$BASE_DIR/Webapp" || exit
nohup npm run dev -- --host --port 5174 > "$BASE_DIR/frontend.log" 2>&1 &

echo "==== deploy ok: $BASE_DIR/backend.log / $BASE_DIR/frontend.log ===="
