FROM openjdk:21-jdk-slim
WORKDIR /app
ENV TZ=Asia/Seoul
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
COPY build/libs/be-0.0.1-SNAPSHOT.jar /app/be.jar
CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "be.jar"]

EXPOSE 8080