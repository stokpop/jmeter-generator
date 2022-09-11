FROM amazoncorretto:11
#FROM centos

LABEL maintainer peter.paul.bakker@stokpop.nl

#RUN yum install -y dnf-plugins-core && \
#    yum config-manager --set-enabled powertools && \
RUN    yum install -y wget && \
    yum install -y unzip && \
#    yum install -y java-11 && \
#    yum install -y xorg-x11-server-Xorg xorg-x11-xauth xorg-x11-apps && \
#    yum install -y libXext.x86_64 && \
#    yum install -y libXrender.x86_64 && \
#    yum install -y libXtst.x86_64 && \
    cd /usr/lib && \
    wget -q https://github.com/JetBrains/kotlin/releases/download/v1.6.10/kotlin-compiler-1.6.10.zip && \
    unzip kotlin-compiler-*.zip && \
    rm kotlin-compiler-*.zip && \
    rm -f kotlinc/bin/*.bat

COPY app.jar /app.jar
COPY jmeter-dsl.jar /jmeter-dsl/jmeter-dsl.jar
COPY openapi-generator-cli.jar /openapi-generator-cli.jar

RUN cd /jmeter-dsl && \
    unzip -a jmeter-dsl.jar && \
    rm jmeter-dsl.jar && \
    chmod u+x generate-from-dsl.sh

ENV PATH $PATH:/usr/lib/kotlinc/bin

CMD ["java", "-jar", "app.jar"]
