FROM FROM openjdk:21-alpine

COPY --chown=internal:internal target/sogno-di-ale*.jar ./app.jar
COPY --chown=internal:internal entrypoint.sh ./

RUN chmod +x ./entrypoint.sh

ENTRYPOINT [ "./entrypoint.sh" ]