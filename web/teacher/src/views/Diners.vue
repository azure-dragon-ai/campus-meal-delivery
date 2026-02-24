<template>
  <div class="diners-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>班级就餐人</span>
          <el-button type="success" @click="handleExport">导出就餐人</el-button>
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
        <el-table-column prop="idCard" label="身份证号" />
        <el-table-column prop="className" label="班级" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getDiners } from '@/api/teacher'

const loading = ref(false)
const tableData = ref([])

const loadDiners = async () => {
  loading.value = true
  try {
    const res = await getDiners()
    tableData.value = res.data
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

onMounted(() => {
  loadDiners()
})
</script>

<style scoped>
.diners-page { height: 100%; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
