# generate-diagrams.ps1
# Run from the project root: .\tools\generate-diagrams.ps1

$ErrorActionPreference = "Stop"

$templateDir = "src/main/resources/diagrams/templates"
$themeDir = "tools/diagram-themes"
$tempDir = "target/generated-diagram-mmd"

New-Item -ItemType Directory -Force -Path $tempDir | Out-Null

$themes = Get-ChildItem $themeDir -Filter "*.json"
$templates = Get-ChildItem $templateDir -Filter "*.mmd.template"

foreach ($themeFile in $themes) {
    $themeName = [System.IO.Path]::GetFileNameWithoutExtension($themeFile.Name)
    Write-Host "`nGenerating theme: $themeName"
    $outDir = "src/main/resources/diagrams/$themeName"
    New-Item -ItemType Directory -Force -Path $outDir | Out-Null

    $theme = Get-Content $themeFile.FullName -Raw | ConvertFrom-Json

    foreach ($templateFile in $templates) {
        $diagramName = $templateFile.BaseName -replace '\.mmd$', ''
        $text = Get-Content $templateFile.FullName -Raw

    foreach ($property in $theme.PSObject.Properties) {
        $placeholder = "{{" + $property.Name + "}}"
        $text = $text.Replace($placeholder, $property.Value)
    }

    $generatedMmd = Join-Path $tempDir "$diagramName`_$themeName.mmd"
    $outputSvg = Join-Path $outDir "$diagramName`_$themeName.svg"

    Set-Content -Path $generatedMmd -Value $text -Encoding UTF8

    mmdc -i $generatedMmd -o $outputSvg

    $svgText = Get-Content $outputSvg -Raw

    $svgText = $svgText -replace '<filter[\s\S]*?</filter>', ''
    $svgText = $svgText -replace '\sfilter="url\([^"]+\)"', ''

    $svgText = $svgText -replace 'stroke:url\([^)]+\)', "stroke:$($theme.primaryBorderColor)"
    $svgText = $svgText -replace 'fill:url\([^)]+\)', "fill:$($theme.primaryColor)"
    $svgText = $svgText -replace 'stroke:hsl\([^)]+\)', "stroke:$($theme.primaryBorderColor)"
    $svgText = $svgText -replace 'filter:drop-shadow\([^;]+\);?', ''
    $svgText = $svgText -replace 'display:inline-block;', ''
    $svgText = $svgText -replace 'stroke:revert;', "stroke:$($theme.primaryBorderColor);"
    $svgText = $svgText -replace 'stroke-width:revert;', 'stroke-width:1px;'

    # Remove invalid empty rects Mermaid/Batik may dislike.
    $svgText = $svgText -replace '<rect\s*/>', ''

    # Add real background rectangle after opening svg tag.
    $svgText = $svgText -replace '(<svg\b[^>]*>)', "`$1`n<rect width=`"100%`" height=`"100%`" fill=`"$($theme.background)`"/>"

    Set-Content -Path $outputSvg -Value $svgText -Encoding UTF8

        Write-Host "Generated $outputSvg"
    }
}
