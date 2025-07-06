FROM ubuntu:25.04
ENV LANG=en_US.UTF-8

ARG DEBIAN_FRONTEND=noninteractive

ENV LANG=en_AU.UTF-8
ENV LANGUAGE=en_AU:en
ENV LC_ALL=en_AU.UTF-8
ENV TZ=Australia/Brisbane

RUN apt-get update -y
RUN apt-get install -y wget gnupg2 \
  inotify-tools \
  language-pack-en \
  locales && \
  locale-gen en_AU.UTF-8

RUN update-ca-certificates

RUN apt-get install -y curl apt-transport-https unzip

RUN wget https://storage.googleapis.com/dart-archive/channels/stable/release/3.8.1/sdk/dartsdk-linux-arm64-release.zip -O /tmp/dartsdk-linux-arm64-release.zip
RUN unzip /tmp/dartsdk-linux-arm64-release.zip -d /usr/local
RUN rm /tmp/dartsdk-linux-arm64-release.zip
ENV PATH="/usr/local/dart-sdk/bin:$PATH"

ENV PATH="$PATH":"/root/.pub-cache/bin"

RUN curl -fsSL https://www.mongodb.org/static/pgp/server-8.0.asc | \
  gpg --dearmor -o /etc/apt/trusted.gpg.d/mongodb-server-8.0.gpg
RUN echo "deb [ arch=arm64 ] https://repo.mongodb.org/apt/ubuntu noble/mongodb-org/8.0 multiverse" | \
  tee /etc/apt/sources.list.d/mongodb-org-8.0.list
RUN apt-get update -y
RUN apt-get install -y mongodb-org
RUN mkdir -p /data/db

RUN mkdir -p /workspace/ktc

WORKDIR /workspace/ktc

CMD ["/usr/bin/mongod", "--logpath", "/var/log/mongodb.log"]
