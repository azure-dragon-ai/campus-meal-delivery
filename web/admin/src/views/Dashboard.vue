<template>
  <div class="dashboard">
    <el-card>
      <h2>欢迎使用校园配餐管理系统</h2>
      <el-row :gutter="20" style="margin-top: 30px">
        <el-col :span="6">
          <el-statistic title="学校数量" :value="10" />
        </el-col>
        <el-col :span="6">
          <el-statistic title="班级数量" :value="120" />
        </el-col>
        <el-col :span="6">
          <el-statistic title="教师数量" :value="350" />
        </el-col>
        <el-col :span="6">
          <el-statistic title="就餐人数量" :value="2500" />
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
</script>

<style scoped>
.dashboard {
  height: 100%;
}
</style>
