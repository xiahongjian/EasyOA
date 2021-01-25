import request from '@/utils/request'

export function listRole(query) {
  return request({
    url: '/roles',
    method: 'get',
    params: query
  })
}

export function createRole(form) {
  return request({
    url: '/role',
    method: 'post',
    data: form
  })
}

export function getRole(id) {
  return request({
    url: `/role/${id}`,
    method: 'get'
  })
}

export function updateRole(id, form) {
  return request({
    url: `/role/${id}`,
    method: 'put',
    data: form
  })
}

export function deleteRole(id) {
  return request({
    url: `/role/${id}`,
    method: 'delete'
  })
}
