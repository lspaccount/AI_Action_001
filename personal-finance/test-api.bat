@echo off
chcp 65001 >nul
echo ========================================
echo JWT Authentication API Tests
echo ========================================
echo.

echo [Test 1] Register new user...
curl -X POST http://localhost:8081/api/user/register ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"testuser\",\"password\":\"123456\",\"email\":\"test@example.com\"}"
echo.
echo.

echo [Test 2] Login to get token...
curl -X POST http://localhost:8081/api/user/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"testuser\",\"password\":\"123456\"}"
echo.
echo.

echo [Test 3] Add bill with token...
curl -X POST http://localhost:8081/api/bill ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer {YOUR_TOKEN_HERE}" ^
  -d "{\"type\":1,\"amount\":100.50,\"category\":\"Food\",\"remark\":\"Lunch\"}"
echo.
echo.

echo [Test 4] Query bills with token...
curl -X GET http://localhost:8081/api/bill/list ^
  -H "Authorization: Bearer {YOUR_TOKEN_HERE}"
echo.
echo.

echo [Test 5] Query without token (expect 401)...
curl -X GET http://localhost:8081/api/bill/list -w "\nHTTP Status: %{http_code}"
echo.
echo.

echo ========================================
echo All tests completed!
echo ========================================
pause
