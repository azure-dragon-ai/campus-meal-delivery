import request from '@/utils/request'

// 管理员登录
export function login(data) {
  return request({
    url: '/admin/login',
    method: 'post',
    data
  })
}

// 获取管理员列表
export function getAdminList(params) {
  return request({
    url: '/admin/list',
    method: 'get',
    params
  })
}

// 创建管理员
export function createAdmin(data) {
  return request({
    url: '/admin',
    method: 'post',
    data
  })
}

// 更新管理员
export function updateAdmin(id, data) {
  return request({
    url: `/admin/${id}`,
    method: 'put',
    data
  })
}

// 删除管理员
export function deleteAdmin(id) {
  return request({
    url: `/admin/${id}`,
    method: 'delete'
  })
}

// 重置密码
export function resetPassword(id, newPassword) {
  return request({
    url: `/admin/${id}/password`,
    method: 'put',
    params: { newPassword }
  })
}
