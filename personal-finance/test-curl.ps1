# 测试API接口的PowerShell命令
Write-Host "=== 1. 注册用户 ==="
Invoke-WebRequest -Uri "http://localhost:8082/api/user/register" `
                 -Method POST `
                 -ContentType "application/json" `
                 -Body '{"username":"testuser","password":"123456","email":"test@example.com"}'

Write-Host "`n=== 2. 登录获取Token ==="
$response = Invoke-WebRequest -Uri "http://localhost:8082/api/user/login" `
                              -Method POST `
                              -ContentType "application/json" `
                              -Body '{"username":"testuser","password":"123456"}'
Write-Host $response.Content

# 提取Token
$token = ($response.Content | ConvertFrom-Json).data

Write-Host "`n=== 3. 用Token添加账单 ==="
Invoke-WebRequest -Uri "http://localhost:8082/api/bill" `
                 -Method POST `
                 -ContentType "application/json" `
                 -Headers @{"Authorization"="Bearer $token"} `
                 -Body '{"amount":100.0,"type":"expense","category":"Food","date":"2026-04-07","description":"Lunch"}'

Write-Host "`n=== 4. 用Token查询账单 ==="
Invoke-WebRequest -Uri "http://localhost:8082/api/bill" `
                 -Method GET `
                 -Headers @{"Authorization"="Bearer $token"}

Write-Host "`n=== 5. 不带Token访问，验证返回401 ==="
Invoke-WebRequest -Uri "http://localhost:8082/api/bill" `
                 -Method GET `
                 -ErrorAction SilentlyContinue

Write-Host "`n=== 测试完成后执行Git提交 ==="
Write-Host "git add ."
Write-Host "git commit -m 'feat: 添加JWT登录认证和拦截器'"