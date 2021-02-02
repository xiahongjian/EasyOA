import request from '@/utils/request'

export function listDept(form) {
  return request({
    url: '/departments',
    method: 'get',
    params: form
  })
}

export function getDept(id) {
  return request({
    url: `/department/${id}`,
    method: 'get'
  })
}

export function createDept(form) {
  return request({
    url: '/deparment',
    data: form
  })
}

export function updateDept(id, form) {
  return request({
    url: `/department/${id}`,
    method: 'put',
    data: form
  })
}

export function deleteDept(id) {
  if (id instanceof Array) {
    return request({
      url: `/department`,
      method: 'delete',
      params: {
        ids: id
      }
    })
  }
  return request({
    url: `/department/${id}`,
    method: 'delete'
  })
}
