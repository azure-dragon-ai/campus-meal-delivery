<template>
  <div class="diners-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>就餐人管理</span>
          <el-button type="primary" @click="handleAdd">新建就餐人</el-button>
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
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role" placeholder="请选择角色" style="width: 100%">
            <el-option label="学生" :value="1" />
            <el-option label="教师" :value="2" />
            <el-option label="校干" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="退款收款人" prop="bankAccountName">
          <el-input v-model="form.bankAccountName" placeholder="请输入退款收款人" />
        </el-form-item>
        <el-form-item label="开户行" prop="bankName">
          <el-input v-model="form.bankName" placeholder="请输入开户行" />
        </el-form-item>
        <el-form-item label="支行名称" prop="bankSubbranch">
          <el-input v-model="form.bankSubbranch" placeholder="请输入支行名称" />
        </el-form-item>
        <el-form-item label="银行账号" prop="bankAccount">
          <el-input v-model="form.bankAccount" placeholder="请输入银行账号" />
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
import { getDiners, createDiner, updateDiner, deleteDiner } from '@/api/parent'

const loading = ref(false), dialogVisible = ref(false), dialogTitle = ref(''), isEdit = ref(false)
const tableData = ref([])
const form = reactive({ id: null, name: '', role: 1, idCard: '', phone: '', bankAccountName: '', bankName: '', bankSubbranch: '', bankAccount: '' })
const rules = { name: [{ required: true, message: '请输入姓名', trigger: 'blur' }], role: [{ required: true, message: '请选择角色', trigger: 'change' }] }
const formRef = ref(null)

const loadDiners = async () => {
  loading.value = true
  try { const res = await getDiners(); tableData.value = res.data }
  catch (error) { console.error(error) }
  finally { loading.value = false }
}

const handleAdd = () => {
  isEdit.value = false; dialogTitle.value = '新建就餐人'
  Object.assign(form, { id: null, name: '', role: 1, idCard: '', phone: '', bankAccountName: '', bankName: '', bankSubbranch: '', bankAccount: '' })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true; dialogTitle.value = '编辑就餐人'
  Object.assign(form, { id: row.id, name: row.name, role: row.role, idCard: row.idCard, phone: row.phone, bankAccountName: row.bankAccountName, bankName: row.bankName, bankSubbranch: row.bankSubbranch, bankAccount: row.bankAccount })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该就餐人吗？', '提示', { type: 'warning' })
    .then(async () => { await deleteDiner(row.id); ElMessage.success('删除成功'); loadDiners() })
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const { id, ...data } = form
        if (isEdit.value) { await updateDiner(id, data) }
        else { await createDiner(data) }
        ElMessage.success('操作成功'); dialogVisible.value = false; loadDiners()
      } catch (error) { console.error(error) }
    }
  })
}

onMounted(() => { loadDiners() })
</script>

<style scoped>
.diners-page { height: 100%; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
