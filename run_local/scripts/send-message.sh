#!/bin/bash

aws --endpoint-url "http://localhost:4566" sqs send-message \
    --queue-url "http://sqs.sa-east-1.localhost.localstack.cloud:4566/000000000000/resilience-queue" \
    --message-body "$1"