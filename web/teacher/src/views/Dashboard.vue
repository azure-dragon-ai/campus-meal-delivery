<template>
  <div class="dashboard">
    <el-card>
      <h2>欢迎使用教师系统</h2>
      <el-descriptions :column="2" border style="margin-top: 20px">
        <el-descriptions-item label="姓名">{{ teacherInfo.name }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ teacherInfo.phone }}</el-descriptions-item>
        <el-descriptions-item label="学校">{{ teacherInfo.schoolName }}</el-descriptions-item>
        <el-descriptions-item label="班级">{{ teacherInfo.className }}</el-descriptions-item>
        <el-descriptions-item label="职务">{{ teacherInfo.position }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getInfo } from '@/api/teacher'

const teacherInfo = ref({})

onMounted(async () => {
  try {
    const res = await getInfo()
    teacherInfo.value = res.data
  } catch (error) {
    console.error(error)
  }
})
</script>

<style scoped>
.dashboard { height: 100%; }
</style>
