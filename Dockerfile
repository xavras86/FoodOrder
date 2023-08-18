FROM eclipse-temurin:17
COPY build/libs/*.jar foodorder.jar
ENTRYPOINT ["java","-jar","/foodorder.jar"]
