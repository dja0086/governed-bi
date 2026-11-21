#!/bin/bash
# Read-only check of the deleted-records table. Run from Terminal:
#   bash ~/Downloads/NetSuiteJDBCDrivers/ns_deleted_check.sh
# Secrets are typed here, hidden, used once, and not saved. Nothing is written to disk.
cd "$(dirname "$0")" || exit 1
if ! command -v java >/dev/null 2>&1 || ! java -version >/dev/null 2>&1; then echo "Java is not installed."; exit 3; fi
if [ ! -f NQjc.jar ]; then echo "NQjc.jar not found next to this script."; exit 3; fi
export NS_HOST="tstdrv1755417.connect.api.netsuite.com" NS_PORT="1708" NS_ACCOUNT="TSTDRV1755417" NS_ROLE="57"
echo "Paste each value and press Return. Nothing will show as you paste."
read -r -s -p "Consumer key:    " NS_CONSUMER_KEY; echo
read -r -s -p "Consumer secret: " NS_CONSUMER_SECRET; echo
read -r -s -p "Token ID:        " NS_TOKEN_ID; echo
read -r -s -p "Token secret:    " NS_TOKEN_SECRET; echo
export NS_CONSUMER_KEY NS_CONSUMER_SECRET NS_TOKEN_ID NS_TOKEN_SECRET
java -cp NQjc.jar NsDeletedCheck.java
status=$?
unset NS_CONSUMER_KEY NS_CONSUMER_SECRET NS_TOKEN_ID NS_TOKEN_SECRET
exit $status
