#!/bin/sh
# Lance les tests de performance k6.
# Prérequis : l'application doit être démarrée (./gradlew bootRun) et accessible
# sur http://localhost:8080 (ou définir BASE_URL).
k6 run index.js -c config.json
