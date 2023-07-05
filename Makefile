build:
	./mvnw clean install package

package: build
	docker build . --tag event-outbox:latest

