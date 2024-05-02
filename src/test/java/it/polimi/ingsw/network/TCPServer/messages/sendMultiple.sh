#!/bin/bash
{
  cat chosenCard.json
  cat joinGame2.json
  cat createGame.json
  cat joinGame2.json
  cat joinGame2_duplicate.json
  cat invalidCmd.json
  cat joinGame3.json
  cat chosenCard.json
} | nc localhost 12345