#!/bin/bash

[ ! -e message-batch.json ] || rm message-batch.json

python3 create-message-batch.py $1 > message-batch.json

aws --endpoint-url "http://localhost:4566" sqs send-message-batch \
    --queue-url "http://sqs.sa-east-1.localhost.localstack.cloud:4566/000000000000/resilience-queue" \
    --entries "file://message-batch.json" \
    --no-cli-pager