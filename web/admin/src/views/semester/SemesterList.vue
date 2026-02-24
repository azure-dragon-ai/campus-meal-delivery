<template>
  <div class="semester-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>学期管理</span>
          <el-button type="primary" @click="handleAdd">新增学期</el-button>
        </div>
      </template>

      <el-table :data="tableData" border v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="学期名称" />
        <el-table-column prop="startDate" label="开始日期" />
        <el-table-column prop="endDate" label="结束日期" />
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="当前学期" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.isCurrent === 1" type="primary">是</el-tag>
            <el-tag v-else>否</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="row.isCurrent !== 1" type="warning" link @click="handleSetCurrent(row)">设为当前</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="queryForm.page"
        v-model:page-size="queryForm.size"
        :total="total"
        @current-change="getList"
        @size-change="getList"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="学期名称" prop="name">
          <el-input v-model="form.name" placeholder="如：2024 年春季学期" />
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker v-model="form.startDate" type="date" placeholder="选择开始日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker v-model="form.endDate" type="date" placeholder="选择结束日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="当前学期" prop="isCurrent">
          <el-radio-group v-model="form.isCurrent">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
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
import { getSemesterList, createSemester, updateSemester, deleteSemester, setCurrentSemester } from '@/api/semester'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)

const queryForm = reactive({ page: 1, size: 10, keyword: '' })

const form = reactive({
  id: null,
  name: '',
  startDate: null,
  endDate: null,
  status: 1,
  isCurrent: 0
})

const rules = {
  name: [{ required: true, message: '请输入学期名称', trigger: 'blur' }],
  startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }]
}

const formRef = ref(null)

const getList = async () => {
  loading.value = true
  try {
    const res = await getSemesterList(queryForm)
    tableData.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增学期'
  Object.assign(form, { id: null, name: '', startDate: null, endDate: null, status: 1, isCurrent: 0 })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑学期'
  Object.assign(form, {
    id: row.id,
    name: row.name,
    startDate: row.startDate,
    endDate: row.endDate,
    status: row.status,
    isCurrent: row.isCurrent
  })
  dialogVisible.value = true
}

const handleSetCurrent = async (row) => {
  ElMessageBox.confirm('确认设为当前学期吗？', '提示', { type: 'warning' })
    .then(async () => {
      await setCurrentSemester(row.id)
      ElMessage.success('设置成功')
      getList()
    })
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该学期吗？', '提示', { type: 'warning' })
    .then(async () => {
      await deleteSemester(row.id)
      ElMessage.success('删除成功')
      getList()
    })
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const data = {
          ...form,
          startDate: formatDate(form.startDate),
          endDate: formatDate(form.endDate)
        }
        if (isEdit.value) {
          await updateSemester(form.id, data)
        } else {
          await createSemester(data)
        }
        ElMessage.success('操作成功')
        dialogVisible.value = false
        getList()
      } catch (error) {
        console.error(error)
      }
    }
  })
}

const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.semester-page { height: 100%; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
