import request from '@/utils/request'

export function listType(query) {
  return request({
    url: '/dict/typelist',
    method: 'get',
    params: query
  })
}
