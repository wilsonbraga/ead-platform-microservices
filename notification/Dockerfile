# ============================================================
# Stage 1 - Build
# ============================================================
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copia o pom.xml e baixa dependências antes do código (cache de camadas)
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline -B 2>/dev/null || true

COPY src ./src
COPY pom.xml .

RUN apk add --no-cache maven && \
    mvn clean package -DskipTests -B

# ============================================================
# Stage 2 - Runtime (imagem final leve)
# ============================================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Usuário não-root por segurança
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

COPY --from=builder /app/target/*.jar app.jar

# Variáveis de ambiente configuráveis
ENV JAVA_OPTS="-Xms256m -Xmx512m"
ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]