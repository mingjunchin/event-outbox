#!/bin/zsh
curl -X PUT localhost:8083/connectors/EventOutbox/config -H "Content-Type: application/json" -d @./eventoutbox-connector-prop.json