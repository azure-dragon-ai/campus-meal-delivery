<template>
  <div class="meal-reg-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>配餐登记管理</span>
          <el-button type="primary" @click="handleAdd">新增登记</el-button>
        </div>
      </template>

      <el-form :inline="true" class="query-form">
        <el-form-item label="学期">
          <el-select v-model="queryForm.semesterId" placeholder="请选择学期" clearable @change="handleQuery">
            <el-option v-for="item in semesters" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="queryForm.keyword" placeholder="姓名/身份证号/手机号" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="semesterName" label="学期" />
        <el-table-column prop="dinerName" label="就餐人" />
        <el-table-column prop="idCard" label="身份证号" />
        <el-table-column prop="phone" label="联系电话" />
        <el-table-column prop="schoolName" label="学校" />
        <el-table-column label="在校配餐" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isEat === 1 ? 'success' : 'danger'">
              {{ row.isEat === 1 ? '是' : '否' }}
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

      <el-pagination
        v-model:current-page="queryForm.page"
        v-model:page-size="queryForm.size"
        :total="total"
        @current-change="getList"
        @size-change="getList"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="学期" prop="semesterId">
          <el-select v-model="form.semesterId" placeholder="请选择学期" style="width: 100%" :disabled="isEdit">
            <el-option v-for="item in semesters" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="就餐人" prop="dinerId">
          <el-input v-model="form.dinerName" placeholder="请输入就餐人姓名" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="学校" prop="schoolId">
          <el-select v-model="form.schoolId" placeholder="请选择学校" style="width: 100%">
            <el-option v-for="item in schools" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否在校配餐" prop="isEat">
          <el-radio-group v-model="form.isEat">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" rows="3" placeholder="请输入备注" />
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
import { getMealRegList, createMealReg, updateMealReg, deleteMealReg } from '@/api/mealReg'
import { getActiveSemesters } from '@/api/semester'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const semesters = ref([])
const schools = ref([])

const queryForm = reactive({ page: 1, size: 10, semesterId: null, keyword: '' })

const form = reactive({
  id: null,
  semesterId: null,
  dinerId: null,
  dinerName: '',
  idCard: '',
  phone: '',
  schoolId: null,
  classId: null,
  isEat: 1,
  remark: ''
})

const rules = {
  semesterId: [{ required: true, message: '请选择学期', trigger: 'change' }],
  dinerName: [{ required: true, message: '请输入就餐人姓名', trigger: 'blur' }],
  schoolId: [{ required: true, message: '请选择学校', trigger: 'change' }],
  isEat: [{ required: true, message: '请选择是否在校配餐', trigger: 'change' }]
}

const formRef = ref(null)

const getList = async () => {
  loading.value = true
  try {
    const res = await getMealRegList(queryForm)
    tableData.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const loadSemesters = async () => {
  const res = await getActiveSemesters()
  semesters.value = res.data
}

const handleQuery = () => {
  queryForm.page = 1
  getList()
}

const handleReset = () => {
  queryForm.keyword = ''
  queryForm.semesterId = null
  handleQuery()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增配餐登记'
  Object.assign(form, { id: null, semesterId: null, dinerId: null, dinerName: '', idCard: '', phone: '', schoolId: null, classId: null, isEat: 1, remark: '' })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑配餐登记'
  Object.assign(form, {
    id: row.id,
    semesterId: row.semesterId,
    dinerId: row.dinerId,
    dinerName: row.dinerName,
    idCard: row.idCard,
    phone: row.phone,
    schoolId: row.schoolId,
    classId: row.classId,
    isEat: row.isEat,
    remark: row.remark
  })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该登记吗？', '提示', { type: 'warning' })
    .then(async () => {
      await deleteMealReg(row.id)
      ElMessage.success('删除成功')
      getList()
    })
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await updateMealReg(form.id, form)
        } else {
          await createMealReg(form)
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

onMounted(() => {
  getList()
  loadSemesters()
})
</script>

<style scoped>
.meal-reg-page { height: 100%; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.query-form { margin-bottom: 20px; }
</style>
