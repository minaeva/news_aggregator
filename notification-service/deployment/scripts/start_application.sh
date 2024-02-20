#!/bin/bash
echo "Starting notification-service..."
cd /home/ec2-user/services/notification-service
pwd
ls -la
docker-compose up -d --force-recreate notification-service
echo "Notification-service started."