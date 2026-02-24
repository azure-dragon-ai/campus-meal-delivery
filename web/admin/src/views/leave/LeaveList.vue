<template>
  <div class="leave-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>请假管理</span>
        </div>
      </template>

      <el-table :data="tableData" border v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="dinerName" label="就餐人" />
        <el-table-column prop="phone" label="联系电话" />
        <el-table-column prop="leaveDate" label="请假日期" />
        <el-table-column prop="reason" label="请假原因" show-overflow-tooltip />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 0">待审核</el-tag>
            <el-tag v-else-if="row.status === 1" type="success">通过</el-tag>
            <el-tag v-else type="danger">拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="success" link @click="handleAudit(row, 1)">通过</el-button>
            <el-button type="danger" link @click="handleAudit(row, 2)">拒绝</el-button>
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

const handleAudit = (row, status) => {
  ElMessage.info('功能开发中...')
}
</script>

<style scoped>
.leave-page {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
