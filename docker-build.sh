#!/bin/bash
# ti-live-app Docker 镜像构建脚本
# 使用前请确保：
#   1. Docker Desktop 已启动
#   2. 已执行 mvn clean package -DskipTests 完成本地编译

set -e

echo "========================================="
echo "  ti-live-app Docker 镜像构建"
echo "========================================="
echo ""

# 后端服务列表 (JAR路径:镜像名:端口)
SERVICES=(
  "ti-live-gateway/target/ti-live-gateway-1.0.jar:ti-live-gateway:9000"
  "ti-live-api/target/ti-live-api-1.0.jar:ti-live-api:8080"
  "ti-live-user/ti-live-user-provider/target/ti-live-user-provider-1.0.jar:ti-live-user-provider:8081"
  "ti-live-im/ti-live-im-provider/target/ti-live-im-provider-1.0.jar:ti-live-im-provider:8085"
  "ti-live-live/ti-live-live-provider/target/ti-live-live-provider-1.0.jar:ti-live-live-provider:8086"
  "ti-live-vod/ti-live-vod-provider/target/ti-live-vod-provider-1.0.jar:ti-live-vod-provider:8087"
)

TOTAL=${#SERVICES[@]}
COUNT=0

# 构建所有后端服务
for entry in "${SERVICES[@]}"; do
  JAR_PATH="${entry%%:*}"
  REMAINDER="${entry#*:}"
  IMAGE_NAME="${REMAINDER%%:*}"
  PORT="${REMAINDER##*:}"

  COUNT=$((COUNT + 1))

  if [ ! -f "$JAR_PATH" ]; then
    echo "[$COUNT/$TOTAL] ⚠️  跳过 $IMAGE_NAME — 未找到 $JAR_PATH"
    echo ""
    continue
  fi

  echo "[$COUNT/$TOTAL] 构建 $IMAGE_NAME (端口=$PORT) ..."
  docker build -f Dockerfile.runtime \
    --build-arg JAR_PATH="$JAR_PATH" \
    --build-arg PORT="$PORT" \
    -t "$IMAGE_NAME":latest .
  echo ">>> $IMAGE_NAME 构建完成"
  echo ""
done

# 构建前端
echo "[FRONTEND] 构建 ti-live-web ..."
if [ -d "ti-live-web/dist" ]; then
  docker build -f Dockerfile.web -t ti-live-web:latest .
  echo ">>> ti-live-web 构建完成"
else
  echo "⚠️  ti-live-web/dist 不存在，请先执行 cd ti-live-web && npm run build"
fi

echo ""
echo "========================================="
echo "  镜像构建完成！"
echo "========================================="
echo ""
echo "已构建镜像:"
docker images --format "{{.Repository}}:{{.Tag}}  ({{.Size}})" | grep -E "^(ti-live-|id-generate-)"
echo ""
echo "===== 启动指南 ====="
echo ""
echo "前置依赖 (必须提前启动):"
echo "  - Nacos (127.0.0.1:8848)"
echo "  - MySQL (127.0.0.1:3306)"
echo "  - Redis (127.0.0.1:6379)"
echo ""
echo "启动所有后端服务 (需 Linux 主机或 WSL2):"
echo "  docker run --network host -d ti-live-gateway:latest"
echo "  docker run --network host -d ti-live-api:latest"
echo "  docker run --network host -d ti-live-user-provider:latest"
echo "  docker run --network host -d ti-live-im-provider:latest"
echo "  docker run --network host -d ti-live-live-provider:latest"
echo "  docker run --network host -d ti-live-vod-provider:latest"
echo ""
echo "启动前端:"
echo "  docker run -d -p 80:80 ti-live-web:latest"
echo ""
echo "Windows 上替代方案 (端口映射):"
echo "  docker run -d -p 9000:9000 ti-live-gateway:latest"
echo "  # 注意: 需修改配置中的 127.0.0.1 为 host.docker.internal"
