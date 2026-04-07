param()
$ErrorActionPreference = 'Continue'

Write-Host "Test 1: Register" -ForegroundColor Green
try {
    $result = Invoke-RestMethod -Uri "http://localhost:8081/api/user/register" -Method POST -ContentType "application/json" -Body '{"username":"testuser","password":"123456","email":"test@example.com"}'
    Write-Host "Success" -ForegroundColor Green
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nTest 2: Login" -ForegroundColor Green
try {
    $login = Invoke-RestMethod -Uri "http://localhost:8081/api/user/login" -Method POST -ContentType "application/json" -Body '{"username":"testuser","password":"123456"}'
    $token = $login.data
    Write-Host "Token: $token" -ForegroundColor Green
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nTest 3: Add Bill with Token" -ForegroundColor Green
try {
    $headers = @{"Authorization"="Bearer $token"}
    $result = Invoke-RestMethod -Uri "http://localhost:8081/api/bill" -Method POST -Headers $headers -ContentType "application/json" -Body '{"type":1,"amount":100,"category":"Food"}'
    Write-Host "Success" -ForegroundColor Green
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nTest 4: Query with Token" -ForegroundColor Green
try {
    $headers = @{"Authorization"="Bearer $token"}
    $result = Invoke-RestMethod -Uri "http://localhost:8081/api/bill/list" -Method GET -Headers $headers
    Write-Host "Success" -ForegroundColor Green
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nTest 5: Query without Token (expect 401)" -ForegroundColor Green
try {
    $result = Invoke-RestMethod -Uri "http://localhost:8081/api/bill/list" -Method GET
    Write-Host "FAIL: Should return 401" -ForegroundColor Red
} catch {
    $code = [int]$_.Exception.Response.StatusCode
    if ($code -eq 401) {
        Write-Host "PASS: Got 401 as expected" -ForegroundColor Green
    } else {
        Write-Host "FAIL: Got $code instead of 401" -ForegroundColor Red
    }
}

Write-Host "`nAll tests completed!" -ForegroundColor Green
