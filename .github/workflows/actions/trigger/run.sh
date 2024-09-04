#!/bin/bash

PAT=$1
WORKFLOW_FILE=$1
REPOSITORY=$2

# trigger workflow
gh workflow run $WORKFLOW_FILE -R $REPO

# fetch RUN_ID
RUN_ID=$(gh run list -R $REPO -w $WORKFLOW_FILE -L 1 --json databaseId -q '.[0].databaseId')
echo "Triggered workflow RUN_ID: $RUN_ID"

# block until the workflow done
while true; do

done
