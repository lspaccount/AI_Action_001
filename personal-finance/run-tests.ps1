Write-Host "========================================" -ForegroundColor Cyan
Write-Host "JWT Authentication API Tests" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Test 1: Register
Write-Host "[Test 1] Register new user..." -ForegroundColor Yellow
try {
    $register = Invoke-RestMethod -Uri "http://localhost:8081/api/user/register" -Method POST -ContentType "application/json" -Body '{"username":"testuser","password":"123456","email":"test@example.com"}'
    Write-Host "PASS - Register: $($register.message)" -ForegroundColor Green
} catch {
    Write-Host "FAIL - Register: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 2: Login
Write-Host "[Test 2] Login to get token..." -ForegroundColor Yellow
try {
    $login = Invoke-RestMethod -Uri "http://localhost:8081/api/user/login" -Method POST -ContentType "application/json" -Body '{"username":"testuser","password":"123456"}'
    $token = $login.data
    Write-Host "PASS - Login success, Token: $token" -ForegroundColor Green
} catch {
    Write-Host "FAIL - Login: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}
Write-Host ""

# Test 3: Add bill with token
Write-Host "[Test 3] Add bill with token..." -ForegroundColor Yellow
try {
    $headers = @{"Authorization"="Bearer $token"}
    $addBill = Invoke-RestMethod -Uri "http://localhost:8081/api/bill" -Method POST -Headers $headers -ContentType "application/json" -Body '{"type":1,"amount":100.50,"category":"Food","remark":"Lunch"}'
    Write-Host "PASS - Add bill: $($addBill.message)" -ForegroundColor Green
} catch {
    Write-Host "FAIL - Add bill: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 4: Query bills with token
Write-Host "[Test 4] Query bills with token..." -ForegroundColor Yellow
try {
    $headers = @{"Authorization"="Bearer $token"}
    $list = Invoke-RestMethod -Uri "http://localhost:8081/api/bill/list" -Method GET -Headers $headers
    Write-Host "PASS - Query bills: $($list.code)" -ForegroundColor Green
} catch {
    Write-Host "FAIL - Query bills: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 5: Query without token (should return 401)
Write-Host "[Test 5] Query without token (expect 401)..." -ForegroundColor Yellow
try {
    $noAuth = Invoke-RestMethod -Uri "http://localhost:8081/api/bill/list" -Method GET
    Write-Host "FAIL - Should return 401 but got 200" -ForegroundColor Red
} catch {
    $statusCode = [int]$_.Exception.Response.StatusCode
    if ($statusCode -eq 401) {
        Write-Host "PASS - Got 401 as expected (Unauthorized access blocked)" -ForegroundColor Green
    } else {
        Write-Host "FAIL - Expected 401 but got $statusCode" -ForegroundColor Red
    }
}
Write-Host ""

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "All tests completed!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
