#!/usr/bin/env bash

DEV_DIR=/Users/pp/stokpop/git
cp $DEV_DIR/jmeter-generator/target/generator-0.0.1-SNAPSHOT.jar app.jar
cp $DEV_DIR/jmeter-dsl/target/jmeter-dsl-1.0-SNAPSHOT.jar jmeter-dsl.jar
cp $DEV_DIR/openapi-generator/modules/openapi-generator-cli/target/openapi-generator-cli.jar openapi-generator-cli.jar

docker build -t stokpop/jmeter-gen:0.0.1 .
