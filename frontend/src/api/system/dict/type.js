import request from '@/utils/request'

const BASE = '/dict/type'

export function listType(query) {
  return request({
    url: `/dict/typeList`,
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
