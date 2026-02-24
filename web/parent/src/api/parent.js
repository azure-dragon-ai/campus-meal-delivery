import request from '@/utils/request'

export function login(data) { return request({ url: '/parent/login', method: 'post', data }) }
export function register(data) { return request({ url: '/parent/register', method: 'post', data }) }
export function getDiners() { return request({ url: '/parent/diners', method: 'get' }) }
export function createDiner(data) { return request({ url: '/parent/diners', method: 'post', data }) }
export function updateDiner(id, data) { return request({ url: `/parent/diners/${id}`, method: 'put', data }) }
export function deleteDiner(id) { return request({ url: `/parent/diners/${id}`, method: 'delete' }) }
export function getLeaves(params) { return request({ url: '/parent/leaves', method: 'get', params }) }
export function createLeave(data) { return request({ url: '/parent/leaves', method: 'post', data }) }
export function cancelLeave(id) { return request({ url: `/parent/leaves/${id}/cancel`, method: 'post' }) }
export function getMealRegistrations(params) { return request({ url: '/parent/meal-registrations', method: 'get', params }) }
export function createMealRegistration(data) { return request({ url: '/parent/meal-registrations', method: 'post', data }) }
export function updateMealRegistration(id, data) { return request({ url: `/parent/meal-registrations/${id}`, method: 'put', data }) }
export function deleteMealRegistration(id) { return request({ url: `/parent/meal-registrations/${id}`, method: 'delete' }) }
export function getSemesters() { return request({ url: '/parent/semesters', method: 'get' }) }
