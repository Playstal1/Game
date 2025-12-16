@echo off
chcp 1251
title Game
echo Starting Game...
java --module-path "javafx-sdk-25.0.1/lib" --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics -jar app.jar
pause