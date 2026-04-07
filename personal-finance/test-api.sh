#!/bin/bash

# 测试 API 脚本

BASE_URL="http://localhost:8080"

echo "=== 1. 注册用户 ==="
curl -X POST "$BASE_URL/api/user/register" \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"123456","email":"test@example.com"}'

echo -e "\n\n=== 2. 登录 ==="
curl -X POST "$BASE_URL/api/user/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"123456"}'

echo -e "\n\n=== 3. 添加账单（收入） ==="
curl -X POST "$BASE_URL/api/bill" \
  -H "Content-Type: application/json" \
  -d '{"type":1,"amount":100.50,"category":"餐饮","remark":"午餐"}'

echo -e "\n\n=== 4. 添加账单（支出） ==="
curl -X POST "$BASE_URL/api/bill" \
  -H "Content-Type: application/json" \
  -d '{"type":2,"amount":50.00,"category":"交通","remark":"打车"}'

echo -e "\n\n=== 5. 查询账单列表 ==="
curl -X GET "$BASE_URL/api/bill/list"

echo -e "\n\n=== 6. 按月统计 ==="
curl -X GET "$BASE_URL/api/bill/stats?yearMonth=2026-04"

echo -e "\n"