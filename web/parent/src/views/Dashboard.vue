<template>
  <div class="dashboard">
    <el-card>
      <h2>欢迎使用家长系统</h2>
      <p style="margin-top: 20px; color: #666">
        您可以在"就餐人管理"页面管理您的孩子信息，在"请假记录"页面查看和提交请假申请。
      </p>
      <el-button type="primary" @click="$router.push('/diners')" style="margin-top: 20px">管理就餐人</el-button>
    </el-card>
  </div>
</template>

<script setup>
</script>

<style scoped>
.dashboard { height: 100%; }
</style>
