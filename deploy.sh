#!/bin/bash

# Access the arguments
GHCR_AUTH_TOKEN=$2

#Login to GitHub Container Registry
echo "$GHCR_AUTH_TOKEN" | docker login ghcr.io -u piervitocreanza --password-stdin

# Pull the image
docker pull ghcr.io/piervitocreanza/is24-am02:latest

# Run the docker compose
docker-compose up -d