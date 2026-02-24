<template>
  <div class="role-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>角色管理</span>
          <el-button type="primary" @click="handleAdd">新增角色</el-button>
        </div>
      </template>

      <el-table :data="tableData" border v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="角色名称" />
        <el-table-column prop="code" label="角色编码" />
        <el-table-column prop="description" label="角色描述" />
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
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

const loading = ref(false)
const tableData = ref([
  { id: 1, name: '超级管理员', code: 'SUPER_ADMIN', description: '拥有所有权限', status: 1 },
  { id: 2, name: '普通管理员', code: 'ADMIN', description: '普通管理员权限', status: 1 },
  { id: 3, name: '教师', code: 'TEACHER', description: '教师权限', status: 1 },
  { id: 4, name: '家长', code: 'PARENT', description: '家长权限', status: 1 }
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
  // 加载角色列表
})
</script>

<style scoped>
.role-page {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
