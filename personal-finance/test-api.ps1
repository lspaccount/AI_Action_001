# PowerShell 脚本 - 测试 JWT API
# 保存为: test-api.ps1

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "JWT Authentication API Tests" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Test 1: Register
Write-Host "[Test 1] Register new user..." -ForegroundColor Yellow
try {
    $register = Invoke-RestMethod -Uri "http://localhost:8081/api/user/register" -Method POST -ContentType "application/json" -Body '{"username":"testuser","password":"123456","email":"test@example.com"}'
    Write-Host "PASS - $($register.message)" -ForegroundColor Green
} catch {
    Write-Host "FAIL - $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 2: Login
Write-Host "[Test 2] Login to get token..." -ForegroundColor Yellow
try {
    $login = Invoke-RestMethod -Uri "http://localhost:8081/api/user/login" -Method POST -ContentType "application/json" -Body '{"username":"testuser","password":"123456"}'
    $token = $login.data
    Write-Host "PASS - Token: $token" -ForegroundColor Green
} catch {
    Write-Host "FAIL - $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}
Write-Host ""

# Test 3: Add bill with token
Write-Host "[Test 3] Add bill with token..." -ForegroundColor Yellow
try {
    $headers = @{"Authorization"="Bearer $token"}
    $addBill = Invoke-RestMethod -Uri "http://localhost:8081/api/bill" -Method POST -Headers $headers -ContentType "application/json" -Body '{"type":1,"amount":100.50,"category":"Food","remark":"Lunch"}'
    Write-Host "PASS - $($addBill.message)" -ForegroundColor Green
} catch {
    Write-Host "FAIL - $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 4: Query bills with token
Write-Host "[Test 4] Query bills with token..." -ForegroundColor Yellow
try {
    $headers = @{"Authorization"="Bearer $token"}
    $list = Invoke-RestMethod -Uri "http://localhost:8081/api/bill/list" -Method GET -Headers $headers
    Write-Host "PASS - Query success" -ForegroundColor Green
} catch {
    Write-Host "FAIL - $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 5: Query without token (expect 401)
Write-Host "[Test 5] Query without token (expect 401)..." -ForegroundColor Yellow
try {
    Invoke-RestMethod -Uri "http://localhost:8081/api/bill/list" -Method GET
    Write-Host "FAIL - Should return 401" -ForegroundColor Red
} catch {
    $statusCode = [int]$_.Exception.Response.StatusCode
    if ($statusCode -eq 401) {
        Write-Host "PASS - Got 401 (Unauthorized)" -ForegroundColor Green
    } else {
        Write-Host "FAIL - Expected 401, got $statusCode" -ForegroundColor Red
    }
}
Write-Host ""

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "All tests completed!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
