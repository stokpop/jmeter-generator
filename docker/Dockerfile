FROM bellsoft/liberica-openjdk-alpine-musl:17 as build

LABEL maintainer=peter.paul.bakker@stokpop.nl

#RUN yum install -y dnf-plugins-core && \
#    yum config-manager --set-enabled powertools && \
RUN apk add --no-cache wget && \
    apk add --no-cache unzip && \
    apk add --no-cache bash && \
    cd /usr/lib && \
    wget -q https://github.com/JetBrains/kotlin/releases/download/v1.6.10/kotlin-compiler-1.6.10.zip && \
    unzip kotlin-compiler-*.zip && \
    rm kotlin-compiler-*.zip && \
    rm -f kotlinc/bin/*.bat

COPY jmeter-dsl.jar /jmeter-dsl/jmeter-dsl.jar

RUN cd /jmeter-dsl && \
    unzip -a jmeter-dsl.jar && \
    rm jmeter-dsl.jar && \
    chmod u+x generate-from-dsl.sh

FROM bellsoft/liberica-openjdk-alpine-musl:17

RUN apk --no-cache add bash

COPY --from=build /usr/lib/kotlinc /usr/lib/kotlinc
COPY --from=build /jmeter-dsl /jmeter-dsl
COPY app.jar /app.jar
COPY openapi-generator-cli.jar /openapi-generator-cli.jar

# workaround for: java.lang.NullPointerException: Cannot load from short array because "sun.awt.FontConfiguration.head" is null
RUN apk add --no-cache fontconfig ttf-dejavu

ENV PATH $PATH:/usr/lib/kotlinc/bin

CMD ["java", "-jar", "app.jar"]
