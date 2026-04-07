Write-Host "=== Test 1: Register ===" -ForegroundColor Green
$r = Invoke-RestMethod -Uri "http://localhost:8081/api/user/register" -Method POST -ContentType "application/json" -Body '{"username":"testuser","password":"123456","email":"test@example.com"}'
$r | ConvertTo-Json

Write-Host "`n=== Test 2: Login ===" -ForegroundColor Green
$l = Invoke-RestMethod -Uri "http://localhost:8081/api/user/login" -Method POST -ContentType "application/json" -Body '{"username":"testuser","password":"123456"}'
$l | ConvertTo-Json
$t = $l.data
Write-Host "Token: $t" -ForegroundColor Cyan

Write-Host "`n=== Test 3: Add Bill ===" -ForegroundColor Green
$h = @{Authorization="Bearer $t"}
$a = Invoke-RestMethod -Uri "http://localhost:8081/api/bill" -Method POST -Headers $h -ContentType "application/json" -Body '{"type":1,"amount":100,"category":"Food"}'
$a | ConvertTo-Json

Write-Host "`n=== Test 4: Query with Token ===" -ForegroundColor Green
$q = Invoke-RestMethod -Uri "http://localhost:8081/api/bill/list" -Method GET -Headers $h
$q | ConvertTo-Json

Write-Host "`n=== Test 5: Query without Token ===" -ForegroundColor Green
try {
    Invoke-RestMethod -Uri "http://localhost:8081/api/bill/list" -Method GET
    Write-Host "FAIL: Should return 401" -ForegroundColor Red
} catch {
    $code = [int]$_.Exception.Response.StatusCode
    Write-Host "PASS: Got $code (Unauthorized)" -ForegroundColor Green
}

Write-Host "`n=== All Tests Completed ===" -ForegroundColor Green
