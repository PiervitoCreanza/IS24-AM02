#!/bin/bash

# Access the arguments
GHCR_AUTH_TOKEN=$2

#Login to GitHub Container Registry
echo "$GHCR_AUTH_TOKEN" | docker login ghcr.io -u piervitocreanza --password-stdin

# Run the docker compose
docker-compose up -d