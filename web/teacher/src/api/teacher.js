import request from '@/utils/request'

export function login(data) {
  return request({ url: '/teacher/login', method: 'post', data })
}

export function register(data) {
  return request({ url: '/teacher/register', method: 'post', data })
}

export function getInfo() {
  return request({ url: '/teacher/info', method: 'get' })
}

export function getDiners() {
  return request({ url: '/teacher/diners', method: 'get' })
}

export function getLeaves(params) {
  return request({ url: '/teacher/leaves', method: 'get', params })
}

export function auditLeave(id, status, remark) {
  return request({
    url: `/teacher/leaves/${id}/audit`,
    method: 'post',
    params: { status, remark }
  })
}
