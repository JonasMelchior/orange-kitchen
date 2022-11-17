# (1)
FROM maven:3-openjdk-17-slim as build
# (2)
RUN useradd -m jonas
# (3)
WORKDIR /usr/src/app/
# (4)
RUN chown jonas:jonas /usr/src/app/
# (5)
USER jonas
# (6)
COPY --chown=jonas pom.xml ./
# (7)
RUN mvn dependency:go-offline -Pproduction
# (8)
COPY --chown=jonas:jonas src src
COPY --chown=jonas:jonas frontend frontend
COPY --chown=jonas:jonas package.json ./
COPY --chown=jonas:jonas package-lock.json* pnpm-lock.yaml* webpack.config.js* ./
# (9)
RUN mvn clean package -DskipTests -Pproduction
# (10)
FROM openjdk:17-jdk-slim
# (11)
COPY --from=build /usr/src/app/target/*.jar /usr/app/app.jar
# (12)
RUN useradd -m jonas
# (13)
USER jonas
# (14)
EXPOSE 8080
# (15)
CMD java -jar /usr/app/app.jar