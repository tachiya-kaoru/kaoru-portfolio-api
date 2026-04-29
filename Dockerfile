##
# Multi-stage build:
# 1) GradleでSpring Bootのjarを生成
# 2) jarを使って本番実行（Renderがこの起動コマンドを呼びます）
##

# --- build stage ---
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Gradle wrapper / 設定ファイル
COPY gradlew gradlew.bat settings.gradle build.gradle /app/
COPY gradle /app/gradle

# アプリ本体
COPY src /app/src

# Linux環境では実行権限が必要なことがあるため付与
RUN chmod +x ./gradlew

# bootJarでjarを生成（build.gradle側でjar名を設定している）
RUN ./gradlew --no-daemon bootJar

# --- runtime stage ---
FROM eclipse-temurin:17-jre
WORKDIR /app

# 生成されたjarを起動用にコピー
COPY --from=build /app/build/libs/spring-0.0.1-SNAPSHOT.jar /app/app.jar

# Renderは環境変数PORTを渡すので、それに合わせて起動
ENV PORT=8080
EXPOSE 8080

# sh -c にしてPORTの展開を許可
CMD ["sh", "-c", "java -jar /app/app.jar -Dserver.port=${PORT}"]