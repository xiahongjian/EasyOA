import request from '@/utils/request'

export function listLeaveForms(query) {
  return request({
    url: 'leaveForms',
    method: 'get',
    params: query
  })
}

export function createLeaveForm(form) {
  return request({
    url: 'leaveForm',
    method: 'post',
    data: form
  })
}

export function updateLeaveForm(id, form) {
  return request({
    url: `leveForm/${id}`,
    method: 'put',
    data: form
  })
}

export function getLeaveForm(id) {
  return request({
    url: `leaveForm/${id}`,
    method: 'get'
  })
}
