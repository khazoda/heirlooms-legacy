#!/usr/bin/env pwsh

param(
    [string]$ModName,
    [string]$ModId,
    [string]$Author,
    [string]$PackageName,
    [string]$ProjectName,
    [switch]$KeepFolderName,
    [switch]$RenameFolder
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$utf8 = New-Object System.Text.UTF8Encoding($false)
$scriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectDir = Get-Item $scriptRoot
$launchDir = (Get-Location).Path
$editableRoots = @(
    'common/src/main/java'
    'common/src/main/resources'
    'fabric/src/main/java'
    'fabric/src/main/resources'
    'neoforge/src/main/java'
    'neoforge/src/main/resources'
) | ForEach-Object { Join-Path $scriptRoot $_ } | Where-Object { Test-Path $_ }

function Read-PropertyValue {
    param([string]$Path, [string]$Key)

    $match = Select-String -Path $Path -Pattern "^$([regex]::Escape($Key))=" | Select-Object -First 1
    if ($null -eq $match) {
        return ''
    }

    return ($match.Line -replace '^[^=]+=','')
}

function Read-RequiredValue {
    param([string]$Prompt, [string]$DefaultValue = '')

    $suffix = if ([string]::IsNullOrWhiteSpace($DefaultValue)) { '' } else { " [$DefaultValue]" }
    $value = Read-Host "$Prompt$suffix"
    if ([string]::IsNullOrWhiteSpace($value)) {
        return $DefaultValue
    }

    return $value.Trim()
}

function Read-YesNo {
    param([string]$Prompt, [bool]$DefaultYes = $true)

    $hint = if ($DefaultYes) { '[Y/n]' } else { '[y/N]' }
    $value = (Read-Host "$Prompt $hint").Trim().ToLowerInvariant()

    if ([string]::IsNullOrWhiteSpace($value)) {
        return $DefaultYes
    }

    return $value -in @('y', 'yes')
}

function Write-Utf8File {
    param([string]$Path, [string]$Content)

    [System.IO.File]::WriteAllText($Path, $Content, $utf8)
}

function Set-LineValue {
    param([string]$Path, [string]$Pattern, [string]$ReplacementLine)

    $lines = [System.Collections.Generic.List[string]]::new()
    foreach ($currentLine in [System.IO.File]::ReadAllLines($Path)) {
        $lines.Add($currentLine)
    }

    $updated = $false

    for ($i = 0; $i -lt $lines.Count; $i++) {
        if ($lines[$i] -match $Pattern) {
            $lines[$i] = $ReplacementLine
            $updated = $true
            break
        }
    }

    if (-not $updated) {
        $lines.Add($ReplacementLine)
    }

    Write-Utf8File -Path $Path -Content (($lines -join [Environment]::NewLine) + [Environment]::NewLine)
}

function Update-PropertyValue {
    param([string]$Path, [string]$Key, [string]$Value)

    $escaped = ($Value -replace "`r`n?", "`n").Replace("`n", '\n')
    Set-LineValue -Path $Path -Pattern "^$([regex]::Escape($Key))=" -ReplacementLine "$Key=$escaped"
}

function Update-RootProjectName {
    param([string]$Path, [string]$Value)

    $escaped = $Value.Replace("'", "\'")
    Set-LineValue -Path $Path -Pattern '^rootProject\.name\s*=' -ReplacementLine "rootProject.name = '$escaped'"
}

function Get-CurrentPackageName {
    $javaFiles = Get-ChildItem -Path (Join-Path $scriptRoot 'common/src/main/java') -Recurse -File -Filter '*.java'
    $seedFile = $javaFiles | Where-Object { $_.Name -eq 'Constants.java' } | Select-Object -First 1
    if ($null -eq $seedFile) {
        $seedFile = $javaFiles | Select-Object -First 1
    }
    if ($null -eq $seedFile) {
        throw 'Could not find a Java source file to detect the current package.'
    }

    $text = [System.IO.File]::ReadAllText($seedFile.FullName)
    $match = [regex]::Match($text, '(?m)^package\s+([^;]+);$')
    if (-not $match.Success) {
        throw "Could not detect the package declaration in $($seedFile.FullName)."
    }

    return $match.Groups[1].Value
}

function Get-TextFiles {
    $textExtensions = @('.gradle', '.properties', '.java', '.json', '.toml', '.mcmeta', '.md', '.txt', '.cfg', '.accesswidener')

    Get-ChildItem -Path $editableRoots -Recurse -File | Where-Object {
        $path = $_.FullName
        $textExtensions -contains $_.Extension.ToLowerInvariant() -or
        $path -match '[\\/]+META-INF[\\/]+services[\\/]+'
    }
}

function Replace-InFiles {
    param([object[]]$Replacements)

    foreach ($file in Get-TextFiles) {
        $path = $file.FullName
        $content = [System.IO.File]::ReadAllText($path)
        $updated = $content

        foreach ($entry in $Replacements) {
            if ($entry.From -and $entry.From -ne $entry.To) {
                $updated = $updated.Replace([string]$entry.From, [string]$entry.To)
            }
        }

        if ($updated -ne $content) {
            Write-Utf8File -Path $path -Content $updated
        }
    }
}

function Remove-EmptyParents {
    param([string]$StartPath, [string]$StopPath)

    $current = Split-Path -Parent $StartPath
    $stop = (Get-Item $StopPath).FullName

    while (-not [string]::IsNullOrWhiteSpace($current)) {
        $resolved = (Get-Item $current).FullName
        if ($resolved -eq $stop) {
            break
        }

        if ((Get-ChildItem -Path $resolved -Force | Measure-Object).Count -gt 0) {
            break
        }

        Remove-Item $resolved -Force
        $current = Split-Path -Parent $resolved
    }
}

function Move-PackageTree {
    param([string]$ModuleName, [string]$FromPackage, [string]$ToPackage)

    if ($FromPackage -eq $ToPackage) {
        return
    }

    $sourceRoot = Join-Path $scriptRoot "$ModuleName/src/main/java"
    $sourcePath = Join-Path $sourceRoot ($FromPackage -replace '\.', '/')
    $targetPath = Join-Path $sourceRoot ($ToPackage -replace '\.', '/')

    if (-not (Test-Path $sourcePath)) {
        return
    }

    $targetParent = Split-Path -Parent $targetPath
    if (-not (Test-Path $targetParent)) {
        New-Item -ItemType Directory -Path $targetParent -Force | Out-Null
    }

    Move-Item -Path $sourcePath -Destination $targetPath -Force
    Remove-EmptyParents -StartPath $sourcePath -StopPath $sourceRoot
}

function Rename-ResourceFiles {
    param([string]$CurrentModId, [string]$TargetModId)

    if ($CurrentModId -eq $TargetModId) {
        return
    }

    Get-ChildItem -Path $scriptRoot -Recurse -File | Where-Object {
        $_.FullName -match '[\\/]+src[\\/]+main[\\/]+resources[\\/]+'
    } | Sort-Object FullName -Descending | ForEach-Object {
        if ($_.Name.Contains($CurrentModId)) {
            $newName = $_.Name.Replace($CurrentModId, $TargetModId)
            Rename-Item -Path $_.FullName -NewName $newName
        }
    }
}

function Rename-ServiceFiles {
    param([string]$CurrentPackageName, [string]$TargetPackageName)

    if ($CurrentPackageName -eq $TargetPackageName) {
        return
    }

    Get-ChildItem -Path $scriptRoot -Recurse -File | Where-Object {
        $_.FullName -match '[\\/]+META-INF[\\/]+services[\\/]+'
    } | ForEach-Object {
        if ($_.Name.Contains($CurrentPackageName)) {
            $newName = $_.Name.Replace($CurrentPackageName, $TargetPackageName)
            Rename-Item -Path $_.FullName -NewName $newName
        }
    }
}

function Test-IsSameOrChildPath {
    param([string]$Path, [string]$RootPath)

    $fullPath = [System.IO.Path]::GetFullPath($Path).TrimEnd('\', '/')
    $fullRootPath = [System.IO.Path]::GetFullPath($RootPath).TrimEnd('\', '/')

    return $fullPath.Equals($fullRootPath, [System.StringComparison]::OrdinalIgnoreCase) -or
        $fullPath.StartsWith($fullRootPath + '\', [System.StringComparison]::OrdinalIgnoreCase) -or
        $fullPath.StartsWith($fullRootPath + '/', [System.StringComparison]::OrdinalIgnoreCase)
}

$gradlePropertiesPath = Join-Path $scriptRoot 'gradle.properties'
$settingsPath = Join-Path $scriptRoot 'settings.gradle'

$currentModName = Read-PropertyValue -Path $gradlePropertiesPath -Key 'mod_name'
$currentModId = Read-PropertyValue -Path $gradlePropertiesPath -Key 'mod_id'
$currentAuthor = Read-PropertyValue -Path $gradlePropertiesPath -Key 'mod_author'
$currentPackageName = Get-CurrentPackageName
$currentFolderName = $projectDir.Name

if ([string]::IsNullOrWhiteSpace($ModName)) {
    $ModName = Read-RequiredValue -Prompt 'Mod name' -DefaultValue $currentModName
}

if ([string]::IsNullOrWhiteSpace($ModId)) {
    $ModId = Read-RequiredValue -Prompt 'Mod id' -DefaultValue $currentModId
}

if ([string]::IsNullOrWhiteSpace($Author)) {
    $Author = Read-RequiredValue -Prompt 'Author' -DefaultValue $currentAuthor
}

if ([string]::IsNullOrWhiteSpace($PackageName)) {
    $PackageName = Read-RequiredValue -Prompt 'Package structure' -DefaultValue $currentPackageName
}

if ($KeepFolderName -and $RenameFolder) {
    throw 'Choose either -KeepFolderName or -RenameFolder, not both.'
}

if ([string]::IsNullOrWhiteSpace($ProjectName)) {
    if ($KeepFolderName) {
        $ProjectName = $currentFolderName
    }
    elseif ($RenameFolder) {
        $ProjectName = Read-RequiredValue -Prompt 'New folder / Gradle project name' -DefaultValue $ModId
    }
    else {
        $keepCurrent = Read-YesNo -Prompt "Keep the current folder name '$currentFolderName'?"
        if ($keepCurrent) {
            $ProjectName = $currentFolderName
            $KeepFolderName = $true
        }
        else {
            $RenameFolder = $true
            $ProjectName = Read-RequiredValue -Prompt 'New folder / Gradle project name' -DefaultValue $ModId
        }
    }
}
elseif (-not $KeepFolderName -and -not $RenameFolder) {
    $RenameFolder = $ProjectName -ne $currentFolderName
    $KeepFolderName = -not $RenameFolder
}

if ($ModId -notmatch '^[a-z][a-z0-9_-]*$') {
    throw 'Mod id must start with a lowercase letter and only contain lowercase letters, numbers, underscores, or dashes.'
}

if ($PackageName -notmatch '^[A-Za-z_][A-Za-z0-9_]*(\.[A-Za-z_][A-Za-z0-9_]*)*$') {
    throw 'Package structure must be a valid Java package name.'
}

$projectNameToUse = $ProjectName.Trim()
$targetPackageName = $PackageName.Trim()

if ([string]::IsNullOrWhiteSpace($projectNameToUse)) {
    throw 'Project name must not be blank.'
}

Replace-InFiles -Replacements @(
    [pscustomobject]@{ From = $currentPackageName; To = $targetPackageName }
    [pscustomobject]@{ From = "`"$currentModName`""; To = "`"$ModName`"" }
    [pscustomobject]@{ From = "'$currentModName'"; To = "'$ModName'" }
    [pscustomobject]@{ From = $currentModId; To = $ModId }
)

Update-PropertyValue -Path $gradlePropertiesPath -Key 'mod_name' -Value $ModName
Update-PropertyValue -Path $gradlePropertiesPath -Key 'mod_id' -Value $ModId
Update-PropertyValue -Path $gradlePropertiesPath -Key 'mod_author' -Value $Author
Update-PropertyValue -Path $gradlePropertiesPath -Key 'group' -Value $targetPackageName
Update-RootProjectName -Path $settingsPath -Value $projectNameToUse

Rename-ServiceFiles -CurrentPackageName $currentPackageName -TargetPackageName $targetPackageName
Rename-ResourceFiles -CurrentModId $currentModId -TargetModId $ModId

foreach ($module in @('common', 'fabric', 'neoforge')) {
    Move-PackageTree -ModuleName $module -FromPackage $currentPackageName -ToPackage $targetPackageName
}

$finalProjectDir = $projectDir.FullName
$renameInstruction = ''
if ($RenameFolder -and $projectNameToUse -ne $currentFolderName) {
    $parentDir = Split-Path -Parent $projectDir.FullName
    $targetProjectDir = Join-Path $parentDir $projectNameToUse

    if (Test-IsSameOrChildPath -Path $launchDir -RootPath $projectDir.FullName) {
        $renameInstruction = "Rename the project folder after the script finishes: '$currentFolderName' -> '$projectNameToUse'"
    }
    else {
        try {
            Rename-Item -LiteralPath $projectDir.FullName -NewName $projectNameToUse
            $finalProjectDir = $targetProjectDir
        } catch [System.IO.IOException] {
            $renameInstruction = "Could not rename the project folder automatically. Rename it manually: '$currentFolderName' -> '$projectNameToUse'"
        }
    }
}

Write-Host ''
Write-Host "Bootstrap complete."
Write-Host "Mod name: $ModName"
Write-Host "Mod id: $ModId"
Write-Host "Author: $Author"
Write-Host "Package: $targetPackageName"
Write-Host "Project folder: $finalProjectDir"
if ($renameInstruction) {
    Write-Host $renameInstruction
}
