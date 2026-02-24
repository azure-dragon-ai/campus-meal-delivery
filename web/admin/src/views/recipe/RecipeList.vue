<template>
  <div class="recipe-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>食谱管理</span>
          <el-button type="primary" @click="handleAdd">新增食谱</el-button>
        </div>
      </template>

      <el-form :inline="true" class="query-form">
        <el-form-item label="日期范围">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="date" label="日期" />
        <el-table-column prop="weekDayName" label="星期" width="80" />
        <el-table-column prop="lunchMenu" label="午餐菜谱" show-overflow-tooltip />
        <el-table-column prop="lunchMenuWithWeight" label="带量午餐菜谱" show-overflow-tooltip />
        <el-table-column prop="dinnerMenu" label="晚餐菜谱" show-overflow-tooltip />
        <el-table-column prop="dinnerMenuWithWeight" label="带量晚餐菜谱" show-overflow-tooltip />
        <el-table-column prop="createName" label="创建人" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="日期" prop="date">
          <el-date-picker v-model="form.date" type="date" placeholder="选择日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="星期" prop="weekDay">
          <el-select v-model="form.weekDay" placeholder="请选择星期" style="width: 100%">
            <el-option label="周一" :value="1" />
            <el-option label="周二" :value="2" />
            <el-option label="周三" :value="3" />
            <el-option label="周四" :value="4" />
            <el-option label="周五" :value="5" />
            <el-option label="周六" :value="6" />
            <el-option label="周日" :value="7" />
          </el-select>
        </el-form-item>
        <el-form-item label="午餐菜谱" prop="lunchMenu">
          <el-input v-model="form.lunchMenu" placeholder="请输入午餐菜谱" />
        </el-form-item>
        <el-form-item label="带量午餐菜谱" prop="lunchMenuWithWeight">
          <el-input v-model="form.lunchMenuWithWeight" type="textarea" rows="3" placeholder="请输入带量午餐菜谱" />
        </el-form-item>
        <el-form-item label="晚餐菜谱" prop="dinnerMenu">
          <el-input v-model="form.dinnerMenu" placeholder="请输入晚餐菜谱" />
        </el-form-item>
        <el-form-item label="带量晚餐菜谱" prop="dinnerMenuWithWeight">
          <el-input v-model="form.dinnerMenuWithWeight" type="textarea" rows="3" placeholder="请输入带量晚餐菜谱" />
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
import { getRecipeList, createRecipe, updateRecipe, deleteRecipe } from '@/api/recipe'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const dateRange = ref([])

const queryForm = reactive({ page: 1, size: 10, startDate: '', endDate: '' })

const form = reactive({
  id: null,
  semesterId: 1,
  schoolId: 1,
  date: null,
  weekDay: null,
  lunchMenu: '',
  lunchMenuWithWeight: '',
  dinnerMenu: '',
  dinnerMenuWithWeight: ''
})

const rules = {
  date: [{ required: true, message: '请选择日期', trigger: 'change' }],
  weekDay: [{ required: true, message: '请选择星期', trigger: 'change' }]
}

const formRef = ref(null)

const getList = async () => {
  loading.value = true
  try {
    const res = await getRecipeList(queryForm)
    tableData.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryForm.page = 1
  if (dateRange.value && dateRange.value.length === 2) {
    queryForm.startDate = formatDate(dateRange.value[0])
    queryForm.endDate = formatDate(dateRange.value[1])
  }
  getList()
}

const handleReset = () => {
  dateRange.value = []
  queryForm.startDate = ''
  queryForm.endDate = ''
  handleQuery()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增食谱'
  Object.assign(form, { id: null, date: null, weekDay: null, lunchMenu: '', lunchMenuWithWeight: '', dinnerMenu: '', dinnerMenuWithWeight: '' })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑食谱'
  Object.assign(form, {
    id: row.id,
    date: row.date,
    weekDay: row.weekDay,
    lunchMenu: row.lunchMenu,
    lunchMenuWithWeight: row.lunchMenuWithWeight,
    dinnerMenu: row.dinnerMenu,
    dinnerMenuWithWeight: row.dinnerMenuWithWeight
  })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该食谱吗？', '提示', { type: 'warning' })
    .then(async () => {
      await deleteRecipe(row.id)
      ElMessage.success('删除成功')
      getList()
    })
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const data = { ...form, date: formatDate(form.date) }
        if (isEdit.value) {
          await updateRecipe(form.id, data)
        } else {
          await createRecipe(data)
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
.recipe-page { height: 100%; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.query-form { margin-bottom: 20px; }
</style>
