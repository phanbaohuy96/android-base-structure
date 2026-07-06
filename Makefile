.PHONY: setup build test lint coverage format init

setup:
	./gradlew --version

build:
	./gradlew :app:assembleDevDebug

test:
	./gradlew testDevDebugUnitTest

lint:
	./gradlew detekt spotlessCheck

coverage:
	./gradlew :app:koverVerifyDevDebug :data:koverVerifyDevDebug :core:koverVerifyDebug :domain:koverVerify

format:
	./gradlew spotlessApply

init:
	./init_project.sh
