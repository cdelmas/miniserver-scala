FROM openjdk:10.0.1-10-jdk as base
# we redefine the hseeberger/scala-sbt base image as it is using openjdk 8 to build
ENV SCALA_VERSION 2.12.6 
ENV SBT_VERSION 1.1.6
# Install Scala
RUN \
  curl -fsL https://downloads.typesafe.com/scala/$SCALA_VERSION/scala-$SCALA_VERSION.tgz | tar xfz - -C /root/ && \
  echo >> /root/.bashrc && \
  echo "export PATH=~/scala-$SCALA_VERSION/bin:$PATH" >> /root/.bashrc
# Install sbt 
RUN \
  curl -L -o sbt-$SBT_VERSION.deb https://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  apt-get update && \
  apt-get install sbt && \
  sbt sbtVersion

WORKDIR /root

FROM base as deps
COPY build.sbt .
COPY project/ ./project/
RUN sbt update

FROM deps as builder
COPY src/ ./src/
RUN sbt assembly

FROM openjdk:10.0.1-10-jre-slim as release
COPY ./statsdexporter.yaml ./statsd_mapping.conf
COPY --from=builder /root/target/scala-2.12/miniserver-scala.jar .
CMD ["java","-jar","miniserver-scala.jar"]
