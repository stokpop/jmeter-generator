# JMeter Generator

Docker image with a JMeter generator based on OpenAPI specs.

This project contains a SpringBoot application and the docker image create scipts

# Run

    docker run --rm -d -p 4756:8080 --name jmeter-gen stokpop/jmeter-gen:0.0.1

Run without `-d` to see logging and stop with `ctrl-c`.

# Create mappers

Use curl or postman to create mapper files based on an openapi spec in yaml or json.
Try this example, which works from the project root directory where the `example/` dir is:

    curl -F "file=@example/afterburner-api-docs.json" http://localhost:4756/upload

You get a reply like the following:

    {"projectId":"wiremock-gen.1668434485709"}

You can use that `projectId` to download the resulting zip file:

    curl -sS --remote-header-name -O localhost:4756/download/jmeter-gen.1668434485709

Unzip the file and load the `generated-script.jmx` file into jMeter.

    unzip jmeter-gen.1668434485709.zip

Run the script from jMeter.

# References

* jmeter-dsl: https://github.com/stokpop/jmeter-dsl
* afterburner: https://github.com/perfana/afterburner
* wiremock-generator: https://github.com/stokpop/wiremock-generator
* openapi-generator: https://github.com/stokpop/openapi-generator

