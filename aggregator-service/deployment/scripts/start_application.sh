#!/bin/bash
echo "Starting aggregator-service..."
cd /home/ec2-user/services/aggregator-service
pwd
ls -la
docker-compose up -d --force-recreate aggregator-service
echo "Aggregator-service started."
