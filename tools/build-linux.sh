#!/usr/bin/env bash
# build-linux.sh
# Builds JAR, copies dependencies, and creates Linux installer

set -e

APP_NAME="DS-Tool"
APP_VERSION="1.0.0"
MAIN_JAR="data-structure-tool-1.0-SNAPSHOT.jar"
MAIN_CLASS="com.fernando.ds.GuiMain"
ICON_FILE="icon.png"

echo "==== Preparing dist folder ===="

mkdir -p dist

echo "==== Building project with Maven ===="

mvn clean package dependency:copy-dependencies

if [ ! -f "target/$MAIN_JAR" ]; then
    echo "ERROR: JAR not found: target/$MAIN_JAR"
    exit 1
fi

echo "==== Creating Linux installer with jpackage ===="

jpackage \
  --type deb \
  --name "$APP_NAME" \
  --app-version "$APP_VERSION" \
  --input target \
  --main-jar "$MAIN_JAR" \
  --main-class "$MAIN_CLASS" \
  --dest dist \
  --module-path target/dependency \
  --add-modules javafx.controls,javafx.web,javafx.swing \
  --icon "$ICON_FILE"

echo "==== Renaming installer ===="

installer=$(find dist -maxdepth 1 -type f \( -name "*.deb" -o -name "*.rpm" \) | head -n 1)

if [ -z "$installer" ]; then
    echo "ERROR: No Linux installer found in dist/"
    exit 1
fi

ext="${installer##*.}"
new_name="DS-Tool-Linux-v${APP_VERSION}.${ext}"

if [ -f "dist/$new_name" ]; then
    echo "WARNING: dist/$new_name already exists. Overwriting..."
    rm "dist/$new_name"
fi

mv "$installer" "dist/$new_name"

echo "==== Build complete ===="
echo "Installer created:"
echo "dist/$new_name"