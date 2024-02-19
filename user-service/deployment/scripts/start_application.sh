#!/bin/bash
echo "Starting user-service..."
cd /home/ec2-user/services/user-service
pwd
ls -la
docker-compose up -d --force-recreate user-service
echo "User-service started."
