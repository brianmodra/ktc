all: build

test-java:
	mvn clean compile test-compile
	mvn dependency:copy -Dartifact=org.junit.platform:junit-platform-console-standalone:1.10.0 -DoutputDirectory=target
	mvn dependency:build-classpath -Dmdep.outputFile=/tmp/cp.txt
	export CLASSPATH=$$(cat /tmp/cp.txt):target/classes:target/test-classes:target/dependency/junit-platform-console-standalone-*.jar && \
	java -cp $$CLASSPATH -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005 \
	org.junit.platform.console.ConsoleLauncher \
	--select-class com.ktc.text.NodeBaseTest
	rm /tmp/cp.txt

build: docker_build

bash:
	if [ "x$$(docker inspect --format '{{json .Created}}' ktc)" = "x" ]; then make docker_build; fi
	if [ "$$(docker inspect --format '{{json .State.Running}}' ktc)" = "true" ]; then echo ktc is running; else make run; fi
	docker exec -it ktc bash

docker_build:
	docker build -t ktc dart/

run:
	docker run --name ktc --mount type=bind,src=dart/,dst=/workspace/ktc -d -p 127.0.0.1:8088:8080 ktc

clean:
	docker container stop ktc
	docker container rm ktc
	docker rmi ktc

.PHONY: all build clean run test bash test-java
