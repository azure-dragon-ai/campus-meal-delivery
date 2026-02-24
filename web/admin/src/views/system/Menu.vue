<template>
  <div class="menu-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>菜单管理</span>
          <el-button type="primary" @click="handleAdd">新增菜单</el-button>
        </div>
      </template>

      <el-table :data="tableData" border row-key="id" default-expand-all>
        <el-table-column prop="name" label="菜单名称" width="200" />
        <el-table-column prop="path" label="路由路径" />
        <el-table-column prop="component" label="组件路径" />
        <el-table-column label="类型" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.type === 1">目录</el-tag>
            <el-tag v-else-if="row.type === 2" type="success">菜单</el-tag>
            <el-tag v-else type="warning">按钮</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="permission" label="权限标识" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const tableData = ref([
  {
    id: 1,
    parent_id: 0,
    name: '系统管理',
    path: '/system',
    component: 'Layout',
    icon: 'setting',
    type: 1,
    permission: null,
    sort: 100,
    children: [
      { id: 2, parent_id: 1, name: '管理员管理', path: '/system/admin', component: 'system/admin', icon: 'user', type: 2, permission: 'system:admin:list', sort: 1 },
      { id: 3, parent_id: 1, name: '角色管理', path: '/system/role', component: 'system/role', icon: 'peoples', type: 2, permission: 'system:role:list', sort: 2 },
      { id: 4, parent_id: 1, name: '菜单管理', path: '/system/menu', component: 'system/menu', icon: 'tree-table', type: 2, permission: 'system:menu:list', sort: 3 }
    ]
  },
  {
    id: 5,
    parent_id: 0,
    name: '学校管理',
    path: '/school',
    component: 'Layout',
    icon: 'school',
    type: 1,
    permission: null,
    sort: 50,
    children: [
      { id: 6, parent_id: 5, name: '学校列表', path: '/school/list', component: 'school/list', icon: 'list', type: 2, permission: 'school:list', sort: 1 },
      { id: 7, parent_id: 5, name: '班级管理', path: '/school/class', component: 'school/class', icon: 'peoples', type: 2, permission: 'school:class:list', sort: 2 }
    ]
  }
])

const handleAdd = () => {
  ElMessage.info('功能开发中...')
}

const handleEdit = (row) => {
  ElMessage.info('功能开发中...')
}

const handleDelete = (row) => {
  ElMessage.info('功能开发中...')
}

onMounted(() => {
  // 加载菜单列表
})
</script>

<style scoped>
.menu-page {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
