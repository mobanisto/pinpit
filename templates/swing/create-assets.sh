#!/bin/bash

set -e

pinpit create-image-assets-from-material-icon --input jpui.svg --output assets

mv assets/icon-192.png src/main/resources/icon.png

mkdir -p src/main/packaging/linux/
mv assets/icon-500.png src/main/packaging/linux/icon.png

mkdir -p src/main/packaging/windows/
mv assets/icon.ico src/main/packaging/windows/
mv assets/banner.bmp src/main/packaging/windows/
mv assets/dialog.bmp src/main/packaging/windows/

mkdir -p src/main/packaging/macos/
mv assets/icon.icns src/main/packaging/macos/

rm assets/icon.svg
rm assets/icon-macos.svg
rm assets/banner.svg
rm assets/dialog.svg

rmdir assets
