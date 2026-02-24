<template>
  <div class="diner-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>就餐人管理</span>
        </div>
      </template>

      <el-table :data="tableData" border v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column label="角色" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.role === 1">学生</el-tag>
            <el-tag v-else-if="row.role === 2" type="success">教师</el-tag>
            <el-tag v-else type="warning">校干</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="联系电话" />
        <el-table-column prop="schoolName" label="学校" />
        <el-table-column prop="className" label="班级" />
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
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const tableData = ref([])

const handleEdit = (row) => {
  ElMessage.info('功能开发中...')
}

const handleDelete = (row) => {
  ElMessage.info('功能开发中...')
}
</script>

<style scoped>
.diner-page {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
