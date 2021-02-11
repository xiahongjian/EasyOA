import request from '@/utils/request'

const BASE = '/dict/type'

export function listType(query) {
  return request({
    url: `/dict/types`,
    method: 'get',
    params: query
  })
}

export function getType(id) {
  return request({
    url: `${BASE}/${id}`,
    method: 'get'
  })
}

export function addType(data) {
  return request({
    url: BASE,
    method: 'post',
    data
  })
}

export function updateType(id, data) {
  return request({
    url: `${BASE}/${id}`,
    method: 'put',
    data
  })
}

export function delType(id) {
  if (id instanceof Array) {
    return request({
      url: `${BASE}/batch`,
      method: 'delete',
      params: {
        ids: id + ''
      }
    })
  }
  return request({
    url: `${BASE}/${id}`,
    method: 'delete'
  })
}
