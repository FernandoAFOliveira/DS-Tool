# build.ps1
# Builds JAR, copies dependencies, and creates installer

$ErrorActionPreference = "Stop"

Write-Host "==== Cleaning previous build ===="

if (Test-Path dist) {
    New-Item -ItemType Directory -Path dist | Out-Null
}
if (Test-Path "dist\$newName") {
    Write-Host "WARNING: $newName already exists. Overwriting..."
    Remove-Item "dist\$newName"
}

Write-Host "==== Building project (Maven) ===="
mvn clean package dependency:copy-dependencies

if (!(Test-Path "target\data-structure-tool-1.0-SNAPSHOT.jar")) {
    Write-Host "ERROR: JAR not found!"
    exit 1
}

Write-Host "==== Creating installer (jpackage) ===="

jpackage `
  --type exe `
  --name DS-Tool `
  $appVersion = (Select-String -Path pom.xml -Pattern "<version>(.+)</version>").Matches[0].Groups[1].Value`
  --app-version $appVersion `
  --input target `
  --main-jar data-structure-tool-1.0-SNAPSHOT.jar `
  --main-class com.fernando.ds.GuiMain `
  --dest dist `
  --module-path target/dependency `
  --add-modules javafx.controls,javafx.web,javafx.swing `
  --icon icon.ico `
  --win-dir-chooser `
  --win-menu `
  --win-shortcut

Write-Host "==== Build complete ===="
Write-Host "Installer is in /dist"
Write-Host  "Run the installer, then search Windows Start for DS-Tool."

# Detect generated installer
$installer = Get-ChildItem dist | Where-Object {
    $_.Extension -in ".exe", ".msi", ".deb", ".rpm", ".pkg", ".dmg"
} | Select-Object -First 1

if ($null -eq $installer) {
    Write-Host "ERROR: No installer found in dist/"
    exit 1
}

# Determine platform from extension
$platform = switch ($installer.Extension) {
    ".exe" { "Windows" }
    ".msi" { "Windows" }
    ".deb" { "Linux" }
    ".rpm" { "Linux" }
    ".pkg" { "Mac" }
    ".dmg" { "Mac" }
    default { "Unknown" }
}

# Extract version from filename
if ($installer.Name -match "(\d+\.\d+\.\d+)") {
    $version = $Matches[1]
} else {
    $version = "1.0.0"
}

# Build new name
$newName = "DS-Tool-$platform-v$version$($installer.Extension)"

Rename-Item `
  -Path $installer.FullName `
  -NewName $newName

Write-Host "Renamed installer to: $newName"