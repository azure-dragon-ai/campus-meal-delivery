import request from '@/utils/request'

export function getSemesterList(params) {
  return request({ url: '/semester/list', method: 'get', params })
}

export function getActiveSemesters() {
  return request({ url: '/semester/active', method: 'get' })
}

export function getCurrentSemester() {
  return request({ url: '/semester/current', method: 'get' })
}

export function createSemester(data) {
  return request({ url: '/semester', method: 'post', data })
}

export function updateSemester(id, data) {
  return request({ url: `/semester/${id}`, method: 'put', data })
}

export function deleteSemester(id) {
  return request({ url: `/semester/${id}`, method: 'delete' })
}

export function setCurrentSemester(id) {
  return request({ url: `/semester/${id}/current`, method: 'put' })
}
