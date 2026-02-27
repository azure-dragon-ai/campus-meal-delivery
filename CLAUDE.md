# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Quick Start

### Database Setup
```sql
source database/schema.sql
```

### Backend (Multi-module Maven)
```bash
cd server
mvn clean install
# Run from starter module
java -jar target/campus-meal-delivery.jar
# Or run from IDE: com.campus.starter.CampusMealDeliveryApplication
```

### Frontend (3 separate apps)
```bash
# Admin system (port 3001)
cd web/admin && npm install && npm run dev

# Teacher system (port 3002)
cd web/teacher && npm install && npm run dev

# Parent system (port 3003)
cd web/parent && npm install && npm run dev
```

## Architecture Overview

### Backend Structure
```
server/
├── core/        # Common utilities, JWT, exceptions, config
├── database/    # Entities, MyBatis mappers, service base classes
├── business/    # Business logic organized by system:
│   ├── admin/   # Admin management endpoints
│   ├── teacher/ # Teacher endpoints
│   └── parent/  # Parent endpoints
└── starter/     # Spring Boot application entry point
```

### Frontend Structure
```
web/
├── admin/   # Admin management UI (Vue 3 + Element Plus)
├── teacher/ # Teacher UI
└── parent/  # Parent UI
```

### Key Patterns

- **Authentication**: JWT token via interceptor (`WebMvcConfig`). All `/api/**` endpoints require auth except login/register
- **Response Format**: All APIs return `Result<T>` wrapper with code/msg/data
- **Pagination**: `PageResult<T>` with page/size params, uses MyBatis-Plus Page
- **Password**: BCrypt encoding via `PasswordUtil`
- **Data Isolation**: Parents see only their children, teachers see only their school

### Database Tables

Core tables: `sys_admin`, `biz_teacher`, `biz_parent`, `biz_diner`, `biz_school`, `biz_class`, `biz_leave`

Feature tables: `biz_semester` (学期), `biz_meal_registration` (配餐登记), `biz_recipe` (食谱)

### Common Operations

**Add new entity**: Create in `database/entity` → `database/mapper` → `business/service` → `business/controller`

**Add frontend feature**: Create view in `src/views/` → add route → add menu item → create API calls

**API endpoint convention**: `/{system}/{resource}` e.g., `/parent/diners`, `/teacher/leaves`
