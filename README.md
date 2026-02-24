# 校园配餐系统

## 项目架构

```
campus-meal-delivery/
├── server/                 # 后端 Spring Boot 项目
│   ├── core/              # 核心模块
│   │   ├── common/        # 通用类
│   │   ├── config/        # 配置类
│   │   ├── constant/      # 常量定义
│   │   ├── exception/     # 异常处理
│   │   └── util/          # 工具类
│   ├── database/          # 数据库模块
│   │   ├── config/        # 数据库配置
│   │   ├── entity/        # 实体类
│   │   ├── mapper/        # MyBatis Mapper
│   │   └── service/       # 数据库服务层
│   ├── business/          # 业务模块
│   │   ├── admin/         # 后台管理系统
│   │   ├── teacher/       # 教师系统
│   │   └── parent/        # 家长系统
│   └── starter/           # 启动类
├── web/                   # 前端 Vue 项目
│   ├── admin/             # 后台管理系统
│   ├── teacher/           # 教师系统
│   └── parent/            # 家长系统
└── README.md
```

## 技术栈

### 后端
- Spring Boot 2.7.x
- MyBatis-Plus
- MySQL 8.0
- JWT (jjwt)
- Lombok
- Hutool

### 前端
- Vue 3
- Vue Router
- Vuex/Pinia
- Axios
- Element Plus

## 功能模块

### 后台管理系统
- 管理员管理
- 角色管理
- 菜单管理
- 权限管理
- 学校管理
- 教师管理
- 家长管理
- 就餐人管理
- 请假管理
- 数据表管理（增删改查、导出）

### 教师系统
- 登录/注册（手机号 + 密码）
- 查询所在班级就餐人
- 查询就餐人请假信息
- 导出就餐人信息
- 导出请假信息

### 家长系统
- 登录/注册（手机号 + 密码）
- 新建就餐人
- 更新就餐人信息
- 请假管理

## 快速开始

### 数据库初始化
```sql
source database/schema.sql
```

### 后端启动
```bash
cd server
mvn clean install
java -jar target/campus-meal-delivery.jar
```

### 前端启动
```bash
cd web/admin && npm install && npm run dev
cd web/teacher && npm install && npm run dev
cd web/parent && npm install && npm run dev
```

## 认证方式

所有接口均使用 JWT Token 进行身份验证，Token 在请求头中传递：
```
Authorization: Bearer <token>
```

## 默认账号

### 后台管理
- 用户名：admin
- 密码：admin123

### 教师系统
- 需要注册后才能登录

### 家长系统
- 需要注册后才能登录

## API 接口列表

### 后台管理接口
| 接口 | 方法 | 描述 |
|------|------|------|
| /api/admin/login | POST | 管理员登录 |
| /api/admin/logout | POST | 管理员登出 |
| /api/admin/list | GET | 获取管理员列表 |
| /api/admin | POST | 创建管理员 |
| /api/admin/{id} | PUT | 更新管理员 |
| /api/admin/{id} | DELETE | 删除管理员 |
| /api/admin/{id}/password | PUT | 重置密码 |

### 教师系统接口
| 接口 | 方法 | 描述 |
|------|------|------|
| /api/teacher/login | POST | 教师登录 |
| /api/teacher/register | POST | 教师注册 |
| /api/teacher/info | GET | 获取教师信息 |
| /api/teacher/diners | GET | 获取班级就餐人 |
| /api/teacher/leaves | GET | 获取请假列表 |
| /api/teacher/leaves/{id}/audit | POST | 审核请假 |

### 家长系统接口
| 接口 | 方法 | 描述 |
|------|------|------|
| /api/parent/login | POST | 家长登录 |
| /api/parent/register | POST | 家长注册 |
| /api/parent/diners | GET | 获取就餐人列表 |
| /api/parent/diners | POST | 创建就餐人 |
| /api/parent/diners/{id} | PUT | 更新就餐人 |
| /api/parent/diners/{id} | DELETE | 删除就餐人 |
| /api/parent/leaves | GET | 获取请假列表 |
| /api/parent/leaves | POST | 创建请假 |
| /api/parent/leaves/{id}/cancel | POST | 取消请假 |

## 注意事项

1. 确保 MySQL 数据库已启动并创建了相应的数据库
2. 修改 application.yml 中的数据库连接配置
3. 后端服务默认端口为 8080
4. 前端三个系统分别运行在不同的端口：
   - 后台管理：3001
   - 教师系统：3002
   - 家长系统：3003
