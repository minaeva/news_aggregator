#!/bin/bash
echo "Starting application..."
cd /home/ec2-user/services/aggregator-service
pwd
ls -la
docker-compose up --build > /tmp/logfile.log 2> /tmp/errorlog.log
echo "Application started."
