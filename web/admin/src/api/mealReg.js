import request from '@/utils/request'

export function getMealRegList(params) {
  return request({ url: '/meal-reg/list', method: 'get', params })
}

export function createMealReg(data) {
  return request({ url: '/meal-reg', method: 'post', data })
}

export function updateMealReg(id, data) {
  return request({ url: `/meal-reg/${id}`, method: 'put', data })
}

export function deleteMealReg(id) {
  return request({ url: `/meal-reg/${id}`, method: 'delete' })
}

export function importMealReg(semesterId, file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({ url: `/meal-reg/import?semesterId=${semesterId}`, method: 'post', data: formData })
}
