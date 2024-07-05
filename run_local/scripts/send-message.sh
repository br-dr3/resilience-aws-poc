#!/bin/bash

aws --endpoint "http://localhost:4566" sqs send-message \
    --queue-url "http://localhost:4566/000000000000/resilience-queue" \
    --message-body "Oi"