FROM maven:3-openjdk-11 as builder

WORKDIR /eshop-discount

COPY src/ src/
COPY pom.xml .

RUN mvn clean package

FROM openjdk:11-jre

COPY --from=builder /eshop-discount/target/eshop-discount.jar /eshop-discount.jar

COPY config.yaml /data

ENTRYPOINT ["java", "-jar", "/eshop-discount.jar"]
