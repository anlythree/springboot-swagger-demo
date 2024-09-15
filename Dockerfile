# 使用官方的 OpenJDK 作为基础镜像
FROM openjdk:8-jdk-alpine

# 将工作目录设置为 /app
WORKDIR /app

# 将打包好的 Java JAR 文件复制到镜像中
COPY target/springboot-swagger-demo-1.0-SNAPSHOT.jar /app/springboot-swagger-demo-1.0-SNAPSHOT.jar

# 暴露应用监听的端口，例如 8080
EXPOSE 8080

# 指定容器启动时执行的命令
CMD ["java", "-jar", "springboot-swagger-demo-1.0-SNAPSHOT.jar"]