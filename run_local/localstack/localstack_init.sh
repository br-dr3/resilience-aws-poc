#!/bin/bash

for f in /etc/localstack/init/ready.d/resources/queues/*.json
do
  awslocal sqs create-queue --cli-input-json "file://$f"
done
