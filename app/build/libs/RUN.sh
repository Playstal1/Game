#!/bin/bash

# Game Startup Script
echo -ne "\033]0;Game\007"
echo "Starting Game..."

java --module-path "javafx-sdk-25.0.1/lib" \
     --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics \
     -jar app.jar

read -p "Press Enter to continue..."
