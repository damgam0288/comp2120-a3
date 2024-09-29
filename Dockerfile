FROM eclipse-temurin:17-jdk
WORKDIR /usr/src/game
COPY . .
RUN ./gradlew build
CMD ["java", "-jar", "build/libs/comp2120-wed16_a3_f.jar"]