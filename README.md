# Personal Finance - 个人记账本

## 项目介绍

Personal Finance 是一个基于 Spring Boot 3 开发的个人记账本 API，支持 JWT 认证，提供账单管理和收支统计功能。

## 技术栈

- Spring Boot 3.2
- MyBatis-Plus
- H2 内存数据库
- JWT 认证
- Swagger 在线文档

## 快速启动

1. 克隆项目
   ```bash
   git clone https://github.com/your-username/personal-finance.git
   cd personal-finance
   ```

2. 启动应用
   ```bash
   mvn spring-boot:run
   ```

3. 访问 Swagger 文档
   ```
   http://localhost:8082/swagger-ui.html
   ```

## 接口说明

### 用户接口

- **注册**：POST /api/user/register
- **登录**：POST /api/user/login
- **获取用户信息**：GET /api/user/info
- **退出登录**：POST /api/user/logout

### 账单接口

- **添加账单**：POST /api/bill
- **获取账单列表**：GET /api/bill/list
- **获取月度统计**：GET /api/bill/stats
- **账单分页查询**：GET /api/bill/page

## 项目截图

[项目截图占位符]  
[Swagger文档截图]  
[接口测试截图]
