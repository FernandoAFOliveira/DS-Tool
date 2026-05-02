#!/usr/bin/env bash
# build-linux.sh
# Builds JAR, copies dependencies, and creates Linux installers:
# - .deb package for Debian/Ubuntu/Kubuntu
# - portable .tar.gz app image for other Linux distributions

set -e

APP_NAME="DS-Tool"
APP_VERSION="1.0.0"
MAIN_JAR="data-structure-tool-1.0-SNAPSHOT.jar"
MAIN_CLASS="com.fernando.ds.GuiMain"
ICON_FILE="icon.png"

DEB_NAME="DS-Tool-Linux-v${APP_VERSION}.deb"
PORTABLE_NAME="DS-Tool-Linux-Portable-v${APP_VERSION}.tar.gz"

echo "==== Preparing dist folder ===="

mkdir -p dist

echo "==== Building project with Maven ===="

mvn clean package dependency:copy-dependencies

if [ ! -f "target/$MAIN_JAR" ]; then
    echo "ERROR: JAR not found: target/$MAIN_JAR"
    exit 1
fi

echo "==== Creating Debian/Ubuntu installer (.deb) ===="

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

deb_file=$(find dist -maxdepth 1 -type f -name "*.deb" | head -n 1)

if [ -z "$deb_file" ]; then
    echo "ERROR: No .deb installer found in dist/"
    exit 1
fi

if [ -f "dist/$DEB_NAME" ]; then
    echo "WARNING: dist/$DEB_NAME already exists. Overwriting..."
    rm "dist/$DEB_NAME"
fi

mv "$deb_file" "dist/$DEB_NAME"

echo "==== Creating portable Linux app image ===="

portable_work_dir="dist/portable-work"

if [ -d "$portable_work_dir" ]; then
    rm -rf "$portable_work_dir"
fi

mkdir -p "$portable_work_dir"

jpackage \
  --type app-image \
  --name "$APP_NAME" \
  --app-version "$APP_VERSION" \
  --input target \
  --main-jar "$MAIN_JAR" \
  --main-class "$MAIN_CLASS" \
  --dest "$portable_work_dir" \
  --module-path target/dependency \
  --add-modules javafx.controls,javafx.web,javafx.swing \
  --icon "$ICON_FILE"

if [ -f "dist/$PORTABLE_NAME" ]; then
    echo "WARNING: dist/$PORTABLE_NAME already exists. Overwriting..."
    rm "dist/$PORTABLE_NAME"
fi

tar -czf "dist/$PORTABLE_NAME" -C "$portable_work_dir" "$APP_NAME"

rm -rf "$portable_work_dir"

echo "==== Build complete ===="
echo "Created:"
echo "dist/$DEB_NAME"
echo "dist/$PORTABLE_NAME"