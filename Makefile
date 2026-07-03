.PHONY: setup build test lint format init

setup:
	./gradlew --version

build:
	./gradlew :app:assembleDevDebug

test:
	./gradlew testDevDebugUnitTest

lint:
	./gradlew detekt spotlessCheck

format:
	./gradlew spotlessApply

init:
	./init_project.sh
