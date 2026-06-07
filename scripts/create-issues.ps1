param(
  [string]$Repo = "mohammed-fernine/spring-bdd-jpa",
  [string]$DefaultAssignee = "@me",
  [string]$AssigneeMapPath = "scripts/assignees.json",
  [string[]]$Labels = @("scenario","bdd"),
  [string]$FeaturesPath = "src/test/resources/features",
  [switch]$DryRun,
  [switch]$Unassigned
)

function Require-GhCli {
  if (-not (Get-Command gh -ErrorAction SilentlyContinue)) {
    Write-Error "GitHub CLI (gh) is not installed. Install from https://cli.github.com and run 'gh auth login'."
    exit 1
  }
}

function New-Issue {
  param(
    [string]$Repo,
    [string]$Title,
    [string]$Body,
    [string[]]$Labels,
    [string]$Assignee
  )
  $labelArgs = @()
  foreach ($l in $Labels) { $labelArgs += @('--label', $l) }
  $args = @('issue','create','--repo', $Repo, '--title', $Title, '--body', $Body) + $labelArgs
  if (-not [string]::IsNullOrWhiteSpace($Assignee)) { $args += @('--assignee', $Assignee) }
  if ($DryRun) { Write-Host ("[DRY RUN] gh " + ($args -join ' ')) }
  else { gh @args | Write-Host }
}

function Get-FeatureFiles {
  param([string]$Path)
  if (-not (Test-Path $Path)) { Write-Error "Features path not found: $Path"; exit 1 }
  Get-ChildItem -Path $Path -Filter *.feature -Recurse
}

function Slugify([string]$text) {
  $s = $text.ToLower()
  $s = ($s -replace "[^a-z0-9]+","-").Trim('-')
  return $s
}

function Normalize([string]$text) {
  if ($null -eq $text) { return "" }
  return ($text.ToLower().Trim())
}

function Load-AssigneeMap {
  param([string]$Path)
  $map = @{}
  if ([string]::IsNullOrWhiteSpace($Path)) { return $map }
  if (-not (Test-Path -LiteralPath $Path)) { return $map }
  try {
    $json = Get-Content -LiteralPath $Path -Raw | ConvertFrom-Json -AsHashtable
    foreach ($k in $json.Keys) {
      $map[(Normalize $k)] = [string]$json[$k]
    }
  } catch {
    Write-Warning "Failed to parse assignee map at $Path: $_"
  }
  return $map
}

function Parse-FeatureFile {
  param([string]$File)
  $lines = Get-Content -LiteralPath $File
  $featureName = ($lines | Where-Object { $_ -match '^\s*Feature:\s*(.+)$' } | Select-Object -First 1) -replace '^\s*Feature:\s*',''
  $background = @()
  $scenarios = @()
  $i = 0
  $inBackground = $false
  while ($i -lt $lines.Count) {
    $line = $lines[$i]
    if ($line -match '^\s*Background:') {
      $inBackground = $true
      $i++
      while ($i -lt $lines.Count -and ($lines[$i] -match '^(\s*(Given|When|Then|And)\b|\s*$)')) {
        if ($lines[$i].Trim()) { $background += $lines[$i].Trim() }
        $i++
      }
      continue
    }
    if ($line -match '^\s*Scenario:\s*(.+)$') {
      $scenarioName = $Matches[1].Trim()
      $steps = @()
      $i++
      while ($i -lt $lines.Count -and -not ($lines[$i] -match '^\s*Scenario:\s*')) {
        if ($lines[$i] -match '^(\s*(Given|When|Then|And)\b)') { $steps += $lines[$i].Trim() }
        elseif ($lines[$i].Trim().Length -eq 0 -and $steps.Count -gt 0) { break }
        $i++
      }
      $scenarios += [pscustomobject]@{ Name=$scenarioName; Steps=$steps }
      continue
    }
    $i++
  }
  [pscustomobject]@{ Feature=$featureName; Background=$background; Scenarios=$scenarios; File=$File }
}

# Main
Require-GhCli
$files = Get-FeatureFiles -Path $FeaturesPath
$all = @()
foreach ($f in $files) { $all += (Parse-FeatureFile -File $f.FullName) }

$assigneeMap = Load-AssigneeMap -Path $AssigneeMapPath
if ($assigneeMap.Count -gt 0) {
  Write-Host "Loaded assignee map from $AssigneeMapPath:" -ForegroundColor Cyan
  $assigneeMap.GetEnumerator() | ForEach-Object { Write-Host "  '" $_.Key "' -> '" $_.Value "'" }
} else {
  Write-Warning "No assignee map found. All issues will be assigned to $DefaultAssignee"
}

$created = 0
foreach ($feat in $all) {
  $featureLabel = "feature:" + (Slugify $feat.Feature)
  $labels = $Labels + $featureLabel
  $featureKey = Normalize $feat.Feature
  $assigneeForFeature = $DefaultAssignee
  if ($assigneeMap.ContainsKey($featureKey)) { $assigneeForFeature = $assigneeMap[$featureKey] }
  if ($Unassigned) { $assigneeForFeature = $null }
  foreach ($sc in $feat.Scenarios) {
    $title = "${($feat.Feature)} — ${($sc.Name)}"
    $body = @()
    $body += "### Feature"
    $body += $feat.Feature
    if ($feat.Background.Count -gt 0) {
      $body += "\n### Background"
      $body += ("- " + ($feat.Background -join "`n- "))
    }
    $body += "\n### Scenario"
    $body += $sc.Name
    if ($sc.Steps.Count -gt 0) {
      $body += "\n### Steps"
      $body += ("- " + ($sc.Steps -join "`n- "))
    }
    $body += "\n\n---\nGenerated from: ``$($feat.File)``"
    $bodyText = ($body -join "`n")
    New-Issue -Repo $Repo -Title $title -Body $bodyText -Labels $labels -Assignee $assigneeForFeature
    $created++
  }
}
Write-Host "Done. Prepared $created issues."
