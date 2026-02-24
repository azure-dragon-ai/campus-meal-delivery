import request from '@/utils/request'

export function getRecipeList(params) {
  return request({ url: '/recipe/list', method: 'get', params })
}

export function createRecipe(data) {
  return request({ url: '/recipe', method: 'post', data })
}

export function updateRecipe(id, data) {
  return request({ url: `/recipe/${id}`, method: 'put', data })
}

export function deleteRecipe(id) {
  return request({ url: `/recipe/${id}`, method: 'delete' })
}

export function getWeekRecipe(params) {
  return request({ url: '/recipe/week', method: 'get', params })
}
