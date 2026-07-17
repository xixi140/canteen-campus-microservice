# ============================================================
# 多阶段构建 Dockerfile
# 阶段1: 构建（Maven + JDK）
# 阶段2: 运行（JRE only，镜像更小）
# ============================================================

# ------ 阶段1: Maven 构建 ------
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build

# 先复制 POM 文件，利用 Docker 缓存层加速
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 复制源码并编译
COPY src ./src
RUN mvn clean package -DskipTests -B

# ------ 阶段2: 运行镜像 ------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 创建非 root 用户（安全最佳实践）
RUN addgroup -S campus && adduser -S campus -G campus
USER campus

# 从构建阶段复制 JAR
COPY --from=builder /build/target/*.jar app.jar

# 健康检查
HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
  CMD wget -q --spider http://localhost:8081/actuator/health || exit 1

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
