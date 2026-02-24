<template>
  <div class="leaves-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>请假管理</span>
          <el-button type="success" @click="handleExport">导出请假信息</el-button>
        </div>
      </template>

      <el-form :inline="true" class="query-form">
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="全部" clearable @change="loadLeaves">
            <el-option label="待审核" :value="0" />
            <el-option label="通过" :value="1" />
            <el-option label="拒绝" :value="2" />
          </el-select>
        </el-form-item>
      </el-form>

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
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.status === 0" type="success" link @click="handleAudit(row, 1)">通过</el-button>
            <el-button v-if="row.status === 0" type="danger" link @click="handleAudit(row, 2)">拒绝</el-button>
            <el-button type="primary" link @click="handleView(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="queryForm.page"
        v-model:page-size="queryForm.size"
        :total="total"
        @current-change="loadLeaves"
        @size-change="loadLeaves"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <el-dialog v-model="auditDialogVisible" title="审核请假" width="400px">
      <el-form :model="auditForm" label-width="80px">
        <el-form-item label="审核备注">
          <el-input v-model="auditForm.remark" type="textarea" rows="3" placeholder="请输入审核备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAuditSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getLeaves, auditLeave } from '@/api/teacher'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const auditDialogVisible = ref(false)
const currentLeave = ref(null)

const queryForm = reactive({ page: 1, size: 10, status: null })

const auditForm = reactive({ remark: '' })

const loadLeaves = async () => {
  loading.value = true
  try {
    const res = await getLeaves(queryForm)
    tableData.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleAudit = (row, status) => {
  currentLeave.value = { id: row.id, status }
  auditForm.remark = ''
  auditDialogVisible.value = true
}

const handleAuditSubmit = async () => {
  try {
    await auditLeave(currentLeave.value.id, currentLeave.value.status, auditForm.remark)
    ElMessage.success('审核成功')
    auditDialogVisible.value = false
    loadLeaves()
  } catch (error) {
    console.error(error)
  }
}

const handleView = (row) => {
  ElMessage.info('详情功能开发中...')
}

const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

onMounted(() => {
  loadLeaves()
})
</script>

<style scoped>
.leaves-page { height: 100%; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.query-form { margin-bottom: 20px; }
</style>
