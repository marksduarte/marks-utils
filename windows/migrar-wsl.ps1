param ($distro, $destino)
Clear-Host
$ErrorActionPreference = 'Stop'
$PSDefaultParameterValues['*:Encoding'] = 'utf8'

# Commands
$checkDirExistsCmd = {
  Write-Host "Verificando diretório de destino..."
  Test-Path $dirPath
}
$createDirIfNotExistsCmd = {
  Write-Host "Criando diretório..." $dirPath -ForegroundColor Yellow
  New-item $dirPath -ItemType Directory
}
$exportCmd = {
  Write-Host "Exportando a distribuição..." -ForegroundColor Yellow
  wsl --export $distro $tempFile
}
$unregisterCmd = {
  Write-Host "Removendo registro da distribuição..."
  wsl --unregister $distro
}
$importCmd = {
  Write-Host "Re-Importando a distribuição..." -ForegroundColor Yellow
  wsl --import $distro $destino $tempFile
}
$changeUserCmd = {
  Write-Host "Restaurando usuário padrão..."
  cd HKCU:\
  Set-Location -path HKCU:\Software\Microsoft\Windows\CurrentVersion\Lxss\
  Get-childitem -recurse -ErrorAction SilentlyContinue | Get-ItemProperty | Where-Object { $_.DistributionName -like $distro } | Set-Itemproperty -Name DefaultUid -Value 1000
}
$removeFileTempCmd = {
  Write-Host "Removendo arquivo temporário..."
  Remove-Item $tempFile -Force
}

# Functions
function checkResult {
  param($results)
  if ($null -ne $results) {
    $results | ForEach-Object -Process {
      if ($_ -ne "" -and $_ -like 'Error*') {
        Write-Host $_ -ForegroundColor Red
        Write-Host "Encerrando script..."
        exit
      }
    }
  }
}

function checkIfDirExists {
  param ($dirPath)
  $results = Invoke-Command -ScriptBlock $checkDirExistsCmd
  if ($results -ne $true -or $results -ne "True") {
    $results = Invoke-Command -ScriptBlock $createDirIfNotExistsCmd
  }
}

Write-Host "Iniciando a migração..."
if ($null -eq $distro) {
  $distro = read-host -Prompt "Favor informar o nome da Distribuição."
}
if ($null -eq $destino) {
  $destino = read-host -Prompt "Favor informar o caminho do diretório de destino da Distribuição."
}
if ($distro -eq "" -Or $destino -eq "") {
  Write-Host "Parâmetros obrigatórios não informados... Encerrando script."
  return
}

Write-Host "Distribuição: $distro e Local de Destino: $destino"  
$tempFile = "C:\ProgramData\Temp\$distro.tar"

checkIfDirExists $destino
checkResult $result
$result = Invoke-Command -ScriptBlock $exportCmd
checkResult $result
$result = Invoke-Command -ScriptBlock $unregisterCmd
checkResult $result
$result = Invoke-Command -ScriptBlock $importCmd
checkResult $result
$result = Invoke-Command -ScriptBlock $removeFileTempCmd
checkResult $result

$isChangeUser = read-host -Prompt "Deseja restaurar o usuário da distribuição? S (Sim) ou N (Não)"
if ($isChangeUser -eq "S" -or $isChangeUser -eq "s") {
  $currentLocation = Get-Location
  Invoke-Command -ScriptBlock $changeUserCmd
  Set-Location -path $currentLocation
}
Write-Host "Migração finalizada." -ForegroundColor Green