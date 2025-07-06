all: build

build: docker_build

bash:
	if [ "x$$(docker inspect --format '{{json .Created}}' ktc)" = "x" ]; then make docker_build; fi
	if [ "$$(docker inspect --format '{{json .State.Running}}' ktc)" = "true" ]; then echo ktc is running; else make run; fi
	docker exec -it ktc bash

docker_build:
	docker build -t ktc .

run:
	docker run --name ktc --mount type=bind,src=.,dst=/workspace/ktc -d -p 127.0.0.1:8088:8080 ktc

clean:
	docker container stop ktc
	docker container rm ktc
	docker rmi ktc

.PHONY: all build clean run test bash
