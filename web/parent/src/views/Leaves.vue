<template>
  <div class="leaves-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>请假记录</span>
          <el-button type="primary" @click="handleAdd">新建请假</el-button>
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
            <el-button v-if="row.status === 0" type="warning" link @click="handleCancel(row)">取消</el-button>
            <el-button type="primary" link @click="handleView(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination v-model:current-page="queryForm.page" v-model:page-size="queryForm.size"
        :total="total" @current-change="loadLeaves" @size-change="loadLeaves"
        style="margin-top: 20px; justify-content: flex-end" />
    </el-card>

    <el-dialog v-model="dialogVisible" title="新建请假" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="就餐人" prop="dinerId">
          <el-select v-model="form.dinerId" placeholder="请选择就餐人" style="width: 100%">
            <el-option v-for="item in diners" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="请假日期" prop="leaveDate">
          <el-date-picker v-model="form.leaveDate" type="date" placeholder="选择日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="请假原因" prop="reason">
          <el-input v-model="form.reason" type="textarea" rows="3" placeholder="请输入请假原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getLeaves, createLeave, cancelLeave, getDiners } from '@/api/parent'

const loading = ref(false), dialogVisible = ref(false)
const tableData = ref([]), diners = ref([]), total = ref(0)
const queryForm = reactive({ page: 1, size: 10 })
const form = reactive({ dinerId: null, leaveDate: null, reason: '' })
const rules = {
  dinerId: [{ required: true, message: '请选择就餐人', trigger: 'change' }],
  leaveDate: [{ required: true, message: '请选择请假日期', trigger: 'change' }]
}
const formRef = ref(null)

const loadLeaves = async () => {
  loading.value = true
  try { const res = await getLeaves(queryForm); tableData.value = res.data.records; total.value = res.data.total }
  catch (error) { console.error(error) }
  finally { loading.value = false }
}

const loadDiners = async () => {
  try { const res = await getDiners(); diners.value = res.data }
  catch (error) { console.error(error) }
}

const handleAdd = async () => {
  await loadDiners()
  if (diners.value.length === 0) { ElMessage.warning('请先添加就餐人'); return }
  Object.assign(form, { dinerId: null, leaveDate: null, reason: '' })
  dialogVisible.value = true
}

const handleCancel = (row) => {
  ElMessageBox.confirm('确认取消该请假申请吗？', '提示', { type: 'warning' })
    .then(async () => { await cancelLeave(row.id); ElMessage.success('取消成功'); loadLeaves() })
}

const handleView = (row) => { ElMessage.info('详情功能开发中...') }

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const data = { ...form, leaveDate: formatDate(form.leaveDate) }
        await createLeave(data)
        ElMessage.success('提交成功'); dialogVisible.value = false; loadLeaves()
      } catch (error) { console.error(error) }
    }
  })
}

const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

onMounted(() => { loadLeaves() })
</script>

<style scoped>
.leaves-page { height: 100%; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
