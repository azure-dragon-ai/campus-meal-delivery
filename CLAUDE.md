# CLAUDE.md

本文件为 Claude Code (claude.ai/code) 在此代码仓库工作时提供指导。

## 快速开始

### 数据库初始化
```sql
source database/schema.sql
```

### 后端 (多模块 Maven 项目)
```bash
cd server
mvn clean install
# 从 starter 模块运行
java -jar target/campus-meal-delivery.jar
# 或从 IDE 运行：com.campus.starter.CampusMealDeliveryApplication
```

### 前端 (3 个独立应用)
```bash
# 后台管理系统 (端口 3001)
cd web/admin && npm install && npm run dev

# 教师系统 (端口 3002)
cd web/teacher && npm install && npm run dev

# 家长系统 (端口 3003)
cd web/parent && npm install && npm run dev
```

## 架构概览

### 后端结构
```
server/
├── core/        # 通用工具类、JWT、异常处理、配置
├── database/    # 实体类、MyBatis Mapper、Service 基类
├── business/    # 业务逻辑按系统划分:
│   ├── admin/   # 后台管理接口
│   ├── teacher/ # 教师接口
│   └── parent/  # 家长接口
└── starter/     # Spring Boot 应用入口
```

### 前端结构
```
web/
├── admin/   # 后台管理界面 (Vue 3 + Element Plus)
├── teacher/ # 教师界面
└── parent/  # 家长界面
```

### 核心模式

- **认证方式**: JWT Token 通过拦截器 (`WebMvcConfig`)。所有 `/api/**` 接口需要认证，登录/注册接口除外
- **响应格式**: 所有接口返回 `Result<T>` 包装类，包含 code/msg/data
- **分页**: `PageResult<T>` 使用 page/size 参数，基于 MyBatis-Plus Page
- **密码**: BCrypt 加密通过 `PasswordUtil`
- **数据隔离**: 家长只能查看自己的子女，教师只能查看所在学校数据

### 数据库表

核心表：`sys_admin`, `biz_teacher`, `biz_parent`, `biz_diner`, `biz_school`, `biz_class`, `biz_leave`

功能表：`biz_semester` (学期), `biz_meal_registration` (配餐登记), `biz_recipe` (食谱)

### 常见操作

**新增实体**: 在 `database/entity` 创建实体 → `database/mapper` → `business/service` → `business/controller`

**新增前端功能**: 在 `src/views/` 创建视图 → 添加路由 → 添加菜单项 → 创建 API 调用

**接口路径规范**: `/{system}/{resource}` 例如 `/parent/diners`, `/teacher/leaves`
