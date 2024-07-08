#!/bin/bash

for f in /etc/localstack/init/ready.d/resources/queues/*.json
do
  awslocal sqs create-queue --cli-input-json "file://$f"
done

for f in /etc/localstack/init/ready.d/resources/lambdas/*.json
do
  filename="$(echo "$f" | cut -d '-' -f 2,3)"
  zip -j "$filename.zip" /etc/localstack/init/ready.d/resources/lambdas/code/"$filename"/lambda_handler.py
  awslocal lambda create-function --cli-input-json "file://$f" --zip-file "fileb://$filename.zip"
  rm -rf "$filename.zip"
done

for f in /etc/localstack/init/ready.d/resources/lambdas/event-source-mappings/*.json
do
  awslocal lambda create-event-source-mapping --cli-input-json "file://$f"
done